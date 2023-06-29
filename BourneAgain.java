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

@TeleOp(name="Bourne-Again", group="Iterative Opmode")
public class BourneAgain extends OpMode
{
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
   
    // Hardware declarations
    private HardwareRobot robot;
    
    // control channels
    private double intakeElevation = 0.0;
    private double intakeGripping = 0.8;
    private double intakeExtension = 0.0;
    private double scoringExtension = 0.0;
    private double scoringGripping = 0.0;
    private double scoringElevation = 0.8;
    
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.39;
    private double minSpeed = 0.15;
    
    
    public BourneAgain()
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
        robot = new HardwareRobot(hardwareMap);
        
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
        // the speed formula https://www.desmos.com/calculator/cb6flvmdnv
        double speed =  (1 - boost - shift) * defSpeed + boost + minSpeed * shift;
        
        
        /* PLEASE KEEP ALL CONTROLLER READINGS ABOVE THIS LINE */
        // saving current gamepad state so we may compare against it n the next iteration        
        copyGamepad(gamepad1, prevgamepad1);        
        
        
        intakeElevation = Range.clip(intakeElevation + 0.005* ((gamepad1.x ? 1.0 : 0.0)+(gamepad1.y ? -1.0 : 0.0)), 0.00, 0.56);
        intakeExtension = Range.clip(intakeExtension + 1.5* ((gamepad1.dpad_up ? 1.0 : 0.0)+(gamepad1.dpad_down ? -1.5 : 0.0)), 0.0, 290.0);
        intakeGripping = (gamepad1.a ? 0.8 : (gamepad1.b ? 0.4: intakeGripping));
        scoringElevation = Range.clip(scoringElevation + 0.005* ((gamepad2.x ? 1.0 : 0.0)+(gamepad2.y ? -1.0 : 0.0)), 0.00, 1.0);
        scoringExtension = Range.clip(scoringExtension + 1.5* ((gamepad2.dpad_up ? 1.0 : 0.0)+(gamepad2.dpad_down ? -1.0 : 0.0)), 0.0, 1900.0);
        scoringGripping = (gamepad2.a ? 0.8 : (gamepad2.b ? 0.4: scoringGripping));
        
        // sending calculated values to the hardware
        robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(drive, strafe, turn, speed));
        
        robot.intakeSliderL.slide(intakeExtension);
        robot.intakeSliderR.slide(intakeExtension);
        robot.intakeWristL.setPosition(intakeElevation);
        robot.intakeWristR.setPosition(intakeElevation);
        robot.intakeGripper.setPosition(intakeGripping);
        
        robot.scoringSliderL.slide(scoringExtension);
        robot.scoringSliderR.slide(scoringExtension);
        robot.scoringWrist.setPosition(scoringElevation);
        robot.scoringGripper.setPosition(scoringGripping);
        
        
        //printing useful (lol) information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("IN ELEVATION", "%.2f", intakeElevation);
        telemetry.addData("IN GRIPPING", "%.3f", intakeGripping);
        telemetry.addData("H EXT", "%.2f", intakeExtension);
        telemetry.addData("SC ELEVATION", "%.2f", scoringElevation);
        telemetry.addData("SC GRIPPING", "%.3f", scoringGripping);
        telemetry.addData("V EXT", "%.2f", scoringExtension);
        telemetry.update();
    }

    // @Override
    // public void stop() {
    //     //robot.slider.robot.slide(robot.positions.get(0));
    // }
}
