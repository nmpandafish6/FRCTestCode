package edu.wpi.first.wpilibj.templates;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/




//import NerdHerd.Source.*;
import PID_Robot.NerdyPIDRobot;
import NerdHerd.Source.NerdyBot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a test of the nerdyBot robotclass
 * This should run a modified version of the interative robot
 * This should run the teleopcontinous void all the time
 */
public class RobotTemplate extends NerdyBot{
    Joystick Joystick1;
    NerdyPIDRobot Robot;
    Command command;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        Robot = new NerdyPIDRobot();
        Joystick1 = new Joystick(1);
        command.start();
        SmartDashboard.putData(Scheduler.getInstance());
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        while(!Robot.isDistanceTolerable(3)){
            while (!Robot.isHeadingTolerable(90)){
               Robot.MoveAndRotate(90, Robot.getPIDOutputLinear(3));

                if(!Robot.isDistanceTolerable(3)){
                  break;
               }
            }
        }
        while (!Robot.isHeadingTolerable(90)){
            Robot.Rotate(Robot.GetPIDOutputAngular(90));
             updateStatus();
        }
    }
    
    
    /**
     * This function is called each time operator control starts
     */
    public void teleopInit() {
           updateStatus();
    }

    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        Scheduler.getInstance().run();
        double desiredAngle = Robot.get360JoystickAngle();
        double linearPower = Robot.JoystickMain.getY();
        Robot.MoveAndRotate(desiredAngle, linearPower);
        updateStatus();
        
    }
       public void updateStatus(){
        Robot.updateStatus();
         
       }
    /**
     * This function is called repeatedly during teleop mode
     */
    public void teleopContinous(){

    }
}
