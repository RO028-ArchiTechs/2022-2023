package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDenisa {
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight; 
    DcMotor motorBackLeft; 
    DcMotor motorBackRight; 
    
    Servo gripper;
    Servo arm;
    
    
    
    
    HardwareMap hardwareMap;

     public void Drive(double d, double s, double r)
    {
        motorFrontLeft.setPower(d - r - s); 
        motorFrontRight.setPower(d + r + s); 
        motorBackLeft.setPower(d - r + s); 
        motorBackRight.setPower(d + r - s); 
    } 
    MecanumDenisa(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;
        motorFrontLeft = hardwareMap.get(DcMotor.class, "FL"); 
        motorFrontRight = hardwareMap.get(DcMotor.class, "FR");
        motorBackLeft = hardwareMap.get(DcMotor.class, "BL");
        motorBackRight = hardwareMap.get(DcMotor.class, "BR");
        
        gripper = hardwareMap.get(Servo.class, "GRI");
        arm = hardwareMap.get(Servo.class, "INL");
        
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE); 
        motorBackRight.setDirection(DcMotor.Direction.REVERSE); 
    }
}