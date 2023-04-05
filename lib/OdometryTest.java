package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@TeleOp(name="OdometryTest", group="Iterative Opmode")
public class OdometryTest extends OpMode
{
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
   
    // Hardware declarations
    private HardwareDrivetrainMecanum localDrivetrain;
    
    private DcMotor LeftOdo;
    private DcMotor RightOdo;
    private DcMotor BackOdo;
    private OdometricCalculator odcalc;
    

    // hardware constants
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double COUNTS_PER_ENC_REV = 8192.0;
    private double DRIVE_GEAR_REDUCTION = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.39;
    private double minSpeed = 0.15;
    
    //state variables 
        int oldRcount;
        int oldLcount;
        int oldBcount;
     
    
    public OdometryTest()
    {
        //empty Constructor (inside an OpMode and is thus _acceptable_)
    }
    
    // so that we may elegantly have state change detection 
    Gamepad prevgamepad;
    
    public void copyGamepad( Gamepad source, Gamepad target )
    {
        try{
        target.copy(source);
        }
        catch(RobotCoreException gamepadfutut)
        {
            telemetry.addLine("C3V4 S3 FU7U la gamepad");
            telemetry.update();
        }
        
    }
    
    
    /*
     * Code to run after the driver hits INIT
     */
    @Override
    public void init()
    {
        telemetry.addData("Status", "Initialized");
        prevgamepad = new Gamepad();
        copyGamepad(gamepad1, prevgamepad);
        localDrivetrain = new HardwareDrivetrainMecanum(hardwareMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION, WHEEL_CIRCUMFERENCE_MM_goBilda);
        
        odcalc = new OdometricCalculator(160.0, 100.0);
        LeftOdo = hardwareMap.get(DcMotor.class, "OL");
        LeftOdo.setDirection(DcMotor.Direction.REVERSE);
        RightOdo = hardwareMap.get(DcMotor.class, "OR");
        RightOdo.setDirection(DcMotor.Direction.FORWARD);
        BackOdo = hardwareMap.get(DcMotor.class, "OB");
        BackOdo.setDirection(DcMotor.Direction.FORWARD);
        
        LeftOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BackOdo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        oldRcount = 0;
        oldLcount = 0;
        oldBcount = 0;
        
        
        telemetry.addData("Say", "To infinity and beyond!");
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
    
    @Override
    public void loop()
    {
        // Setup a variable for each drive wheel to save power level for telemetry
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
        double boost = gamepad1.right_trigger;
        double shift = gamepad1.left_trigger;  
        double speed = (1 - boost - shift)*defSpeed + boost + minSpeed*shift;
           
        if (gamepad1.x)
        {
            odcalc.resetCoords();
        }
        
        // saving current gamepad state so we may compare against it n the next iteration        
        copyGamepad(gamepad1, prevgamepad);        
        
        // sending calculated values to the hardware
        localDrivetrain.DriveWPower(localDrivetrain.calcPower(drive, strafe, turn, speed));
        
        Map<String, Double> coords = odcalc.getCoords();
        odcalc.updatePosition(
            (RightOdo.getCurrentPosition()- oldRcount)/(COUNTS_PER_ENC_REV/(WHEEL_CIRCUMFERENCE_MM_goBilda)/2),
            (LeftOdo.getCurrentPosition() - oldLcount)/(COUNTS_PER_ENC_REV/(WHEEL_CIRCUMFERENCE_MM_goBilda)/2),
            (BackOdo.getCurrentPosition() - oldBcount)/(COUNTS_PER_ENC_REV/(WHEEL_CIRCUMFERENCE_MM_goBilda)/2)
            );
        oldRcount = RightOdo.getCurrentPosition();
        oldLcount = LeftOdo.getCurrentPosition();
        oldBcount = BackOdo.getCurrentPosition();
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        
        telemetry.addData("ODO", "X %.2f, Y %.2f, A %.2f", coords.get("X"), coords.get("Y"), coords.get("theta"));
        telemetry.update();
    }

    @Override
    public void stop() {
    }
}
