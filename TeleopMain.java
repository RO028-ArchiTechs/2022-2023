package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@TeleOp(name="TeleopMAIN", group="Iterative Opmode")
public class TeleopMain extends OpMode
{
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
   
    // Hardware declarations
    private HardwareRobot robot;
    
    // sensible defaults
    private double DRIVE_MULTIPLIER = 1.0;  // these 3 variables are used to prefferentially restrict movement speed on one axis or another; to alter the overall drive speed please change defSpeed.
    private double STRAFE_MULTIPLIER = 1.2;  //ca sa nu mai rastoarne hans robotu
    private double TURN_MULTIPLIER = 0.8;  //ca sa nu mai rastoarne hans robotu
    private double defSpeed = 0.39;
    private double minSpeed = 0.15;
    private double HSLIDE_SPEED = 12.0;
    
    private double MANUAL_SLIDE_SPEED = 6.0;    // mm/iter
    private double MANUAL_ARM_SPEED = 3.0;      // deg/iter
    private double MANUAL_GRIP_SPEED = 0.1;     // mm/iter
    private double MANUAL_WRIST_SPEED = 0.005;  // parameter/iter
     
    // state variables
    private int cycler = 0;
    private double armPosition = 0;
    
    enum Mode {AUTO, MANUAL}; 
    enum State {INTAKE, TRANSFER, SCORE}; 
    enum DriveMode {POWER, VELOCITY, DISTANCE, ODOMETRY};
    private Mode localMode; 
    private State localState;
    private DriveMode localDrive;
    private double sliding;     //milimeters
    private double intakesliding;     //milimeters
    private double intakegripping;    //parametric
    private double scoregripping;    //parametric
    private double wristing;    //parametric
    private double arming;    //parametric
    private boolean doDelay; 
    private boolean didDelay1;
    private boolean didDelay2;
    private double initialDelayTime;
    
    
    public TeleopMain()
    {
        //empty Constructor (inside an OpMode and is thus _acceptable_)
    }
    
    // so that we may elegantly have state change detection 
    Gamepad prevgamepad1;
    Gamepad prevgamepad2;
    
    public void copyGamepad( Gamepad source, Gamepad target )
    {
        try{
        target.copy(source);
        }
        catch(RobotCoreException gamepadfutut)
        {
            telemetry.addLine("C3V4 S3 FU7U l4 g4m3p4d");
                // We never managed to get this exception thrown
            telemetry.update();
        }
    }
    
    
    
    
    
    
    private void delayGrip(double initialTime, double t1, double t2) 
    {
        if (!didDelay2 && runtime.time() > initialTime + t1 + t2)
        {
            wristing = 1.0;
            cycler = 1;
            didDelay2 = true;
        }
        if (!didDelay1 && runtime.time() > initialTime + t1)
        {
            intakegripping = 0.0;
            didDelay1 = true;
        }
    }
    
    /*
     * Code to run after the driver hits INIT
     */
    @Override
    public void init()
    {
        robot = new HardwareRobot(hardwareMap);
        telemetry.addData("Status", "Initialized");
        prevgamepad1 = new Gamepad();
        prevgamepad2 = new Gamepad();
        copyGamepad(gamepad1, prevgamepad1);
        copyGamepad(gamepad2, prevgamepad2);
        sliding = 0.0;
        wristing = 0;
        intakegripping = 0.0;
        scoregripping = 0.0;
        armPosition = robot.ARM_LOWER_POSITION;
        robot.armSetPosition(armPosition);
//        robot.wristSetPosition(1.0);
        doDelay = true;
        initialDelayTime = runtime.time();
        
        localState = State.INTAKE;
        
        telemetry.addData("Say", "To infinity and beyond!");
        localMode = Mode.AUTO;
        robot.setMode(robot.drivetrain.DRIVE_MODE.POWER)
    }
    
    private double multiplier(boolean increase, boolean decrease)
    {
        return  ( increase ?
                    1.0
                    :
                    ( decrease ?
                        -1.0 
                        :
                        0.0 
                    ) 
                );
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }
    
    @Override
    public void loop()
    {
        //  E-E-E-EJECT!!!!!!
        if( gamepad1.a&&gamepad1.b&&gamepad1.x&&gamepad1.y || gamepad2.a&&gamepad2.b&&gamepad2.x&&gamepad2.y )
        {
            telemetry.addData("EMERGENCY STOP", "%s", "plm");
            stop(); // unfortunately this does nothing, idk why
            // THE BELOW LINE CAUSES A SEGFAULT (NPE) ON PURPOSE
            telemetry.addData("INVALID INDEX", "%.2f", robot.positions.get(-1));
            // This is done to protect the robot from destroying itself
            // in the event that an encoder cable
        }
        
        // Drivetrain input on Gp1
        double drive  = -gamepad1.left_stick_y;
        double strafe =  gamepad1.left_stick_x;
        double turn   = -gamepad1.right_stick_x;
        double boost  =  gamepad1.right_trigger;
        double shift  =  gamepad1.left_trigger;  
           
        /*
        */
        //toggles mode when driver presses the Logitech button 
        localMode = 
            (!prevgamepad1.guide && gamepad1.guide ? 
                (localMode == Mode.AUTO ?
                    Mode.MANUAL 
                    :
                    Mode.AUTO
                )
                : 
                localMode
            );
        
        // different input handling depending on current mode
        switch(localMode)
        {
            case AUTO:
            // FINITE STATE MACHINE
            switch(localState){
                case INTAKE:
                    wristing = 0;
                    cycler = 0;
                    armPosition = robot.ARM_LOWER_POSITION;
                     
                    intakegripping = 
                        (!prevgamepad1.x && gamepad1.x ? 
                            1.0 - intakegripping 
                            : 
                            intakegripping
                        );
                    
                    intakesliding = Range.clip
                        ( intakesliding +
                        HSLIDE_SPEED *
                        multiplier(gamepad1.dpad_up, gamepad1.dpad_down),
                            0.0,
                            robot.HSLIDER_MAX_POSITION
                        );
                        
                    if(!prevgamepad1.dpad_right && gamepad1.dpad_right)
                    {
                        localState = State.TRANSFER;
                    }
                break;
                
                case TRANSFER:
                    scoregripping = 0.0;
                    intakesliding = 0.0;
                    wristing = 0.0;
                    cycler = 0;
                    armPosition = robot.ARM_UPPER_POSITION;
                                 
                    if(!prevgamepad1.dpad_right && gamepad1.dpad_right)
                    {
                        doDelay = true;     // ultima urâțenie
                        didDelay1 = false;
                        didDelay2 = false;
                        scoregripping = 1.0;
                        localState = State.SCORE;
                    }
                    else if(!prevgamepad1.dpad_left && gamepad1.dpad_left)
                    {
                        localState = State.INTAKE;
                    }
                break;
                
                case SCORE:
                    
                    if (doDelay)
                    {
                        initialDelayTime = runtime.time();
                    }
                    doDelay = false;
                    delayGrip(initialDelayTime, 0.8, 0.2);
                    
                    //height cycler logic
                    cycler = (!prevgamepad1.dpad_up && gamepad1.dpad_up ? 
                        cycler+1 : cycler);
                    cycler = (!prevgamepad1.dpad_down && gamepad1.dpad_down ?
                        cycler-1: cycler);
                        
                    scoregripping = 
                        (!prevgamepad1.x && gamepad1.x ? 
                            1.0 - scoregripping 
                            : 
                            scoregripping
                        );
                        
                    if(cycler>1)
                    {
                        armPosition = robot.ARM_MID_POSITION;    
                    }
                    
                    if(!prevgamepad1.dpad_right && gamepad1.dpad_right)
                    {
                        scoregripping = 0.0;
                        localState = State.INTAKE;
                    }
                    else if(!prevgamepad1.dpad_left && gamepad1.dpad_left)
                    {
                        localState = State.TRANSFER;
                    }
                    
                break;
                
            }
            cycler = Range.clip(cycler, 0, robot.positions.size()-1);
            sliding = robot.positions.get(cycler);
            break;
            
            case MANUAL:
                // Hopefully this should never be needed
                

                robot.intakepivot.offset
                (
                    gamepad1.dpad_up ?
                        1
                        :
                        (  gamepad1.dpad_down ?
                            -1
                            :
                            0
                        )
                );
                        
                
                /*                
                armPosition = Range.clip
                ( 
                    armPosition + MANUAL_ARM_SPEED * 
                    (   gamepad2.dpad_left ?
                        1.0
                        :
                        ( gamepad2.dpad_right ?
                            -1.0
                            :
                            0.0
                        )
                    ), 
                    robot.ARM_LOWER_POSITION, 
                    robot.ARM_UPPER_POSITION
                );
                
                intakesliding = Range.clip
                ( 
                    intakesliding + MANUAL_SLIDE_SPEED * 
                    (   gamepad2.dpad_up ?
                        1.0
                        :
                        ( gamepad2.dpad_down ?
                            -1.0
                            :
                            0.0
                        )
                    ), 
                    0.0, 
                    robot.HSLIDER_MAX_POSITION
                );
                    
                sliding = Range.clip
                (
                    sliding + 
                    MANUAL_SLIDE_SPEED * 
                    ( gamepad1.dpad_up ? 
                        1.0 
                        :
                        ( gamepad1.dpad_down ?
                            -1.0
                            :
                            0.0
                        )
                    ),
                    0.0,
                    robot.VSLIDER_MAX_POSITION
                );
                
                intakegripping = 
                (!prevgamepad2.x && gamepad2.x ? 
                    1.0 - intakegripping 
                    : 
                    intakegripping
                );
                
                scoregripping = 
                (!prevgamepad1.x && gamepad1.x ? 
                    1.0 - scoregripping 
                    : 
                    scoregripping
                );
                
                wristing = Range.clip
                (
                    wristing + 
                    MANUAL_WRIST_SPEED * 
                    ( gamepad1.dpad_left ? 
                        1.0 
                        :
                        ( gamepad1.dpad_right ?
                            -1.0
                            :
                            0.0
                        )
                    ),
                    0.0,
                    1.0
                );
                */
                
            break;
        }
        
        // disabling boost when slider is above a certain height (mm)
        if(sliding > 500)
        {
            boost = 0.0;
        }
        double speed =  (1 - boost - shift) * defSpeed + boost + minSpeed * shift;
        // saving current gamepad state so we may compare against it n the next iteration        
        copyGamepad(gamepad1, prevgamepad1);        
        copyGamepad(gamepad2, prevgamepad2);        
        
        // sending calculated values to the hardware
        // robot.GripIntakeServo.setPosition(intakegripping)
        robot.intakeGrip(intakegripping);
        robot.scoreGrip(scoregripping);
        robot.slide(sliding);
        robot.horizslider.slide(intakesliding);
        robot.wristSetPosition(wristing); 
        robot.intakepivot.pivot(armPosition);
        robot.drivetrain.Drive(drive, strafe, turn, speed);
        
        // robot.slide(100.0);
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("MODE", "" + localMode.toString());
        telemetry.addData("STATE", "" + localState.toString());
        telemetry.addData("VL SLIDER", " h = %.2f, E = %.2f", robot.vertsliderL.getTargetExtension(), robot.vertsliderL.getExtension());
        telemetry.addData("VR SLIDER", " h = %.2f, E = %.2f", robot.vertsliderL.getTargetExtension(), robot.vertsliderR.getExtension());
        telemetry.addData("H SLIDER", " l = %.2f, E = %.2f", intakesliding, robot.horizslider.getExtension());
        telemetry.addData("WRIST", " a = %.2f", wristing);
        telemetry.addData("INTAKE ARM", " a = %.2f", robot.intakepivot.getAngle());
        telemetry.addData("GRIP INTAKE", " t = %.2f", intakegripping);
        telemetry.addData("GRIP SCORE", " t = %.2f", scoregripping);
        telemetry.update();
    }

    // @Override
    // public void stop() {
    //     //robot.slider.robot.slide(robot.positions.get(0));
    // }
}
