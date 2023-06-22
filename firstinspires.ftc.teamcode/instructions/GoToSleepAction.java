package org.firstinspires.ftc.teamcode.instructions;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.teamcode.lib.Action;
import org.firstinspires.ftc.teamcode.lib.HardwareDrivetrainMecanum;
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
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class GoToSleepAction implements Action{
    Status status = Status.IDLE;
    private ElapsedTime runtime;
    double initialTime;
    double delay;
    public GoToSleepAction(double delay)
    {
        this.delay = delay;
    }  
    @Override
    public Status execute(){
        switch (status){
            case IDLE:
                initialTime = runtime.seconds();
                status = Status.RUNNING;
                break;
            case RUNNING:
                if(runtime.seconds() - delay > initialTime){
                    status = Status.DONE;
                }
                break;
            case DONE:
                break;
        }   
        return status;
    }
}