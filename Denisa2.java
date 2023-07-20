
package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.MecanumDenisa;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Denisa", group="Linear Opmode")
public class Denisa2 extends LinearOpMode {
    
    
    @Override
    
    public void runOpMode() {
        
        waitForStart();
        
        MecanumDenisa drivetrain = new MecanumDenisa(hardwareMap);
        boolean prevbutton = false; 
        boolean toggled = false; 
        double grip,arm;
        double elevation=0.0;
        
        while (opModeIsActive()) {
        
        boolean button = gamepad1.a; 
        
            if (button && !prevbutton) 
            {
                toggled = !toggled; 
                if (toggled) 
                {
                    telemetry.addData("Say", "Da"); 
                    telemetry.update();
                }
                else 
                {
                    telemetry.addData("Say", "Nu");
                    telemetry.update();
                }
            }
            prevbutton = button; 
            
            double d=gamepad1.left_stick_y; 
            double s=gamepad1.left_stick_x; 
            double r=gamepad1.right_stick_x; 
            
            drivetrain.Drive (d, s, r); 
            
            
            
            
            if(gamepad1.dpad_up)
            {
                elevation +=0.005;
            }
            else if(gamepad1.dpad_down)
            {
                elevation -=0.005;
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
