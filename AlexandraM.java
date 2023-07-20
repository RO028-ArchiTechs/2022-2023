
package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.MecanumAlexandra;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="AlexAndraM", group="Iterative Opmode")
public class AlexandraM extends LinearOpMode {
  
    MecanumAlexandra drivetrain;
    
     @Override
    public void runOpMode() {
        
        drivetrain = new MecanumAlexandra(hardwareMap);
        boolean previousButton= false;
        boolean toggled=true;
        double d,s,r;
        double grip;
        double elevation=0.0;
        waitForStart();
           
           
        while(opModeIsActive()){
            d= gamepad1.left_stick_y;
            s= gamepad1.left_stick_x;
            r= gamepad1.right_stick_x;
            drivetrain.Drive(d,s,r);
            
            if(gamepad1.dpad_up){
                elevation+=0.005;
            }else if (gamepad1.dpad_down){
                elevation-=0.005;
            }
            
            drivetrain.arm.setPosition(elevation);
            if(gamepad1.x){
                drivetrain.gripper.setPosition(0.0);
            }else if(gamepad1.y){
                drivetrain.gripper.setPosition(0.8);
            }
             
        }
    }
        // Wait for the game to start (driver presses PLAY)
}
