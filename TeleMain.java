package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@TeleOp(name="TeleopMAIN", group="Iterative Opmode")
public class TeleMain extends OpMode
{
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
   
    // Hardware declarations
    private HardwareDrivetrainMecanum localDrivetrain;
    private HardwareSlider localSlider;
    private HardwareGripper localGripper;
    private HardwarePivot localPivot;
    private HardwareWrist localWrist;
    

    // hardware constants
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double DRIVE_GEAR_REDUCTION = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    private double COUNTS_PER_MOTOR_REV_Neverest40 = 1120.0;
    private double COUNTS_PER_MOTOR_TNADO = 1440.0;
    private double WINCH_RADIUS = 56.0;
    private double ARM_REDUCTION = 10.0;
    private double GRIP_POSITION = 0.3;
    private double RELEASE_POSITION = 0.18;
    private double ARM_SPEED_UP = 0.1;
    private double ARM_SPEED_DOWN = 0.05;
    private double ARM_LOWER_POSITION = 0.0;
    private double ARM_UPPER_POSITION = 146.0;
    private double WRIST_UPPER_POSITION = 0.316;
    private double WRIST_LOWER_POSITION = 1.0;
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.32;
    private double minSpeed = 0.15;
    private double MANUAL_SLIDE_SPEED = 5.0;
    private double MANUAL_GRIP_SPEED = 0.05;
     
    // state variables
    private int cycler = 0;
    private double armPosition = 0;
    private double smootharmPosition = 0;
    private List<Double> positions = new ArrayList<>();
    enum Mode {AUTO, MANUAL}; 
    private Mode localMode; 
    private double gripping;    //parametric
    private double sliding;     //milimeters
    
    public TeleMain()
    {
        //empty Constructor (inside an OpMode and is thus _acceptable_)
    }
    
    // so that we may elegantly have state change detection 
    Gamepad prevgamepad;
    
    public void copyGamepad( Gamepad source, Gamepad target )
    {
        try{
        target.copy(source);
        }
        catch(RobotCoreException gamepadfutut)
        {
            telemetry.addLine("C3V4 S3 FU7U la gamepad");
            telemetry.update();
        }
        
    }
    
    private void armSetPosition( double t)
    {
        localPivot.pivot((1.0-t)*ARM_LOWER_POSITION + t*ARM_UPPER_POSITION);

    }
    
    /*
     * Code to run after the driver hits INIT
     */
    @Override
    public void init()
    {
        telemetry.addData("Status", "Initialized");
        prevgamepad = new Gamepad();
        copyGamepad(gamepad1, prevgamepad);
        localDrivetrain = new HardwareDrivetrainMecanum(hardwareMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda);
        localSlider = new HardwareSlider(hardwareMap, COUNTS_PER_MOTOR_REV_Neverest40, WINCH_RADIUS);
        localGripper = new HardwareGripper(hardwareMap, GRIP_POSITION, RELEASE_POSITION);
        localWrist = new HardwareWrist(hardwareMap, WRIST_LOWER_POSITION, WRIST_UPPER_POSITION);
        localPivot = new HardwarePivot(hardwareMap, COUNTS_PER_MOTOR_TNADO, ARM_REDUCTION);
        localPivot.setLimits(ARM_LOWER_POSITION, ARM_UPPER_POSITION);
        gripping = 0.0;
        armPosition = 0.0;
        smootharmPosition = 0.0;
        localGripper.grip(gripping);
        armSetPosition(armPosition);
        localWrist.setPosition((localPivot.getAngle())/(ARM_UPPER_POSITION-ARM_LOWER_POSITION));
        positions.add(0.0);     //intake
        positions.add(150.0);   //coaster
        positions.add(1150.0);  //low pole
        positions.add(370.0);   //med pole
        positions.add(1150.0);  //high pole
        telemetry.addData("Say", "To infinity and beyond!");
        localMode = Mode.AUTO;
    }
    

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        telemetry.addData("pivot", "c: %.3f; t: %.3f", localPivot.getAngle(), localPivot.getTarget());
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }
    
    @Override
    public void loop()
    {
        // Setup a variable for each drive wheel to save power level for telemetry
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
        double boost = gamepad1.right_trigger;
        double shift = gamepad1.left_trigger;  
        double speed = (1 - boost - shift)*defSpeed + boost + minSpeed*shift;
           
        //toggles mode when driver presses Y 
        localMode = (!prevgamepad.y && gamepad1.y ? (localMode == Mode.AUTO ? Mode.MANUAL : Mode.AUTO) : localMode);
        
        // different input handling depending on current mode
        switch(localMode)
        {
            case AUTO:
            //height cycler logic
            cycler = (!prevgamepad.dpad_up && gamepad1.dpad_up ? cycler+1 : cycler);
            cycler = (!prevgamepad.dpad_down && gamepad1.dpad_down ? cycler-1: cycler);
            cycler = Range.clip(cycler, 0, positions.size()-1);
            gripping = (!prevgamepad.x && gamepad1.x ? 1.0 - gripping : gripping);
            sliding = positions.get(cycler);
            armPosition = (cycler>2 ? 1.0 : 0.0);
            break;
            case MANUAL:
            //separate control per-axis
            gripping = Range.clip(gripping + MANUAL_GRIP_SPEED * ( gamepad1.dpad_right ? 1.0 : (gamepad1.dpad_left ? -1.0 : 0.0)), 0.0, 1.0);
            sliding = Range.clip(sliding + MANUAL_SLIDE_SPEED * ( gamepad1.dpad_up ? 1.0 : (gamepad1.dpad_down ? -1.0 : 0.0)), 0.0, 1330.0);
            // armPosition = (!prevgamepad.a && gamepad1.a ? 0.0 : armPosition);
            // armPosition = (!prevgamepad.b && gamepad1.b ? 1.0 : armPosition);
            armPosition = Range.clip(armPosition-gamepad2.right_stick_y, ARM_LOWER_POSITION, ARM_UPPER_POSITION);
            break;
        }
        
        // disabling boost when slider is above a certain height (mm)
        if(sliding > 500)
        {
            boost = 0.0;
        }
        // saving current gamepad state so we may compare against it n the next iteration        
        copyGamepad(gamepad1, prevgamepad);        
        
        // sending calculated values to the hardware
        smootharmPosition = (armPosition > 0.35 ? (1.0-ARM_SPEED_UP)*smootharmPosition + ARM_SPEED_UP*armPosition : (1.0-ARM_SPEED_DOWN)*smootharmPosition + ARM_SPEED_DOWN*armPosition);
        armSetPosition(smootharmPosition);
        localWrist.setPosition((localPivot.getAngle())/(ARM_UPPER_POSITION-ARM_LOWER_POSITION));
        localGripper.grip(gripping);
        localSlider.slide(sliding);
        localDrivetrain.DriveWPower(localDrivetrain.calcPower(drive, strafe, turn, speed));
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("MODE", "" + localMode.toString());
        telemetry.addData("Slider", "t: %.2f, c: %.2f", sliding, localSlider.getExtension());
        telemetry.addData("Pivot", "t: %.2f, c: %.2f", localPivot.getTarget(), localPivot.getAngle());
        telemetry.addData("Gripper", "g: %.2f", gripping);
        telemetry.update();
    }

    @Override
    public void stop() {
        localSlider.slide(positions.get(0));
    }
}