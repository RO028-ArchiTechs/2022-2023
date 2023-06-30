package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;
import java.util.*;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This is NOT an opmode.
 */

public class HardwareDrivetrainMecanum implements DrivetrainInterface
{
    /* Public OpMode members. */
    private DcMotorEx frontLeftDrive;
    private DcMotorEx frontRightDrive;
    private DcMotorEx backLeftDrive;
    private DcMotorEx backRightDrive;
    private double DRIVE_MULTIPLIER = 1.0;
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu

    private double COUNTS_PER_MOTOR_REV;
    private double DRIVE_GEAR_REDUCTION;
    private double WHEEL_CIRCUMFERENCE_MM = 90.0 * Math.PI;
    private double COUNTS_PER_WHEEL_REV;
    private double COUNTS_PER_MM;
    private DRIVE_MODE drivemode;
    /**
     Non-empty constructor
     */
     
    public HardwareDrivetrainMecanum(){
        
    }
    public HardwareDrivetrainMecanum(HardwareMap hwMap, double COUNTS_PER_MOTOR_REV, double DRIVE_GEAR_REDUCTION,
        double WHEEL_CIRCUMFERENCE_MM)
    {
         this.COUNTS_PER_MOTOR_REV = COUNTS_PER_MOTOR_REV;
         this.DRIVE_GEAR_REDUCTION = DRIVE_GEAR_REDUCTION;
         this.WHEEL_CIRCUMFERENCE_MM = 90.0 * Math.PI;
         this.COUNTS_PER_WHEEL_REV = COUNTS_PER_MOTOR_REV;
         this.COUNTS_PER_MM = COUNTS_PER_WHEEL_REV / WHEEL_CIRCUMFERENCE_MM;
         
         frontLeftDrive = hwMap.get(DcMotorEx.class, "FL");
         frontRightDrive = hwMap.get(DcMotorEx.class, "FR");
         backLeftDrive = hwMap.get(DcMotorEx.class, "BL");
         backRightDrive = hwMap.get(DcMotorEx.class, "BR");
         
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        
         frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
         frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
         backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
         backRightDrive.setDirection(DcMotor.Direction.FORWARD);
         
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    
        // Set all motors to zero power
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public Map<String, Double> calcPower(double drive, double strafe, double turn, double speed)
    {
        Map<String, Double> powers = new HashMap<>();
        
        drive *= DRIVE_MULTIPLIER; 
        strafe *= STRAFE_MULTIPLIER; 
        turn *= TURN_MULTIPLIER;
        powers.put("frontRightPower", Range.clip((drive - strafe - turn)*speed, -1.0, 1.0));
        powers.put("frontLeftPower", Range.clip((drive + strafe + turn)*speed, -1.0, 1.0));
        powers.put("backRightPower", Range.clip((drive + strafe - turn)*speed, -1.0, 1.0));
        powers.put("backLeftPower", Range.clip((drive - strafe + turn)*speed, -1.0, 1.0));
    
        return powers;
    }
    
    public Map<String, Double> calcVector(double drive, double strafe, double turn, double value)
    {
        Map<String, Double> vector = new HashMap<>();
        vector.put("frontRight", (drive - strafe - turn));
        vector.put("frontLeft", (drive + strafe + turn));
        vector.put("backRight", (drive + strafe - turn));
        vector.put("backLeft", (drive - strafe + turn));
        vector.put("velocity", value);
        return vector;
    }   
    
    public void resetEncoders()
    {
        
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
    }
    public void DriveWVelocity(double drive, double strafe, double turn, double value) // velocity(ticks/second) 
    {
        Map<String, Double> vector = calcVector(drive, strafe, turn, value)
        double tickspeed = 500.0;
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);        
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        frontLeftDrive.setVelocity( (int)((double)vector.get("frontLeft")));
        frontRightDrive.setVelocity((int)((double)vector.get("frontRight")));
        backLeftDrive.setVelocity(  (int)((double)vector.get("backLeft")));
        backRightDrive.setVelocity( (int)((double)vector.get("backRight")));
    }
    
    public void DriveDistance(double drive, double strafe, double turn, double value) // distance(milimiters)
    {
        Map<String, Double> vector = calcVector(drive, strafe, turn, vaue)
        double frontLeftDistance = (vector.get("frontLeft"));
        double frontRightDistance = (vector.get("frontRight"));
        double backLeftDistance = (vector.get("backLeft"));
        double backRightDistance = (vector.get("backRight"));

        int frontLeftTarget = (int)(frontLeftDistance * COUNTS_PER_MM); 
        int frontRightTarget = (int)(frontRightDistance * COUNTS_PER_MM);
        int backLeftTarget = (int)(backLeftDistance * COUNTS_PER_MM);
        int backRightTarget = (int)(backRightDistance * COUNTS_PER_MM);
        
        double velocity = vector.get("velocity");
        
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       
        frontLeftDrive.setTargetPosition(frontLeftTarget);
        frontRightDrive.setTargetPosition(frontRightTarget);
        backLeftDrive.setTargetPosition(backLeftTarget);
        backRightDrive.setTargetPosition(backRightTarget);


        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);        
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

         frontLeftDrive.setPower(velocity);
         frontRightDrive.setPower(velocity);
         backLeftDrive.setPower(velocity);
         backRightDrive.setPower(velocity);
        }
    public void disableEncoders()
    {
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void DriveWPower(double drive, double strafe, double turn, double value)
    {
        Map<String, Double> powers = calcPower(drive, strafe, turn, value);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        frontRightDrive.setPower(powers.get("frontRightPower") );
        frontLeftDrive.setPower(powers.get("frontLeftPower") );
        backRightDrive.setPower(powers.get("backRightPower"));
        backLeftDrive.setPower(powers.get("backLeftPower"));
        
    }

    @Override
    public void Drive(double drive, double strafe, double turn, double value)
    {
        switch(drivewith)
        {
            case POWER:
                DriveWPower(drive, strafe, turn, value);
                break;
            case VELOCITY:
                DriveWVelocity(drive, strafe, turn, value);
                break;
            case DISTANCE:
                DriveDistance(drive, strafe, turn, value);
                break;
            case ODOMETRY:
                telemetry.addData("Say", "No, I refuse.");
                break;
        }
            
    }    
    @Override
    public void setMode(DRIVE_MODE drivemode){
        this.drivemode = drivemode;
        drivewith = drivemode;
        stopwith = drivemode;
    }


    public Map<String, Integer>getEncoderValues()
    {
        Map<String, Integer> vector = new HashMap<>();
        vector.put("frontRight", (frontRightDrive.getCurrentPosition()));
        vector.put("frontLeft", (frontLeftDrive.getCurrentPosition()));
        vector.put("backRight", (backRightDrive.getCurrentPosition()));
        vector.put("backLeft", (backLeftDrive.getCurrentPosition()));
        return vector;
    }
    
    public boolean isDriving()
    {
        return ((frontRightDrive.isBusy() ? 1 : 0) + (frontLeftDrive.isBusy() ? 1 : 0) + (backLeftDrive.isBusy() ? 1 : 0) + (backRightDrive.isBusy() ? 1 : 0) > 2);
        
    }
    
    public void DriveSTOP()
    {
        DriveWPower(calcPower(0.0, 0.0, 0.0, 0.0));
    }

 }
