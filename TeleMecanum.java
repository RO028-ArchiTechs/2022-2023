package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.HashMap;
import java.util.Map;

@TeleOp(name="goBILDA Drivetrain Test", group="Iterative Opmode")
public class TeleMecanum extends OpMode
{
	
	// Declare OpMode members.
	private ElapsedTime runtime = new ElapsedTime();
	private DcMotor frontLeftDrive;
	private DcMotor frontRightDrive;
	private DcMotor backLeftDrive;
	private DcMotor backRightDrive;
	private HardwareMap hwMap;
	private double DRIVE_MULTIPLIER = 1.0;
	private double STRAFE_MULTIPLIER = 0.7;  //ca sa nu mai rastoarne hans robotu
	private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu

	private double COUNTS_PER_MOTOR_REV = 1120.0;
	private double DRIVE_GEAR_REDUCTION = 1.0;
	private double WHEEL_CIRCUMFERENCE_MM = 90.0 * Math.PI;
	private double defSpeed = 0.3;
	HardwareDrivetrainMecanum localDrivetrain = new HardwareDrivetrainMecanum();
	/*
	 * Code to run ONCE when the driver hits INIT
	 */
	public TeleMecanum()
	{
		telemetry.addData("Status", "Initialized");
		/* 
		 * Create local HardwareDrivetrainMecanum object. This way 
		 * we don't have to define so many variables in the constructor.
		 */
		// Tell the driver that initialization is complete.
		telemetry.addData("Status", "Initialized");
		telemetry.addData("Say", "To infinity and beyond!");
	}
	public void init()
	{
		localDrivetrain = new HardwareDrivetrainMecanum(hwMap, COUNTS_PER_MOTOR_REV, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM);
	}

	/*
	 * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
	 */
	@Override
	public void init_loop() {
	}

	/*
	 * Code to run ONCE when the driver hits PLAY
	 */
	@Override
	public void start() {
		runtime.reset();
	}

	/*
	 * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
	 */
	@Override
	public void loop() {
		// Setup a variable for each drive wheel to save power level for telemetry
		double drive = -gamepad1.left_stick_y;
		double strafe = gamepad1.left_stick_x;
		double turn = gamepad1.right_stick_x;
		double boost = gamepad1.right_trigger; 
		double speed = defSpeed + (1 - defSpeed) * boost;
		Map<String, Double> powers = new HashMap<>();
		powers.putAll(localDrivetrain.calcPower(drive, strafe, turn, speed));
		frontLeftDrive.setPower(powers.get("frontLeftPower"));
		frontRightDrive.setPower(powers.get("frontRightPower"));
		backLeftDrive.setPower(powers.get("backLeftPower"));
		backRightDrive.setPower(powers.get("backRightPower"));
		// Show the elapsed game time and wheel power.
		telemetry.addData("Status", "Run Time: " + runtime.toString());
		telemetry.addData("Motors", "front left (%.2f), front right (%.2f), back left (%.2f), back right(%.2f)",
		powers.get("frontLeftPower"), powers.get("frontRightPower"), powers.get("backLeftPower"), powers.get("backRightPower"));
	}

	/*
	 * Code to run ONCE after the driver hits STOP
	 */
	@Override
	public void stop() {
	}

}
