package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;


public class HardwarePivot
{
    private DcMotorEx pivotMotor;
    private int offset = 0;
    private double COUNTS_PER_MOTOR_REV;
    private double MECH_REDUCTION;
    private double COUNTS_PER_ARM_REV;
    private double COUNTS_PER_deg;
    private double PIVOT_POWER = 0.6;  
    private double LIMIT_LOWER = 0.0;  // in degrees
    private double LIMIT_UPPER = 180.0;
    public DcMotor Pivot;
     
     
     
     public HardwarePivot(DcMotorEx Pivot, double COUNTS_PER_MOTOR_REV, double MECH_REDUCTION)
     {
        this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
        this.MECH_REDUCTION = MECH_REDUCTION;
        
        this.Pivot = Pivot;
        
        COUNTS_PER_ARM_REV = COUNTS_PER_MOTOR_REV*MECH_REDUCTION;
        COUNTS_PER_deg = COUNTS_PER_ARM_REV/360;
        
        Pivot.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        Pivot.setTargetPosition(0);
        Pivot.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        Pivot.setPower(PIVOT_POWER);
        
     }
     
     public void setLimits(double lower, double upper)
     {
         LIMIT_UPPER = upper;
         LIMIT_LOWER = lower;
     }
     
     public void setPower(double power)
     {
         this.PIVOT_POWER = power;
     }
     
    public double getAngle()
    {
        return (double)((Pivot.getCurrentPosition()-offset)/COUNTS_PER_deg);
    }
    
    public void zero()
    {
        offset = -Pivot.getCurrentPosition();
    }
    
    public void offset( int ticks)
    {
        offset += ticks;    
    }
    
        public double getTarget()
    {
        return (double)((Pivot.getTargetPosition()-offset)/COUNTS_PER_deg);
        
    }
        public boolean isPivoting(){
            // if you are using this, you are probably doing something stupid
            return Pivot.isBusy();
        }
    
     
    public void pivot(double pos)
    {
        int target = (int)(Range.clip(pos, LIMIT_LOWER, LIMIT_UPPER) * COUNTS_PER_deg);
        
        Pivot.setTargetPosition(target + offset);
    }
    
    //  There is almost no reason you should ever need this
    // public void pivotWpower(double power)
    // {
    //     Pivot.setPower(power);
    // }
     
}
