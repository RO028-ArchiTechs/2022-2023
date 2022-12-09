package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.*;


public class HardwareGripper 
{
    public Servo GripLeft;
    public Servo GripRight;
    public double GRIP_POSITION;
    public double RELEASE_POSITION;
    
    Map<String, Double> positions = new HashMap<>(); //this still looks extremely ugly, WTF
    HardwareMap hwMap;

    /*  CONSTRUCTOR  */
    public HardwareGripper(HardwareMap hwMap, double GRIP_POSITION, double RELEASE_POSITION)
    {
        this.GRIP_POSITION = GRIP_POSITION;
        this.RELEASE_POSITION = RELEASE_POSITION;
        positions.put("LEFT_GRIP", GRIP_POSITION);
        positions.put("LEFT_RELEASE", RELEASE_POSITION);
        positions.put("RIGHT_GRIP", GRIP_POSITION);
        positions.put("RIGHT_RELEASE", RELEASE_POSITION);
        this.hwMap = hwMap;
        GripLeft = hwMap.get(Servo.class, "GL");
        GripRight = hwMap.get(Servo.class, "GR");
        GripLeft.setDirection(Servo.Direction.REVERSE);
        GripRight.setDirection(Servo.Direction.FORWARD);
    }
    
    public void grip(double gripping)
    {
        GripLeft.setPosition((1.0-gripping)*positions.get("LEFT_RELEASE") + gripping*positions.get("LEFT_GRIP"));
        GripRight.setPosition((1.0-gripping)*positions.get("RIGHT_RELEASE") + gripping*positions.get("RIGHT_GRIP"));
    }
}
