package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.PwmControl.PwmRange;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.lib.*;
import java.util.*;

public class HardwareRobot
{
    // almost all hardware components -- it is usually not a good idea to manually control them so for now they stay private
    private DcMotor SliderLeftMotor;
    private DcMotor SliderRightMotor;
    private DcMotor SliderIntakeMotor;
    private DcMotorEx PivotMotor;
    
    public Servo GripIntakeServo;    
    public Servo GripScoreServo;    
    private ServoImplEx WristScoringLeftServo;    
    private ServoImplEx WristScoringRightServo; 
    
    // mechanical components -- they have to be public cuz otherwise we can't use them
    public HardwareDrivetrainMecanum drivetrain;
    public HardwareSlider horizslider;
    public HardwareSlider vertsliderL;
    public HardwareSlider vertsliderR;
    public HardwarePivot intakepivot;
    public HardwareWrist scoringwristL;
    public HardwareWrist scoringwristR;
    // there is no reason why this should be public so until a good one is found it stays private
    private HardwareMap hwMap;
    
    // hardware constants -- can be public because we may use them 
    // length units in mm (Milimeters)
    public double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    public double COUNTS_PER_MOTOR_REV_Neverest40 = 384.5;
    public double COUNTS_PER_MOTOR_REV_Neverest20 = 751.8;
    public double COUNTS_PER_MOTOR_TNADO = 1440.0;
    
    public double DRIVE_GEAR_REDUCTION = 1.0;
    public double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    
    public double WINCH_RADIUS = 17.825; //milimeters
    public double H_WINCH_RADIUS = 16.0; //milimeters
    public double VSLIDER_MAX_POSITION = 640;
    public double HSLIDER_MAX_POSITION = 370;
    
    public double ARM_REDUCTION = 2.0;
    public double ARM_LOWER_POSITION = 0.0;
    public double ARM_MID_POSITION = 90.0;
    public double ARM_UPPER_POSITION = 122.0; 
    
    public double INTAKE_GRIP_POSITION = 0.35;
    public double INTAKE_RELEASE_POSITION = 0.0;
    public double SCORE_GRIP_POSITION = 0.69;
    public double SCORE_RELEASE_POSITION = 0.436;
    
    public double WRIST_LOWER_POSITION = 0.22;
    public double WRIST_UPPER_POSITION = 0.90;
    
    public List<Double> positions = new ArrayList<>(); // this has to be initialised inside the constructor
    
    HardwareRobot(HardwareMap hwMap)
    {
        this.hwMap = hwMap;
        PivotMotor = hwMap.get(DcMotorEx.class, "PVT");
        PivotMotor.setDirection(DcMotorEx.Direction.REVERSE);
        GripIntakeServo = hwMap.get(Servo.class, "GI");
        GripScoreServo = hwMap.get(Servo.class, "GS");
        SliderLeftMotor = hwMap.get(DcMotor.class, "SL");
        SliderLeftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        SliderRightMotor = hwMap.get(DcMotor.class, "SR");
        SliderRightMotor.setDirection(DcMotorEx.Direction.FORWARD);
        SliderIntakeMotor = hwMap.get(DcMotor.class, "SI");
//        WristScoringLeftServo = hwMap.get(ServoImplEx.class, "WSL");
        WristScoringRightServo = hwMap.get(ServoImplEx.class, "WSR");
//        WristScoringLeftServo.setPwmRange(new PwmRange(500, 2500));
        WristScoringRightServo.setPwmRange(new PwmRange(500, 2500));
//        WristScoringLeftServo.setDirection(Servo.Direction.FORWARD);
        WristScoringRightServo.setDirection(Servo.Direction.FORWARD);
        drivetrain = new HardwareDrivetrainMecanum(hwMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda); // legacy implementation !!
        vertsliderL = new HardwareSlider(SliderLeftMotor, COUNTS_PER_MOTOR_REV_Neverest40, WINCH_RADIUS);
        vertsliderR = new HardwareSlider(SliderRightMotor, COUNTS_PER_MOTOR_REV_Neverest40, WINCH_RADIUS);
        horizslider = new HardwareSlider(SliderIntakeMotor, COUNTS_PER_MOTOR_REV_Neverest40, H_WINCH_RADIUS);
        intakepivot = new HardwarePivot(PivotMotor, COUNTS_PER_MOTOR_REV_Neverest20, ARM_REDUCTION);
        intakepivot.setLimits(ARM_LOWER_POSITION, ARM_UPPER_POSITION);
        intakepivot.setPower(0.6);
        // scoringwristL = new HardwareWrist(WristScoringLeftServo, WRIST_LOWER_POSITION, WRIST_UPPER_POSITION);
        scoringwristR = new HardwareWrist(WristScoringRightServo, WRIST_LOWER_POSITION, WRIST_UPPER_POSITION);
        
        // vertical slider positions
        positions.add(0.0);     //intake
        positions.add(50.0);   //coaster
        positions.add(120.0);  //low pole
        positions.add(360.0);  //med pole
        positions.add(615.0);  //high pole
    }
    
    public void wristSetPosition(double t)
    {
//        scoringwristL.setPosition(t);
        scoringwristR.setPosition(t);
    }
    public void intakeGrip( double t)
    {
        GripIntakeServo.setPosition((1-t)*INTAKE_RELEASE_POSITION + t*INTAKE_GRIP_POSITION);
    }
    
    public void scoreGrip( double t)
    {
        GripScoreServo.setPosition((1.0-t)*SCORE_RELEASE_POSITION + t*SCORE_GRIP_POSITION);
    }
    
    public void slide(double l)
    {
        vertsliderR.slide(l);    
        vertsliderL.slide(l);    
    }
    
    public void armSetPosition( double t)
    {
        intakepivot.pivot
        (
            (1.0 - t) * ARM_LOWER_POSITION + t * ARM_UPPER_POSITION
        );
    }
    
}