package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class HardwareRobot
{
    // members
    public HardwareDrivetrainMecanum drivetrain;
    public HardwareSlider slider;
    public HardwarePivot pivot;
    public HardwareWrist wrist;
    public HardwareGripper gripper;
    private HardwareMap hwMap;
    
    // hardware constants
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double DRIVE_GEAR_REDUCTION = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    private double COUNTS_PER_MOTOR_REV_Neverest40 = 1120.0;
    private double COUNTS_PER_MOTOR_TNADO = 1440.0;
    private double WINCH_RADIUS = 56.0;
    private double ARM_REDUCTION = 10.0;
    private double GRIP_POSITION = 0.3;
    private double RELEASE_POSITION = 0.18;
    private double ARM_SPEED_UP = 0.1;
    private double ARM_SPEED_DOWN = 0.05;
    private double ARM_LOWER_POSITION = 0.0;
    private double ARM_UPPER_POSITION = 146.0;
    private double WRIST_UPPER_POSITION = 0.316;
    private double WRIST_LOWER_POSITION = 0.85;
    
    // C O N S T R U C T O R
    HardwareRobot(HardwareMap hwMap)
    {
        this.hwMap = hwMap;
        drivetrain = new HardwareDrivetrainMecanum(hwMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda);
        slider = new HardwareSlider(hwMap, COUNTS_PER_MOTOR_REV_Neverest40, WINCH_RADIUS);
        gripper = new HardwareGripper(hwMap, GRIP_POSITION, RELEASE_POSITION);
        wrist = new HardwareWrist(hwMap, WRIST_LOWER_POSITION, WRIST_UPPER_POSITION);
        pivot = new HardwarePivot(hwMap, COUNTS_PER_MOTOR_TNADO, ARM_REDUCTION);
        pivot.setLimits(ARM_LOWER_POSITION, ARM_UPPER_POSITION);
    }
}