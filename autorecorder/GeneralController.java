package org.firstinspires.ftc.teamcode.autorecorder;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;


//Base class of various types of controll bindings
//this class's purpose is to be a "man in the middle", and be the class that provides all the inputs
//this is usefull for the decoder,which can just pretend to be getting inputs and in fact reading them from a SaveState
public class GeneralController {
    private ElapsedTime runtime = new ElapsedTime();//this is need for logging the time of save states

    private OpMode cur_opmode; // refference to the opmode that calls this controller, needed for gamepad inputs
    //variety of sensors
    private ColorSensor sColor = null;
    private DistanceSensor sDist = null;
    private ModernRoboticsI2cCompassSensor sCompass = null;

    public GeneralController(OpMode opmode){
        ///setup Hardware
        cur_opmode = opmode; //current opmode
        ///sColor = cur_opmode.hardwareMap.get(ColorSensor.class, "CLR");
        ///sDist = cur_opmode.hardwareMap.get(DistanceSensor.class, "CLR");
        ///sCompass = cur_opmode.hardwareMap.get(ModernRoboticsI2cCompassSensor.class, "HDG");
    }

    //methods for getting gamepad input
    public double get_left_stick_y() {
        return cur_opmode.gamepad1.left_stick_y;
    }

    public double get_left_stick_x() {
        return cur_opmode.gamepad1.left_stick_x;
    }

    public double get_right_stick_y() {
        return cur_opmode.gamepad1.right_stick_y;
    }

    public double get_right_stick_x() {
        return cur_opmode.gamepad1.right_stick_x;
    }

    public boolean get_x() {
        return cur_opmode.gamepad1.x;
    }

    public boolean get_y() {
        return cur_opmode.gamepad1.y;
    }

    public boolean get_a() {
        return cur_opmode.gamepad1.a;
    }

    public boolean get_b() {
        return cur_opmode.gamepad1.b;
    }

    public void reset_runtime() {
        runtime.reset();
    }

    public boolean get_dpad_up() {
        return cur_opmode.gamepad1.dpad_up;
    }

    public boolean get_dpad_down() {
        return cur_opmode.gamepad1.dpad_down;
    }
    public double get_left_trigger() {
        return cur_opmode.gamepad1.left_trigger;
    }

    public boolean get_left_bumper() {
        return cur_opmode.gamepad1.left_bumper;
    }


    public boolean get_dpad_left() {
        return cur_opmode.gamepad1.dpad_left;
    }

    public boolean get_dpad_right() {
        return cur_opmode.gamepad1.dpad_right;
    }


    public double get_right_trigger() {
        return cur_opmode.gamepad1.right_trigger;
    }

    public boolean get_right_bumper() {
        return cur_opmode.gamepad1.right_bumper;
    }


    public double getDistance(DistanceUnit dist_unit) {//returns distance sensor distance, but because we don't use one, we return a neutral value(-1)
///        return sDist.getDistance(dist_unit);
        return -1;
    }

    public double getDirection() {//returns compass sensor direction, but because we don't use one, we return a neutral value(-1)
        ///return (double)sCompass.getDirection();
        return -1;

    }

    public double red() {//returns color sensor red value, but because we don't use one, we return a neutral value(-1)
        //    return sColor.red();
        return -1;
    }

    public double green() {//returns color sensor green value, but because we don't use one, we return a neutral value(-1)
        //   return sColor.green();
        return -1;
    }

    public double blue() {//returns color sensor blue value, but because we don't use one, we return a neutral value(-1)
        //   return sColor.blue();
        return -1;
    }

    public ElapsedTime get_runtime() {//gets current runtime
        return runtime;
    }
}