package org.firstinspires.ftc.teamcode.autorecorder;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.autorecorder.GeneralController;
import org.firstinspires.ftc.teamcode.autorecorder.OutputSaveState;

public class DecodeController extends GeneralController {
    InputSaveState last;//the current input save state

    public DecodeController(OpMode opmode){
        super(opmode);//we call the super of this type of encoder
        last = new InputSaveState();//we initialize the input save state
        last.init_in();//we intialize the input parser
    }

    public void read_state() {//we read the next InputSaveState(if necessary)
        if(this.get_runtime().milliseconds() > last.time){//if current save state is outdated
            last.read();//we read the next InputSaveState
        }
    }

    public boolean is_invalid() {//checks if the InputSaveState is invalid(i.e. EOF was reached)
        return last.is_invalid();
    }

    @Override

    //overrides all methods of the base class to feed the drivetrain with logged savestates, rather than actual inputs

    public double get_left_stick_y() {
        return last.left_stick_y;
    }

    public double get_left_stick_x() {
        return last.left_stick_x;
    }

    public double get_right_stick_y() {
        return last.right_stick_y;
    }

    public double get_right_stick_x() {
        return last.right_stick_x;
    }

    public double getDirection() {
        return last.direction;
    }

    public double red() {
        return last.red;
    }

    public double green() {
        return last.green;
    }

    public double blue() {
        return last.blue;
    }

    public boolean get_x() {
        return last.x;
    }

    public boolean get_y() {
        return last.y;
    }

    public boolean get_a() {
        return last.a;
    }

    public boolean get_b() {
        return last.b;
    }

    public boolean get_dpad_up() {
        return last.dpad_up;
    }

    public boolean get_dpad_down() {
        return last.dpad_down;
    }

    public double get_left_trigger() {
        return last.left_trigger;
    }

    public boolean get_left_bumper() {
        return last.left_bumper;
    }

    public boolean get_dpad_left() {
        return last.dpad_left;
    }

    public boolean get_dpad_right() {
        return last.dpad_right;
    }


    public double get_right_trigger() {
        return last.right_trigger;
    }

    public boolean get_right_bumper() {
        return last.right_bumper;
    }

    ///save states save their distance in cm
    ///change this if you need another disance unit
    public double getDistance() {
        return last.distance;
    }
}