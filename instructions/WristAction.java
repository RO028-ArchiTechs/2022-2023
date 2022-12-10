package org.firstinspires.ftc.teamcode.instructions;
import org.firstinspires.ftc.teamcode.Action;
import org.firstinspires.ftc.teamcode.HardwareWrist;


public class WristAction implements Action {
    Status status = Status.IDLE;
    private HardwareWrist localWrist;
    private double angle;
    
    
    WristAction(double angle, HardwareWrist localWrist)
    {
        this.angle = angle;
        this.localWrist = localWrist;
    }
    
    @Override
    public Status execute(){
        switch(status){
            case IDLE:
                localWrist.setPosition(angle);
                status = Status.DONE;
                break;
            case DONE:
                break;
                
        }
        return status;
    }
}