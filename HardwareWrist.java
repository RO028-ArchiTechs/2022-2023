package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;


public class HardwareWrist {
    public Servo wrist;
    private double WRIST_LOWER_POSITION = 0.950;
    private double WRIST_UPPER_POSITION = 0.250;
    private HardwareMap hwMap;
    
    public HardwareWrist(HardwareMap hwMap, double WRIST_LOWER_POSITION, double WRIST_UPPER_POSITION){
        this.WRIST_LOWER_POSITION = WRIST_LOWER_POSITION;
        this.WRIST_UPPER_POSITION = WRIST_UPPER_POSITION;
        this.hwMap = hwMap;
        wrist = hwMap.get(Servo.class, "WST");
    }
    public void setPosition(double t){
        wrist.setPosition((1.0 - t)*WRIST_LOWER_POSITION + t*WRIST_UPPER_POSITION);
    }
}
