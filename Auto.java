package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.teamcode.instructions.DriveAction;
import org.firstinspires.ftc.teamcode.instructions.GripperAction;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@Disabled
@Autonomous(name="Auto", group="Iterative Opmode")
public class Auto extends LinearOpMode
{

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private HardwareDrivetrainMecanum localDrivetrain;
    private HardwareSlider localSlider;
    private HardwareGripper localGripper;
    private Servo wrist;
    OurMath meth;
    private ColorSensor color;
    
    
    private double COUNTS_PER_MOTOR_REV_goBilda = 1120.0;
    private double DRIVE_GEAR_REDUCTION_goBilda = 1.0;
    private double WHEEL_CIRCUMFERENCE_MM_goBilda = 100.0 * Math.PI;
    private double COUNTS_PER_MOTOR_REV_tetrix = 1120.0;
    private double WINCH_RADIUS = 56.0;
    private double GRIP_POSITION = 0.5;
    private double RELEASE_POSITION = 0.05;
    private double WRIST_LOWER_POSITION = 0.950;
    private double defSpeed = 0.3;
    private double minSpeed = 0.1;
    private int cycler = 0;
    private List<Double> positions = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    enum State{ CYN, MAG, YLW};
    State localState;
        
        
/*
    private double scan()
    {
        int R = color.red();        
        int G = color.green();        
        int B = color.blue();        
        double fR = (double)(R)/255;
        double fG = (double)(G)/255;
        double fB = (double)(B)/255;
        double Cmax = (meth.sMax(meth.sMax(fR, fG), fB));
        double Cmin = (meth.sMin(meth.sMin(fR, fG), fB));
        double delta = Range.clip(Cmax-Cmin, 0.01, 1.0);
//      double delta = 1.0;
        double H = 0.0;
        if(meth.sAbs(Cmax - fR) < 0.01)
        {
            telemetry.addData("Cmax", "R");
            H = 60.0*(((fG-fB)/delta)%6);
        }
        if(meth.sAbs(Cmax - fG) < 0.01)
        {
            telemetry.addData("Cmax", "G");
            H = 60.0*(((fB-fR)/delta)+2.0);
        }
        if(meth.sAbs(Cmax - fB) < 0.01)
        {
            telemetry.addData("Cmax", "B");
            H = 60.0*(((fR-fG)/delta)+4.0);
        }
//        return (H<120 ? State.RED : ( H>120 && H<240 ? State.GRN : State.BLU)); 
        return H;
    }
*/    

    private State scan()
    {
        int r = color.red();
        int g = color.green();
        int b = color.blue();
        State state = State.CYN;
        if (r <= b && r <= g)
        {
            state = State.CYN;                
        }     
        if (g <= r && g <= b)
        {
            state = State.MAG;                
        }     
        if (b <= g && b <= r)
        {
            state = State.YLW;                
        }     
        return state;
    }
    
    @Override
    public void runOpMode()
    {
        localDrivetrain = new HardwareDrivetrainMecanum(hardwareMap, COUNTS_PER_MOTOR_REV_goBilda, DRIVE_GEAR_REDUCTION_goBilda, WHEEL_CIRCUMFERENCE_MM_goBilda);
        localSlider = new HardwareSlider(hardwareMap, COUNTS_PER_MOTOR_REV_tetrix, WINCH_RADIUS);
        localGripper = new HardwareGripper(hardwareMap, GRIP_POSITION, RELEASE_POSITION);
        meth = new OurMath(0.001);
        wrist = hardwareMap.get(Servo.class, "WST");
        color = hardwareMap.get(ColorSensor.class, "CLR");
        wrist.setPosition(WRIST_LOWER_POSITION);
            sleep(500);
        localGripper.grip(GRIP_POSITION);
        positions.add(0.0);     //intake
        positions.add(150.0);   //coaster
        positions.add(1200.0);  //low pole
        positions.add(370.0);   //medium pole
        positions.add(1200.0);  //high pole
        actions.add(new DriveAction( 65.0, 0.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( 0.0, -160.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( -65.0, 0.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( 65.0, 160.0, 0.0, 0.0, localDrivetrain));
        actions.add(new GripperAction(GRIP_POSITION, localGripper));
        actions.add(new DriveAction( 150.0, 0.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( 0.0, 50.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( 80.0, 0.0, 0.0, 0.0, localDrivetrain));
        actions.add(new DriveAction( 65.0, 160.0, 0.0, 0.0, localDrivetrain));
        
        actions.add(new DriveAction( 400.0, 0.0, 0.0, 0.0, localDrivetrain));
        
        telemetry.addData("Status", "Initialized");
        
        
        
        waitForStart();
        runtime.reset();
        int i = 0; 
            while(opModeIsActive() && i<actions.size() )
        {
            switch(actions.get(i).execute())
            {
                case IDLE:
                    telemetry.addData("START", " %d", i);
                break;
                case RUNNING:
                    telemetry.addData("RUNNING", " %d", i);
                break;
                case DONE:
                    ++i; 
                break;
            }
            telemetry.update();
        }
    }
    
}