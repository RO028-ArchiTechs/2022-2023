package org.firstinspires.ftc.teamcode.instructions;
import org.firstinspires.ftc.teamcode.Action;
import org.firstinspires.ftc.teamcode.HardwareSlider;

public class SliderAction implements Action {
    
    Status status = Status.IDLE;
    private HardwareSlider localSlider;
    private double sliding;
    
    
    public SliderAction(double sliding, HardwareSlider localSlider)
    {
        this.sliding = sliding;
        this.localSlider = localSlider;
    }
    
    @Override
    public Status execute(){
        switch(status){
            case IDLE:
                localSlider.slide(sliding);
                status = Status.RUNNING;
                break;
            case RUNNING:
                if(!localSlider.isSliding()){
                    status = Status.DONE;
                }
                break;
            case DONE:
                break;
                
        }
        return status;
    }
}