package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumIleaBogdan {
    
    DcMotor motorFL;
    DcMotor motorFR;
    DcMotor motorBL;
    DcMotor motorBR;
    
    public Servo gripper;
    public Servo arm;
    
    HardwareMap hardwareMap;
    
    public void FunctiaMagnificaALuiIleaSiSaNuCopiati(double Misc, double miscaDrept, double miscInParte){
        motorFL.setPower(Misc-miscaDrept-miscInParte);
        motorFR.setPower(Misc+miscaDrept+miscInParte);
        motorBL.setPower(Misc-miscaDrept+miscInParte);
        motorBR.setPower(Misc+miscaDrept-miscInParte);
    }
    MecanumIleaBogdan(HardwareMap hardwareMap){
        this.hardwareMap=hardwareMap;
        
        motorFL=hardwareMap.get(DcMotor.class,"FL");
        motorFR=hardwareMap.get(DcMotor.class,"FR");
        motorBL=hardwareMap.get(DcMotor.class,"BL");
        motorBR=hardwareMap.get(DcMotor.class,"BR");
        
        gripper=hardwareMap.get(Servo.class,"GRI");
        arm=hardwareMap.get(Servo.class,"INL");
        
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBR.setDirection(DcMotor.Direction.REVERSE);
    }
}