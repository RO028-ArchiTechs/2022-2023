
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Nectarie", group="Linear Opmode")
public class Nectarie extends LinearOpMode {
    
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight; 
    DcMotor motorBackLeft; 
    DcMotor motorBackRight; 


    public void Drive(double d, double s, double r)
    {
        motorFrontLeft.setPower(d - r - s); 
        motorFrontRight.setPower(d + r + s); 
        motorBackLeft.setPower(d - r + s); 
        motorBackRight.setPower(d + r - s); 
    }
    
    @Override
    
    public void runOpMode() {
        
        waitForStart();
        
        motorFrontLeft = hardwareMap.get(DcMotor.class, "FL"); 
        motorFrontRight = hardwareMap.get(DcMotor.class, "FR");
        motorBackLeft = hardwareMap.get(DcMotor.class, "BL");
        motorBackRight = hardwareMap.get(DcMotor.class, "BR");
        
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE); 
        motorBackRight.setDirection(DcMotor.Direction.REVERSE); 
        
        boolean prevbutton = false; 
        boolean toggled = false; 
        
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
            
            
            // motorFrontLeft.setPower(gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x); 
            // motorFrontRight.setPower(gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x); 
            // motorBackLeft.setPower(gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x); 
            // motorBackRight.setPower(gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x); 
            //asta e deja in functie
            
            
            // motorFrontLeft.setPower(-gamepad1.right_stick_x); 
            // motorFrontRight.setPower(gamepad1.right_stick_x); 
            // motorBackLeft.setPower(-gamepad1.right_stick_x); 
            // motorBackRight.setPower(gamepad1.right_stick_x); 
            
            //# face urat daca las astea 
            
            // motorFrontLeft.setPower(-gamepad1.left_stick_x); 
            // motorFrontRight.setPower(gamepad1.left_stick_x); 
            // motorBackLeft.setPower(gamepad1.left_stick_x); 
            // motorBackRight.setPower(-gamepad1.left_stick_x); 
            
            double d=gamepad1.left_stick_y; 
            double s=gamepad1.left_stick_x; 
            double r=gamepad1.right_stick_x; 
            
            Drive (d, s, r); 
            
        }
    }
}
