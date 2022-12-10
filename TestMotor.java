package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@TeleOp(name="TestMotor", group="Linear Opmode")
public class TestMotor extends LinearOpMode
{
   
    private DcMotor Motor;
    private double MAX_POWER = 0.2;

    @Override
    public void runOpMode()
    {
        Motor = hardwareMap.get(DcMotor.class, "PV");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
            
        while (opModeIsActive())
        {
            if (gamepad1.x){
                Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                
            }
            Motor.setPower(-gamepad1.right_stick_y*MAX_POWER);
            telemetry.addData("Motor", "%d", Motor.getCurrentPosition());
            telemetry.update();
        }
    }
}