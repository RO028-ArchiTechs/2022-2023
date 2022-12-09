package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.*;


public class HardwareSlider 
{
    public DcMotor Slider;
    private double SLIDE_POWER = 0.7;
    
    private double COUNTS_PER_MOTOR_REV;
    private double WINCH_RADIUS;
    private double COUNTS_PER_ARM_REV;
    private double COUNTS_PER_mm;
    private HardwareMap hwMap;

    public HardwareSlider()
    {
        
    }

    public HardwareSlider(HardwareMap hwMap, double COUNTS_PER_MOTOR_REV, double WINCH_RADIUS)
    {
        this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
        this.WINCH_RADIUS = WINCH_RADIUS;
        this.hwMap = hwMap;
        Slider = hwMap.get(DcMotor.class, "SL");
        COUNTS_PER_mm = COUNTS_PER_MOTOR_REV /( WINCH_RADIUS * 2 * Math.PI);
        Slider.setDirection(DcMotor.Direction.FORWARD);
        Slider.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setTargetPosition( 0);
        Slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slider.setPower(SLIDE_POWER);
    }
    public double getExtension()
    {
        return (double)(Slider.getCurrentPosition()/COUNTS_PER_mm);
        
    }
    public void slide(double pos)
    {
        int target = (int)(pos * COUNTS_PER_mm);
        Slider.setTargetPosition(target);
    }
}