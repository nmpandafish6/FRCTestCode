package edu.wpi.first.wpilibj.templates;

import NerdHerd.Sensor.NerdyAccel;
import NerdHerd.Sensor.NerdyCompass;
import NerdHerd.Sensor.NerdyGyro;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    NerdyCompass Compass;
    NerdyAccel Accel;
    NerdyGyro Gyro;
    Joystick Joystick1;
    public void robotInit() {

        System.out.print("HEllo");
        
        Compass = new NerdyCompass();
        //Accel = new NerdyAccel();
        //Gyro = new NerdyGyro();
        
        
        Compass.init();
        //Accel.initAccel();
        //Gyro.initGyro();
        
        Joystick1 = new Joystick(1);
        System.out.println("HelloWOrld");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        System.out.println("before");
        Compass.read();
        //Gyro.readGyro();
        //Accel.readAccel();
        System.out.println("after");
        int bearing = Compass.getAxisX();
//        boolean isCWShorter = Compass.IsCWShorter(5.0);
        
        //System.out.println("Bearing is " + bearing + "\t CWShorter is ");
        System.out.println("X: "+Compass.getAxisX() + "Y: " + Compass.getAxisY() + "Z: " + Compass.getAxisZ());
        //System.out.println("X: "+Gyro.getAxisX() + "Y: " + Gyro.getAxisY() + "Z: " + Gyro.getAxisZ());
        //System.out.println("X: "+Accel.getAxisX() + "Y: " + Accel.getAxisY() + "Z: " + Accel.getAxisZ());
        
    
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    

        
    }
    
}
