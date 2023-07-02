package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

@Autonomous(name="AutismusMaximus", group="Iterative Opmode")
public class AutistRebourne extends LinearOpMode
{

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private HardwareRobot robot;
    private HardwareSlider localSlider;
    private HardwareGripper localGripper;
    private Servo wrist;
    OurMath meth;
    private ColorSensor color;
    
    private double intakeElevation = 0.06;   
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double DRIVE_GEAR_REDUCTION_goBilda = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    private double COUNTS_PER_MOTOR_REV_tetrix = 1120.0;
    private double WINCH_RADIUS = 56.0;
    private double GRIP_POSITION = 0.5;
    private double RELEASE_POSITION = 0.05;
    private double WRIST_LOWER_POSITION = 0.950;
    private double defSpeed = 0.3;
    private double minSpeed = 0.1;
    private int cycler = 0;
    private List<Double> positions = new ArrayList<>();
    enum State{ CYN, MAG, YLW};
    State localState;
        
        
/*
    private double scan()
    {
        int R = color.red();        
        int G = color.green();        
        int B = color.blue();        
        double fR = (double)(R)/255;
        double fG = (double)(G)/255;
        double fB = (double)(B)/255;
        double Cmax = (meth.sMax(meth.sMax(fR, fG), fB));
        double Cmin = (meth.sMin(meth.sMin(fR, fG), fB));
        double delta = Range.clip(Cmax-Cmin, 0.01, 1.0);
//      double delta = 1.0;
        double H = 0.0;
        if(meth.sAbs(Cmax - fR) < 0.01)
        {
            telemetry.addData("Cmax", "R");
            H = 60.0*(((fG-fB)/delta)%6);
        }
        if(meth.sAbs(Cmax - fG) < 0.01)
        {
            telemetry.addData("Cmax", "G");
            H = 60.0*(((fB-fR)/delta)+2.0);
        }
        if(meth.sAbs(Cmax - fB) < 0.01)
        {
            telemetry.addData("Cmax", "B");
            H = 60.0*(((fR-fG)/delta)+4.0);
        }
//        return (H<120 ? State.RED : ( H>120 && H<240 ? State.GRN : State.BLU)); 
        return H;
    }
*/    

    private State scan()
    {
        int r = color.red();
        int g = color.green();
        int b = color.blue();
        State state = State.CYN;
        if (r <=b && r<=g )
        {
            state = State.CYN;                
        }     
        if (g <=r && g<=b )
        {
            state = State.MAG;                
        }     
        if (b <=g && b<=r )
        {
            state = State.YLW;                
        }     
        return state;
    }
    
    @Override
    public void runOpMode()
    {
        robot = new HardwareRobot(hardwareMap);
        color = hardwareMap.get(ColorSensor.class, "CLR");
        telemetry.addData("Status", "Initialized");
        
        waitForStart();
        runtime.reset();
        /// AUTO STARTS HERE ///
        if(opModeIsActive())
        {
            robot.intakeWristL.setPosition(intakeElevation);
            robot.intakeWristR.setPosition(intakeElevation);
            robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(1.0, 0.0, 0.0 ,defSpeed));
            sleep(1100);
            robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(0.0, 0.0, 0.0 ,defSpeed));
            sleep(600);
            
            localState = scan();
            
            sleep(200);
            robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(1.0, 0.0, 0.0 ,defSpeed));
            sleep(300);
            robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(0.0, 0.0, 0.0 ,defSpeed));
            
            telemetry.addData("COLOR:", ""+localState.toString());
            telemetry.update();
            switch(localState)
            {
                case CYN:
                    robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(0.0,-1.0, 0.0 ,defSpeed));
                    sleep(2000);
                    robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(0.0, 0.0, 0.0 ,defSpeed));
                    
                break;
                
                case MAG:
                break;
                
                case YLW:
                    robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(-0.1, 1.0, 0.0 ,defSpeed));
                    sleep(2400);
                    robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(0.0, 0.0, 0.0 ,defSpeed));
                break;
            } 
            sleep(15000);
        }
    }
    
}
