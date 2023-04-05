package org.firstinspires.ftc.teamcode.instructions;


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
import org.firstinspires.ftc.teamcode.lib.Action;
import org.firstinspires.ftc.teamcode.lib.HardwareDrivetrainMecanum;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class DriveAction implements Action {
    Status status = Status.IDLE;
    private double drive;
    private double strafe;
    private double turn;
    private double power;
    private HardwareDrivetrainMecanum localDrivetrain;
    
    public DriveAction(double drive, double strafe, double turn, double power, HardwareDrivetrainMecanum localDrivetrain){
//  ^^^ YES, apparently we DO need to make the constructor public if we store this class in a different folder.
        this.drive = drive;
        this.strafe = strafe;
        this.turn = turn;
        this.power = power;
        this.localDrivetrain = localDrivetrain;
    }
    
    @Override
    public Status execute(){
        switch(status){
            case IDLE:
                localDrivetrain.DriveDistance(localDrivetrain.calcVector(drive, strafe, turn, power));
                status = Status.RUNNING;
                break;
            case RUNNING:
                if(!localDrivetrain.isDriving()){
                    status = Status.DONE;
                }
                break;
            case DONE:
                break;
                
        }
        return status;
    }
}
