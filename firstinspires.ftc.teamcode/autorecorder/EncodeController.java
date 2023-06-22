package org.firstinspires.ftc.teamcode.autorecorder;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autorecorder.GeneralController;
import org.firstinspires.ftc.teamcode.autorecorder.InputSaveState;
import org.firstinspires.ftc.teamcode.autorecorder.OutputSaveState;

public class EncodeController extends GeneralController {

    OutputSaveState last;//the last OutputSaveState soon to be written

//    public EncodeController(LinearOpMode opmode) {
    public EncodeController(OpMode opmode) {
        super(opmode);//we call the super of this type of controller
        last = new OutputSaveState(this);//initializing the OutputSaveState
        last.init_out();//initializing the output writer of the save state
    }

    public void write_state() {//writes the current save state(if needed)
        OutputSaveState tmp = new OutputSaveState(this);//getting current OutputSaveState
        if(last.is_same(tmp) == false){//if they are not the same(disregarding time)
            last.write();//write the previous SaveState
            last.change(tmp);//update the current one
        }
        else {
            last.update(tmp);//update only the time of the current savestate
        }
        tmp = null;//destroying the local OutputSaveState via Garbage collector
    }

    public void write_final_state() {//writes the final save state before ending the encoding session
        last.write_final();
        last = null;
    }
}