package org.firstinspires.ftc.teamcode.lib;

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
    private double SLIDE_POWER = 0.8;
    
    private double COUNTS_PER_MOTOR_REV;
    private double WINCH_RADIUS;
    private double COUNTS_PER_ARM_REV;
    private double COUNTS_PER_mm;
    
    private double MIN_POSITION = 0.0;
    private double MAX_POSITION = 69420.0; // this absurdly high number is here to keep backwards compatibility


    public HardwareSlider()
    {
        
    }

    public HardwareSlider(DcMotor Slider, double COUNTS_PER_MOTOR_REV, double WINCH_RADIUS)
    {
        this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
        this.WINCH_RADIUS = WINCH_RADIUS;
        this.Slider = Slider;
        COUNTS_PER_mm = COUNTS_PER_MOTOR_REV /( WINCH_RADIUS * 2 * Math.PI);
        Slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Slider.setTargetPosition(0);
        Slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Slider.setPower(SLIDE_POWER);
    }
    
    public boolean isSliding() {
        //  if you need to use this, you are very likely doing something really stupid
        return Slider.isBusy();
    }
    
    public void setLimits( double lower, double upper)
    {
        this.MIN_POSITION = lower;
        this.MAX_POSITION = upper;
    }
    
    public double getExtension()
    {
        return (double)(Slider.getCurrentPosition()/COUNTS_PER_mm);
        
    }
    public double getTargetExtension()
    {
        return (double)(Slider.getTargetPosition()/COUNTS_PER_mm);
        
    }
    public void slide(double pos)
    {
        int target = (int)(Range.clip(pos, MIN_POSITION, MAX_POSITION) * COUNTS_PER_mm);
        Slider.setTargetPosition(target);
    }
}
