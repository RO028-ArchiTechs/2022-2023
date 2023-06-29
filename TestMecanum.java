package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.State;
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
import org.firstinspires.ftc.teamcode.lib.*;

@TeleOp(name="TestMecanum", group="Iterative Opmode")
public class TestMecanum extends OpMode
{
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
   
    // Hardware declarations
    private HardwareDrivetrainMecanum drivetrain;
    
    // hardware constants -- can be public because we may use them 
    // length units in mm (Milimeters)
    public double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    public double COUNTS_PER_MOTOR_REV_Neverest40 = 384.5;
    public double COUNTS_PER_MOTOR_REV_Neverest20 = 751.8;
    public double COUNTS_PER_MOTOR_TNADO = 1440.0;
    
    public double DRIVE_GEAR_REDUCTION = 1.0;
    public double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.39;
    private double minSpeed = 0.15;
    
    public TestMecanum()
    {
        //empty Constructor (inside an OpMode and is thus _acceptable_)
    }
    
    // so that we may elegantly have state change detection 
    Gamepad prevgamepad1;
    
    public void copyGamepad( Gamepad source, Gamepad target )
    {
        try{
        target.copy(source);
        }
        catch(RobotCoreException gamepadfutut)
        {
            telemetry.addLine("C3V4 S3 FU7U l4 g4m3p4d");
                // We never managed to get this exception thrown
            telemetry.update();
        }
    }
    
    /*
     * Code to run after the driver hits INIT
     */
    @Override
    public void init()
    {
        drivetrain = new HardwareDrivetrainMecanum(hardwareMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda); // legacy implementation !!
        telemetry.addData("Status", "Initialized");
        prevgamepad1 = new Gamepad();
        copyGamepad(gamepad1, prevgamepad1);
        
        telemetry.addData("Say", "Mecanum test");
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
        
        // Drivetrain input on Gp1
        double drive  = -gamepad1.left_stick_y;
        double strafe =  gamepad1.left_stick_x;
        double turn   = -gamepad1.right_stick_x;
        double boost  =  gamepad1.right_trigger;
        double shift  =  gamepad1.left_trigger;  
           
        double speed =  (1 - boost - shift) * defSpeed + boost + minSpeed * shift;
        // saving current gamepad state so we may compare against it n the next iteration        
        copyGamepad(gamepad1, prevgamepad1);        
        
        // sending calculated values to the hardware
        drivetrain.DriveWPower(drivetrain.calcPower(drive, strafe, turn, speed));
        
        // robot.slide(100.0);
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }

    // @Override
    // public void stop() {
    //     //robot.slider.robot.slide(robot.positions.get(0));
    // }
}
