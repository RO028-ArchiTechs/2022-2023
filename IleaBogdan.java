package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.MecanumIleaBogdan;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="IleaBogdan", group="Iterative Opmode")

public class IleaBogdan extends LinearOpMode {
    
    MecanumIleaBogdan drivetrain = new MecanumIleaBogdan(hardwareMap);
    @Override
    public void runOpMode() {
    
        waitForStart();
        double d, s, r;
        double grip, elevation=0;
        while(opModeIsActive()){
            
            d=gamepad1.left_stick_y;
            s=gamepad1.left_stick_x;
            r=gamepad1.right_stick_x;
            
            drivetrain.FunctiaMagnificaALuiIleaSiSaNuCopiati(2*d, 2*r, 2*s);
            
            if (gamepad1.dpad_up){
                elevation+=0.005;
            }
            else {
                if (gamepad1.dpad_down){
                    elevation-=0.005;
                }
            }
            drivetrain.arm.setPosition(elevation);
            if (gamepad1.x) {
                drivetrain.gripper.setPosition(0.0);
            }
            else {
                if (gamepad1.y) {
                    drivetrain.gripper.setPosition(0.8);
                }
            }
            
        }
        
    }
}
