package org.firstinspires.ftc.teamcode.autorecorder;
import android.content.Context;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.autorecorder.EncodeController;
import org.firstinspires.ftc.teamcode.autorecorder.SaveState;
import org.firstinspires.ftc.teamcode.autorecorder.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.InputStream;
import java.io.OutputStream;

///it inherits SaveState to make sure that the directory in which we save is the same for the encoder
//it is the SaveState variation designed for the encoding part of the replicator
public class OutputSaveState extends SaveState{

    Log out = null;

    public OutputSaveState(EncodeController controller) {
        //this time, the constructor need the EncodeController which provides the SaveState with all logged inputs
        //here we initialize all of them according to the EncodeController inputs
        this.left_stick_y = controller.get_left_stick_y();
        this.left_stick_x = controller.get_left_stick_x();
        this.right_stick_y = controller.get_right_stick_y();
        this.right_stick_x = controller.get_right_stick_x();
        this.direction = controller.getDirection();
        this.red = controller.red();
        this.green = controller.green();
        this.blue = controller.blue();
        this.a = controller.get_a();
        this.b = controller.get_b();
        this.x = controller.get_x();
        this.y = controller.get_y();
        this.dpad_up = controller.get_dpad_up();
        this.dpad_down = controller.get_dpad_down();
        this.distance = controller.getDistance(DistanceUnit.CM);
        this.time = controller.get_runtime().milliseconds();
        this.left_bumper = controller.get_left_bumper();
        this.left_trigger = controller.get_left_trigger();
        this.dpad_left = controller.get_dpad_left();
        this.dpad_right = controller.get_dpad_right();
        this.right_bumper = controller.get_right_bumper();
        this.right_trigger = controller.get_right_trigger();
    }

    public void change(OutputSaveState other) {//this copies another\s OutputSaveState fields to the current OutputSaveState

        this.left_stick_y = other.left_stick_y;
        this.left_stick_x = other.left_stick_x;
        this.right_stick_y = other.right_stick_y;
        this.right_stick_x = other.right_stick_x;
        this.direction = other.direction;
        this.red = other.red;
        this.green = other.green;
        this.blue = other.blue;
        this.a = other.a;
        this.b = other.b;
        this.x = other.x;
        this.y = other.y;
        this.dpad_up = other.dpad_up;
        this.dpad_down = other.dpad_down;
        this.distance = other.distance;
        this.time = other.time;
        this.left_bumper = other.left_bumper;
        this.left_trigger = other.left_trigger;
        this.dpad_left = other.dpad_left;
        this.dpad_right = other.dpad_right;
        this.right_bumper = other.right_bumper;
        this.right_trigger = other.right_trigger;
    }


    public void init_out() {//initializes the output writer
        this.out = new Log(dir,file,false);
    }

    public void write() {//writes all the logged date to a file
        out.addData(left_stick_y);
        out.addData(left_stick_x);
        out.addData(right_stick_y);
        out.addData(right_stick_x);
        out.addData(direction);
        out.addData(red);
        out.addData(green);
        out.addData(blue);
        out.addData(a);
        out.addData(b);
        out.addData(x);
        out.addData(y);
        out.addData(dpad_up);
        out.addData(dpad_down);
        out.addData(distance);
        out.addData(time);
        out.addData(left_bumper);
        out.addData(left_trigger);
        out.addData(dpad_left);
        out.addData(dpad_right);
        out.addData(right_bumper);
        out.addData(right_trigger);
        out.update();
    }

    public void write_final() {//it is called to write the final action and then close the output writer
        this.write();
        out.close();
        out = null;
    }


    public boolean is_same(OutputSaveState other) {//checks if a savestate is the same with another one(with no regard to time)
        return this.left_stick_y == other.left_stick_y &&
                this.left_stick_x == other.left_stick_x &&
                this.right_stick_y == other.right_stick_y &&
                this.right_stick_x == other.right_stick_x &&
                this.direction == other.direction &&
                this.red == other.red &&
                this.green == other.green &&
                this.blue == other.blue &&
                this.a == other.a &&
                this.b == other.b &&
                this.x == other.x &&
                this.y == other.y &&
                this.dpad_up == other.dpad_up &&
                this.dpad_down == other.dpad_down &&
                this.distance == other.distance &&
                this.left_bumper == other.left_bumper &&
                this.left_trigger == other.left_trigger &&
                this.dpad_left == other.dpad_left &&
                this.dpad_right == other.dpad_right &&
                this.right_bumper == other.right_bumper &&
                this.right_trigger == other.right_trigger;
    }

    public void update(OutputSaveState tmp) {//this is for quick changes between save states which differ only by time
        this.time = tmp.time;
    }
}