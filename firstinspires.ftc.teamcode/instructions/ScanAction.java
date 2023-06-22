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

public class ScanAction implements Action{
    Status status = Status.IDLE;
    private ElapsedTime runtime;
    double initialTime = runtime.seconds();
    double delay;
    enum State{ CYN, MAG, YLW};
    State localState;
    private ColorSensor color;
    ScanAction(ElapsedTime runtime, ColorSensor color, State localState)
    {
        this.localState = localState;
        this.color = color;
        this.runtime = runtime;
    }   
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
    public Status execute(){
        switch (status){
            case IDLE:
                localState = scan();
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
