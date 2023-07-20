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
    private DcMotor intakeSliderMotorL;
    private DcMotor intakeSliderMotorR;
    private DcMotor scoringSliderMotorL;
    private DcMotor scoringSliderMotorR;
    //"e"
    // mechanical components -- they have to be public cuz otherwise we can't use them
    public Servo intakeWristR;
    public Servo intakeWristL;
    public Servo intakeGripper;
    public Servo scoringWrist;
    public Servo scoringGripper;
    
    public HardwareDrivetrainMecanum drivetrain;
    public HardwareSlider intakeSliderL;
    public HardwareSlider intakeSliderR;
    public HardwareSlider scoringSliderL;
    public HardwareSlider scoringSliderR;
    
    // hardware constants -- can be public because we may use them in other places
    public double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    public double COUNTS_PER_MOTOR_REV_Neverest40 = 384.5;
    public double COUNTS_PER_MOTOR_REV_Neverest20 = 751.8;
    public double COUNTS_PER_MOTOR_TNADO = 1440.0;
    public double DRIVE_GEAR_REDUCTION = 1.0;
    public double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    
    // parameter units
    
    // length units in mm (Milimeters)
    
    // there is no reason why this should be public so until a good one is found it stays private
    private HardwareMap hardwareMap;
    
    public List<Double> positions = new ArrayList<>(); // this has to be initialised inside the constructor
    
    HardwareRobot(HardwareMap hwMap)
    {
        this.hardwareMap = hwMap; // we recieve the hwmap from the opmode on instantiation
        
        drivetrain = new HardwareDrivetrainMecanum(hardwareMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda); // legacy implementation !!
        
        intakeSliderMotorL = hardwareMap.get(DcMotor.class, "SHL");
        intakeSliderMotorR = hardwareMap.get(DcMotor.class, "SHR");
        intakeWristL = hardwareMap.get(Servo.class, "INL");
        intakeWristR = hardwareMap.get(Servo.class, "INR");
        intakeGripper = hardwareMap.get(Servo.class, "GRI");
        scoringSliderMotorL = hardwareMap.get(DcMotor.class, "SVL");
        scoringSliderMotorR = hardwareMap.get(DcMotor.class, "SVR");
        scoringGripper = hardwareMap.get(Servo.class, "GRS");
        scoringWrist = hardwareMap.get(Servo.class, "SW");
        
        intakeSliderMotorL.setDirection(DcMotor.Direction.FORWARD);  //determined using the right-hand-rule
        intakeSliderMotorR.setDirection(DcMotor.Direction.FORWARD);  // CABLE SHENANIGANS
        scoringSliderMotorL.setDirection(DcMotor.Direction.REVERSE); //determined using the right-hand-rule
        scoringSliderMotorR.setDirection(DcMotor.Direction.FORWARD); //determined using the right-hand-rule
        
        intakeWristL.setDirection(Servo.Direction.FORWARD);
        intakeWristR.setDirection(Servo.Direction.REVERSE);
        intakeGripper.setDirection(Servo.Direction.FORWARD);
        intakeGripper.setDirection(Servo.Direction.FORWARD);
        
        
        intakeSliderL = new HardwareSlider(intakeSliderMotorL, 751.8, 16.0);
        intakeSliderR = new HardwareSlider(intakeSliderMotorR, 751.8, 16.0);
        
        intakeSliderL.setLimits( 0.0, 290.0);
        intakeSliderR.setLimits( 0.0, 290.0);
        
        scoringSliderL = new HardwareSlider(scoringSliderMotorL, 384.5, 18.0);
        scoringSliderR = new HardwareSlider(scoringSliderMotorR, 384.5, 18.0);
        
        scoringSliderL.setLimits( 0.0, 1920.0);
        scoringSliderR.setLimits( 0.0, 1920.0);
        
        positions.add(0.0);
        positions.add(190.0);
        positions.add(975.0);
        positions.add(1695.0);
        
    }
    
}