package org.firstinspires.ftc.teamcode.instructions;
import org.firstinspires.ftc.teamcode.lib.Action;
import org.firstinspires.ftc.teamcode.lib.HardwareGripper;

public class GripperAction implements Action {
    
    Status status = Status.IDLE;
    private double grip_position;
    private HardwareGripper localGripper;
    
    public GripperAction (double grip_position, HardwareGripper localGripper) {
        this.grip_position = grip_position;
        this.localGripper = localGripper;
    }
    
    @Override
    public Status execute(){
        switch(status){
            case IDLE:
                localGripper.grip(grip_position);
                status = Status.DONE;
                break;
            case DONE:
                break;
        }
        return status;
    }
}

