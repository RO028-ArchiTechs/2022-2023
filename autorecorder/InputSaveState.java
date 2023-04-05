package org.firstinspires.ftc.teamcode.autorecorder;
import org.firstinspires.ftc.teamcode.autorecorder.SaveState;
import org.firstinspires.ftc.teamcode.autorecorder.EncodeController;

import java.io.IOException;

//it inherits SaveState to make sure that the directory in which we save is the same for the decoder
//it is the SaveState variation designed for the decoding part of the replicator
public class InputSaveState extends SaveState{

    In in;//input file parser

    public InputSaveState() {//constructor is the same as the base class
        super();
    }

    public void init_in() {//initializes the input file parser
        in = new In(dir,file);
    }

    public boolean is_invalid() {//checks if the current savestate is invalid
        return time == -1;//this check works because if we reached EOF, than the neutral value returned is -1, so we just check if the time is -1
    }

    public void read() {

        //reads the values in the same order that and OutputSaveState object would write them
        in.update();
        left_stick_y = in.getDouble();
        left_stick_x = in.getDouble();
        right_stick_y = in.getDouble();
        right_stick_x = in.getDouble();
        direction = in.getDouble();
        red = in.getDouble();
        green = in.getDouble();
        blue = in.getDouble();
        a = in.getBoolean();
        b = in.getBoolean();
        x = in.getBoolean();
        y = in.getBoolean();
        dpad_up = in.getBoolean();
        dpad_down = in.getBoolean();
        distance = in.getDouble();
        time = in.getDouble();
        left_bumper = in.getBoolean();
        left_trigger = in.getDouble();
        dpad_left = in.getBoolean();
        dpad_right = in.getBoolean();
        right_bumper = in.getBoolean();
        right_trigger = in.getDouble();
    }
}