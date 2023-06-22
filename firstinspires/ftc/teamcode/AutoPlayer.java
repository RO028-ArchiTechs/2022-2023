package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.State;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autorecorder.DecodeController;

@Autonomous(name="Geo's Auto", group="Iterative Opmode")

public class AutoPlayer extends OpMode{
    
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
    private double HSLIDE_SPEED = 5;
    
    private double MANUAL_SLIDE_SPEED = 6.0;    // mm/iter
    private double MANUAL_ARM_SPEED = 3.0;      // deg/iter
    private double MANUAL_GRIP_SPEED = 0.1;     // mm/iter
    private double MANUAL_WRIST_SPEED = 0.005;  // parameter/iter
     
    // state variables
    boolean prev_dpad_up = false;
    boolean prev_dpad_down = false;
    boolean prev_dpad_left = false;
    boolean prev_dpad_right = false;
    private int cycler = 0;
    private double armPosition = 0;
    
    enum Mode {AUTO, MANUAL}; 
    enum State {INTAKE, TRANSFER, SCORE}; 
    
    private Mode localMode; 
    private State localState; 
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
    private DecodeController controller;
    
    
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
    

@Override
public void init()
{
    controller = new DecodeController(this);
        
        robot = new HardwareRobot(hardwareMap);
        controller = new DecodeController(this);
        telemetry.addData("Status", "Initialized");
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
}

@Override
public void init_loop()
{
    
}

@Override
public void start()
{
        runtime.reset();
        controller.reset_runtime();
    
}

@Override
public void loop()
{
        controller.read_state();
        
        // Drivetrain input on Gp1
        double drive  =  controller.get_left_stick_y();
        double strafe = -controller.get_left_stick_x();
        double turn   = -controller.get_right_stick_x();
        double boost  =  controller.get_right_trigger();
        double shift  =  controller.get_left_trigger();  
           
        /*
        */
        //toggles mode when driver presses the Logitech button 
        
        // different input handling depending on current mode
            switch(localState){
                case INTAKE:
                    wristing = 0;
                    cycler = 0;
                    armPosition = robot.ARM_LOWER_POSITION;
                     
                    intakegripping = 
                        (controller.get_x() ? 
                            1.0 
                            : 
                            (  controller.get_y() ?
                                0.0
                                :
                                intakegripping
                            )
                        );
                    
                    intakesliding = Range.clip
                        ( intakesliding +
                        HSLIDE_SPEED *
                        multiplier(controller.get_dpad_up(), controller.get_dpad_down()),
                            0.0,
                            robot.HSLIDER_MAX_POSITION
                        );
                        
                    if(!prev_dpad_right && controller.get_dpad_right())
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
                                 
                    if(!prev_dpad_right && controller.get_dpad_right())
                    {
                        doDelay = true;     // ultima urâțenie
                        didDelay1 = false;
                        didDelay2 = false;
                        scoregripping = 1.0;
                        localState = State.SCORE;
                    }
                    else if(!prev_dpad_left && controller.get_dpad_left())
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
                    cycler = (!prev_dpad_up && controller.get_dpad_up() ? 
                        cycler+1 : cycler);
                    cycler = (!prev_dpad_down && controller.get_dpad_down() ?
                        cycler-1: cycler);
                        
                    scoregripping = 
                        (controller.get_x() ? 
                            1.0 
                            : 
                            (  controller.get_y() ?
                                0.0
                                :
                                intakegripping
                            )
                        );
                        
                    if(cycler>1)
                    {
                        armPosition = robot.ARM_MID_POSITION;    
                    }
                    
                    if(!prev_dpad_right && controller.get_dpad_right())
                    {
                        scoregripping = 0.0;
                        localState = State.INTAKE;
                    }
                    else if(!prev_dpad_left && controller.get_dpad_left())
                    {
                        localState = State.TRANSFER;
                    }
                    
                break;
                
            }
            cycler = Range.clip(cycler, 0, robot.positions.size()-1);
            sliding = robot.positions.get(cycler);
            
        
        // disabling boost when slider is above a certain height (mm)
        if(sliding > 500)
        {
            boost = 0.0;
        }
        double speed =  (1 - boost - shift) * defSpeed + boost + minSpeed * shift;
        // saving current gamepad state so we may compare against it n the next iteration        
        prev_dpad_up = controller.get_dpad_up();
        prev_dpad_down = controller.get_dpad_down();
        prev_dpad_left = controller.get_dpad_left();
        prev_dpad_right = controller.get_dpad_right();
        
        // sending calculated values to the hardware
        robot.intakeGrip(intakegripping);
        robot.scoreGrip(scoregripping);
        robot.slide(sliding);
        robot.horizslider.slide(intakesliding);
        robot.wristSetPosition(wristing); 
        robot.intakepivot.pivot(armPosition);
        robot.drivetrain.DriveWPower(robot.drivetrain.calcPower(drive, strafe, turn, speed));
        
        
        //printing useful information
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("DEBUG", "drive %.2f" , controller.get_left_stick_y());
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
    
}