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
    private Servo wrist;
    private Servo arm;
    

    // useful constants
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double DRIVE_GEAR_REDUCTION = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    private double COUNTS_PER_MOTOR_REV_Neverest40 = 1120.0;
    private double WINCH_RADIUS = 56.0;
    private double GRIP_POSITION = 0.3;
    private double RELEASE_POSITION = 0.18;
    private double ARM_SPEED_UP = 0.1;
    private double ARM_SPEED_DOWN = 0.05;
    private double ARM_LOWER_POSITION = 0.142;
    private double ARM_UPPER_POSITION = 0.737;
    private double WRIST_LOWER_POSITION = 0.950;
//    private double WRIST_UPPER_POSITION = 0.250;
    private double WRIST_UPPER_POSITION = WRIST_LOWER_POSITION;
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.0;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.42;
    private double minSpeed = 0.15;
    private double MANUAL_SLIDE_SPEED = 5.0;
    private double MANUAL_GRIP_SPEED = 0.05;
     
    // state variables
    private int cycler = 0;
    private double arming = 0;
    private double smootharming = 0;
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
        arm.setPosition((1.0-t)*ARM_LOWER_POSITION + t*ARM_UPPER_POSITION);
        wrist.setPosition((1.0-t)*WRIST_LOWER_POSITION + t*WRIST_UPPER_POSITION);
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
        arm = hardwareMap.get(Servo.class, "ARM");
        wrist = hardwareMap.get(Servo.class, "WST");
        gripping = 0.0;
        arming = 0.0;
        smootharming = 0.0;
        localGripper.grip(gripping);
        armSetPosition(arming);
        positions.add(0.0);     //intake
        positions.add(150.0);   //coaster
        positions.add(1200.0);  //low pole
        telemetry.addData("Say", "To infinity and beyond!");
        localMode = Mode.AUTO;
    }
    

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
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
            arming = (cycler>2 ? 1.0 : 0.0);
            break;
            case MANUAL:
            //separate control per-axis
            gripping = Range.clip(gripping + MANUAL_GRIP_SPEED * ( gamepad1.dpad_right ? 1.0 : (gamepad1.dpad_left ? -1.0 : 0.0)), 0.0, 1.0);
            sliding = Range.clip(sliding + MANUAL_SLIDE_SPEED * ( gamepad1.dpad_up ? 1.0 : (gamepad1.dpad_down ? -1.0 : 0.0)), 0.0, 1330.0);
            arming = (!prevgamepad.a && gamepad1.a ? 0.0 : arming);
            arming = (!prevgamepad.b && gamepad1.b ? 1.0 : arming);
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
        smootharming = (arming > 0.35 ? (1.0-ARM_SPEED_UP)*smootharming + ARM_SPEED_UP*arming : (1.0-ARM_SPEED_DOWN)*smootharming + ARM_SPEED_DOWN*arming);
        armSetPosition(smootharming);
        localGripper.grip(gripping);
        localSlider.slide(sliding);
        localDrivetrain.DriveWPower(localDrivetrain.calcPower(drive, strafe, turn, speed));
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("MODE", "" + localMode.toString());
        telemetry.addData("Slider", "t: %.2f, c: %.2f", sliding, localSlider.getExtension());
        telemetry.addData("Gripper", "g: %.2f", gripping);
        telemetry.update();
    }

    @Override
    public void stop() {
        localSlider.slide(positions.get(0));
    }
}