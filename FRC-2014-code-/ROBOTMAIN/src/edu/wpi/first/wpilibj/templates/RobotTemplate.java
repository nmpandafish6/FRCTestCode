/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import NerdHerd.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends NerdyBot {
    /**
public class RobotTemplate extends IterativeRobot
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    NerdyPIDRobot Robot;

    public void robotInit() {
        Robot = new NerdyPIDRobot();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        Robot.updateGyroValues();
        updateStatus();
 
    }
    
    /**
     * This function is called periodically during test mode
     */
  
    
    public void updateStatus(){
        SmartDashboard.putDouble("Joystick Angle" , Robot.get360JoystickAngle());
        SmartDashboard.putDouble("Time Period" , m_mainPeriodicTimer.getLastActualPeriod());
        SmartDashboard.putDouble("Heading", Robot.getHeading());
        SmartDashboard.putDouble("Gyro Rate", Robot.getRate());
        SmartDashboard.putDouble("JoystickAngle is : \t", Robot.get360JoystickAngle());
        SmartDashboard.putDouble("JoystickY", -Robot.JoystickMain.getY());
        SmartDashboard.putDouble("JoystickX", -Robot.JoystickMain.getX());
    }
}
