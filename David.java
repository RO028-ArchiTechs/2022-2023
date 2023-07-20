
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="David", group="Linear Opmode")
public class David extends LinearOpMode {
    DcMotor motor;
  
  

    @Override
    public void runOpMode() {
        

    waitForStart();
    motor= hardwareMap.get(DcMotor.class, "FL");
    
    
    while(opModeIsActive()){  
   
    
         boolean button = gamepad1.a;
    
            if(button){
               motor.setPower(1.0);
            }else{
                motor.setPower(0.0);
            }
            
            telemetry.addData("Say", button);
            telemetry.update();
        }
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

    }
}
