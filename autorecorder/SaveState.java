package org.firstinspires.ftc.teamcode.autorecorder;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

//has a field for each possible input(so far)
//it is used for replicating driver behavior
//it also has a working directory and a file name stored, so we can assure that the replicator reads and writes to the same file via inheritance
public class SaveState {
    public double left_trigger;//gamepad left trigger variable
    public boolean left_bumper;//gamepad left bumper variable
    public boolean right_bumper;//gamepad right bumper variable
    public double right_trigger;//gamepad right trigger variable
    public boolean dpad_right;//gamepad dpad right variable
    public boolean dpad_left;//gamepad dpad left variable
    double left_stick_y;//gamepad left stick y variable
    double left_stick_x;//gamepad left stick x variable
    double right_stick_y;//gamepad right stick y variable
    double right_stick_x;//gamepad right stick x variable
    double direction;//compass sensor direction variable
    double red;//color sensor red variable
    double green;//color sensor green variable
    double blue;//color sensor blue variable
    boolean a;//gamepad a button variable
    boolean b;//gamepad b button variable
    boolean x;//gamepad x button variable
    boolean y;//gamepad y button variable
    boolean dpad_up;//gamepad dpad up variable
    boolean dpad_down;//gamepad dpad down variable
    double distance;//distance sensor distance variable
    double time;//the time at which this savestate was created

    protected final static String dir = Environment.getExternalStorageDirectory().getPath() + "/" + "FIRST_";//save state working directory
    protected final static String file = "save1";//save state name

    SaveState(){
        //intializing variables with neutral values
        left_stick_y = -1;
        left_stick_x = -1;
        right_stick_y = -1;
        right_stick_x = -1;
        direction = -1;
        red = -1;
        green = -1;
        blue = -1;
        a = false;
        b = false;
        x = false;
        y = false;
        dpad_up = false;
        dpad_down = false;
        distance = -1;
        time = -1;
        left_bumper = false;
        left_trigger = 0;
        dpad_left = false;
        dpad_right = false;
        right_bumper = false;
        right_trigger = 0;
    }

}