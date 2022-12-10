package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;


public class HardwarePivot
{
    private DcMotor pivotMotor;

     private double COUNTS_PER_MOTOR_REV;
     private double MECH_REDUCTION;
     private double COUNTS_PER_ARM_REV;
     private double COUNTS_PER_deg;
     private double PIVOT_POWER = 0.7;  
     private double LIMIT_LOWER = 0.0;  // in degrees
     private double LIMIT_UPPER = 180.0;
     private double WRIST_LOWER_POSITION = 0.950;
     private double WRIST_UPPER_POSITION = 0.250;
     HardwareMap hwMap;
     private DcMotor Pivot;
     
     
     
     HardwarePivot(HardwareMap hwMap, double COUNTS_PER_MOTOR_REV, double MECH_REDUCTION)
     {
        this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
        this.MECH_REDUCTION = MECH_REDUCTION;
        this.hwMap = hwMap;
        
        COUNTS_PER_ARM_REV = COUNTS_PER_MOTOR_REV*MECH_REDUCTION;
        COUNTS_PER_deg = COUNTS_PER_ARM_REV/360;
        
        Pivot = hwMap.get(DcMotor.class, "PV");
        Pivot.setDirection(DcMotor.Direction.FORWARD);
        Pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Pivot.setTargetPosition( 0);
        Pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Pivot.setPower(PIVOT_POWER);
        
     }
     
     public void setLimits(double lower, double upper)
     {
         LIMIT_UPPER = upper;
         LIMIT_LOWER = lower;
     }
     
    public double getAngle()
    {
        return (double)(Pivot.getCurrentPosition()/COUNTS_PER_deg);
        
    }
    
        public double getTarget()
    {
        return (double)(Pivot.getTargetPosition()/COUNTS_PER_deg);
        
    }
        public boolean isPivoting(){
            return Pivot.isBusy();
        }
    
     
    public void pivot(double pos)
    {
        int target = (int)(Range.clip(pos, LIMIT_LOWER, LIMIT_UPPER) * COUNTS_PER_deg);
        
        Pivot.setTargetPosition(target);
    }
     
}
