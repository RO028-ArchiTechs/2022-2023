package org.firstinspires.ftc.teamcode.instructions;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Action;


public class ArmMovementAction implements Action {
    double slide;
    double pivot;
    double grip;
    double wrist;
    // ArmMovementAction(double grip_position, double t, double sliding, double angle, HardwareGripper localGripper, HardwarePivot localPivot, HardwareSlider localSlider, HardwareWrist localWrist){
    //     this.slide = slide;
    //     this.pivot = pivot;
    //     this.grip = grip;
    //     this.wrist = wrist;
    // }
    
    @Override
    public Status execute()
    {
        return Status.IDLE;
        
    }
}