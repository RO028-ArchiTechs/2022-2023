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

@TeleOp(name="Test2Motor", group="Linear Opmode")
public class Test2Motor extends LinearOpMode
{
   
    private DcMotor Motor1;
    private DcMotor Motor2;
    private double MAX_POWER = 0.4;

    @Override
    public void runOpMode()
    {
        Motor1 = hardwareMap.get(DcMotor.class, "M1");
        Motor1.setDirection(DcMotor.Direction.FORWARD);
        Motor2 = hardwareMap.get(DcMotor.class, "M2");
        Motor2.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
            
        while (opModeIsActive())
        {
            if (gamepad1.x){
                Motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                Motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                Motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                
            }
            Motor1.setPower(-gamepad1.right_stick_y*MAX_POWER);
            Motor2.setPower(-gamepad1.right_stick_y*MAX_POWER);
            telemetry.addData("Motor", "%d", Motor1.getCurrentPosition());
            telemetry.update();
        }
    }
}
