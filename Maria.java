
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Maria", group="Linear Opmode")
public class Maria extends LinearOpMode {
    

    DcMotor motor;
    

    @Override
    public void runOpMode() {
        
        waitForStart();
        motor = hardwareMap.get(DcMotor.class, "FL");

        while (opModeIsActive()){
            

            boolean button = gamepad1.a;
            if( button)
            {
                motor.setPower(1.0);
            }
            else
            {
                motor.setPower(0.0);
            }
            telemetry.update();
        }
    
        
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        
    }
}
