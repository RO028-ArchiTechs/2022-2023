package org.firstinspires.ftc.teamcode.instructions;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Action;
import org.firstinspires.ftc.teamcode.HardwarePivot;


public class PivotAction implements Action{
    Status status = Status.IDLE;
    private HardwarePivot localPivot;
    double t;
    
    PivotAction(double t, HardwarePivot localPivot){
        this.localPivot = localPivot;
        this.t = t;
    }
    @Override
    public Status execute(){
        switch(status){
            case IDLE:
                localPivot.pivot(t);
                break;
            case RUNNING:
                if(!localPivot.isPivoting()){
                    status = Status.DONE;
                }
                break;
            case DONE:
                break;
        }
        return status;
    }
    
}