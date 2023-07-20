
package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.MecanumTudor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="VaDiM TuDoR", group="Linear Opmode")
public class Tudor extends LinearOpMode {

    MecanumTudor drivetrain;
    
    @Override
    public void runOpMode() {
        
        drivetrain = new MecanumTudor(hardwareMap);
        boolean prevbtn = false;
        boolean toggled = true;
        double d, s, r;
        double elevation = 0.0;
        waitForStart();

        while(opModeIsActive()){
            
            d = gamepad1.left_stick_y; 
            s = gamepad1.left_stick_x; 
            r = gamepad1.right_stick_x; 
            
            if(!gamepad1.b){
                drivetrain.Drive( 0.25*d, 0.25*s, 0.2*r);
            } else {
                drivetrain.Drive( d, s, r);
            }
            if(gamepad1.dpad_up)
            {
                elevation += 0.005;
            }else if(gamepad1.dpad_down)
            {
                elevation -= 0.005;
            }
            
            drivetrain.arm.setPosition(elevation);
            
            if(gamepad1.x){
                drivetrain.gripper.setPosition(0.0);
            }
            else if(gamepad1.y){
                drivetrain.gripper.setPosition(0.8);
            }
        }
    }
}
