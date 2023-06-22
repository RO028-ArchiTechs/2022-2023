package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.State;
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
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@Autonomous(name="AutoBasic", group="Iterative Opmode")
public class AutoBasic extends LinearOpMode{
    
    private HardwareRobot robot;
    private double defSpeed = 0.2;
    
    public AutoBasic()
    {
        //empty Constructor (inside an OpMode and is thus _acceptable_)
    }
    
    @Override
    public void runOpMode()
    {
        robot = new HardwareRobot(hardwareMap);
        robot.intakeGrip(1.0);
        sleep(1000);
        robot.armSetPosition(robot.ARM_LOWER_POSITION);
        
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Say", "To infinity and beyond!");
        robot.armSetPosition(robot.ARM_UPPER_POSITION);
        
        
        waitForStart();
        robot.armSetPosition(robot.ARM_LOWER_POSITION);
        sleep(1000);
//        robot.intakeGrip(0.0);
        sleep(1000);
//        robot.armSetPosition(0.06);
        robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(-1.0, 0.0, 0.0, defSpeed));
//        robot.drivetrain.DriveDistance(robot.drivetrain.calcVector(-300, 0.0, 0.0, defSpeed));
        sleep(2300);
    }
}