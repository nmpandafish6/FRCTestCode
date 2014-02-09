/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package NerdHerd;

import edu.wpi.first.wpilibj.Gyro;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class NerdyPIDRobot{

    private double headingTolerance, distanceTolerance;
    private double error;
    private double heading, gyroRate;
    private double distanceTraveled = 0;
    private double KpAngular = 1/3;
    private double KiAngular = 1/10;
    private double KdAngular = 1;
    private double angularScaleFactor = 1/30;
    private double KpLinear = 1/3;
    private double KiLinear = 1/.45;
    private double KdLinear = 0.5;
    private double joystickThreshold = 0.1;
    private double distanceRemaining = 0;
    private boolean drive180Mode = false;
    //private double w = 0.2;
    private double lastTime = 0;
    public Gyro gyro;
    public Joystick JoystickMain;
    public CANJaguar LtDriveMain, LtDriveSub, RtDriveMain, RtDriveSub;
    public TrapezoidalIntegrator HeadingIntegrator, DistanceIntegrator, DistanceRemainingIntegrator;
    private LowPassFilter LowPassFilterLeft, LowPassFilterRight;
    
            
    public NerdyPIDRobot(){
    
    /*
    This function runs at the start of autonomous.
    It initializes the motors, Joystick, and 9dof sensor.
    It also does calculations to check for and elimate bias.
    */
    
        try{
            LtDriveMain = new CANJaguar(2);
            LtDriveSub = new CANJaguar(4);
            RtDriveMain = new CANJaguar(3);
            RtDriveSub = new CANJaguar(5);
        }catch(Exception e){
            System.out.println(e);
        }
        JoystickMain = new Joystick(2);
        HeadingIntegrator = new TrapezoidalIntegrator(0.050, 100);
        DistanceIntegrator = new TrapezoidalIntegrator(0.050);
        DistanceRemainingIntegrator = new TrapezoidalIntegrator(0.050);
        limiter = new Limiter(1);
        gyro = new Gyro(2);
        gyro.setSensitivity(0.0125);

    }
    
    public void setHeadingTolerance(double degree){
    
    /*
    This sets a tolerance for the heading in degrees.
    Heading is tolerable if is + or - tolerance.
    Default is 0.
    */
    
        headingTolerance = degree;
    }
    
    public double getHeadingTolerance(){
    
    /*
    Returns heading tolerance in degrees. 
    If not set, this should return 0.
    */
    
        return headingTolerance;
    }
    
    public boolean isHeadingTolerable(double desiredAngle){
    
    /*
    Returns true if the heading is within the tolerance
    Otherwise returns false.
    */
    
        double lastHeading = getHeading();
        return ((desiredAngle < lastHeading+headingTolerance) && (desiredAngle > lastHeading-headingTolerance));
    }
    
    public double getHeading(){
    
    /*
    Returns the last updated Heading.
    */
    
        return heading;
    }
    
    public void setDistanceTolerance(double meter){
    
    /*
    This sets a tolerance for the distance traveled in meters.
    Distance traveled is tolerable if is + or - tolerance.
    Default is 0.
    */
    
        distanceTolerance = meter;
    }
    
    public double getDistanceTolerance(){
    
    /*
    Returns heading tolerance in meters. 
    If not set, this should return 0.
    */
    
        return distanceTolerance;
    }
    
    public boolean isDistanceTolerable(double desiredDistance){
    
    /*
    Returns true if the distance traveled is within the tolerance
    Otherwise returns false.
    */
    
        double distance_Traveled = getDistanceTraveled();
        return ((desiredDistance < distance_Traveled+distanceTolerance) && (desiredDistance > distance_Traveled-distanceTolerance));
    }
    
    public double getDistanceTraveled(){
    
    /*
    Returns the last updated distance traveled.
    Must use calcDistanceTraveled() prior to get the most recent result.
    */
    
        return distanceTraveled;
    }
    
    public double calcDistanceTraveled(){
        
    /*
    Calculates the distance traveled
    */
        
        //distanceTraveled = Math.sqrt(sqr(Accel.getAxisX()) + sqr(Accel.getAxisY()));
        distanceTraveled = DistanceIntegrator.updateAccumulation(distanceTraveled);
        return distanceTraveled;
    }
    
    public double calcDistanceRemaining(double desiredDistance){
        
        distanceRemaining = desiredDistance - distanceTraveled;
        return distanceRemaining;
    }
    
    public double getDistanceRemaining(){
        
        return distanceRemaining;
    }
    
    public void resetDistance(){
     
    /*
    Resets distance traveled to 0.   
    */
        
        distanceTraveled = 0;
    }
    
    public double getRate(){
        
        return gyroRate;
    }
    
    public double get360JoystickAngle(){
    
    /*
    Grabs an 360 degree angle reading from the joystick.
    0 degrees is north. 90 degrees is west.
    Should rewrite for arcade drive so that robot always faces forward.
    Should subtract Joystick Bias.
    */
    
        double y = -JoystickMain.getY();
        double x = JoystickMain.getX();
            if (Math.abs(x) < joystickThreshold && Math.abs(y) < joystickThreshold ){
                //need heading update code
                return heading;
                }
            double angle = (MathUtils.atan2(y,x)) * 180 / Math.PI + 270;
        if (drive180Mode){
            if (angle >= 90 && angle <= 270){
                angle += 180;//This makes sure that the robot always faces forward.
                angle %= 180;
            }
        }
        return angle%360;
    }
    
    public void updateGyroValues(){
    
    /*
    Updates and returns the heading.
    Heading is not tilt compensated.
    Dependent on a working gyro. 
    */
        heading = gyro.getAngle() % 360;
        gyroRate = gyro.getRate();
    }
    
    private double calcShortestRotation(double desiredAngle){
    
    /*
    Returns the degrees off between the desired angle and the heading.
    Must use calcHeadingNTC(), calcHeadingTC(), or gyroCompassHeading() prior
    to get the most recent result.
    */
    
        updateGyroValues();
        error = desiredAngle - heading;
        if (Math.abs(error) >  180){
            error = -sign(error)*(360 - Math.abs(error));
        }
        return error;
    }   
    
    public double getPIDOutputAngular(double desiredAngle){
    
    /*
    Returns the PIDOutput given in relation to the desired angle and heading
    Use for motor power
    */
        
        double err = calcShortestRotation(desiredAngle);
        double rateCommand = (err * KpAngular) + (HeadingIntegrator.updateAccumulation(err) * KiAngular);
        double PIDOutputAngular = constrain((rateCommand - gyroRate) * KdAngular*angularScaleFactor, 1);
        //angularScaleFactor is to normalize the output to -1 - 1
        return PIDOutputAngular;
    }
    
    public double getPIDOutputLinear(double desiredDistance){
    
    /*
    Returns the PIDOutput given in relation to the desired distance
    Use for motor power
    */
        
        calcDistanceRemaining(desiredDistance);
        double P = distanceRemaining * KpLinear;
        double I = DistanceRemainingIntegrator.updateAccumulation(distanceRemaining) * KiLinear;
        double PIDOutputLinear = (P + I) * KdLinear; 
        return PIDOutputLinear;
    }
    
    public void moveAndRotate(double desiredAngle, double linearPower){
       
    /*
    Rotates the wheels so that the robot faces the desired angle
    and travels at a set speed.
    To use in autonomous, set while loops checking if the distanceTraveled is not tolerable
    and if the heading is not tolerable. Include a if statement checking if the higher priority task
    is complete yet and a break in between. Pass in the vision results for desired heading and 
    the linearPower calculated from the getPIDOutputLinear() function.
    To use during teleop, set desired angle to the get360JoystickAngle() and the linearPower to 
    JoystickMain.getY();
    The 0.5s declaring the left and right powers can be changed as long as the sum is 1.0
    */
       
        double ltPower, rtPower;
        double angularPower = getPIDOutputAngular(desiredAngle);
        double scaleFactor = 1-Math.abs(angularPower);//scaleFactor is to favor the rotation 
        ltPower = angularPower - linearPower*scaleFactor;
        rtPower = angularPower + linearPower*scaleFactor;
        
        ltPower = constrain(ltPower, 1);
        rtPower = constrain(rtPower, 1);
        LowPassFilterLeft.calculate(ltPower);
        LowPassFilterRight.calculate(rtPower);
        try{
            LtDriveMain.set(ltPower);
            LtDriveMain.set(ltPower);
            RtDriveMain.set(rtPower);
            RtDriveMain.set(rtPower);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    
    public void rotate(double angularPower){
    
    /*
    To use during autonomous, use GetPIDOutputAngular(desiredAngle)
    To use during teleop, use JoystickMain.getTwist()
    */

        LowPassFilterLeft.calculate(angularPower);
        try{
            LtDriveMain.set(angularPower);
            LtDriveSub.set(angularPower);
            RtDriveMain.set(angularPower);
            RtDriveSub.set(angularPower);
        }catch (Exception e){
            System.out.println(e);
        }
    }
        
    private int sign(double number){
        
    /*
    Helper Function. Returns the sign of a number. Positive numbers return 1.
    Negative numbers return -1. Zero returns zero.
    */
        if (number > 0){
            return 1;
        }else if (number < 0){
            return -1;
        } else {
            return 0;
        }
    }
    
    private double sqr(double number){
        return number*number;
    }
    
    public double testSin(double w){// W is radians per second
        double A = 1; //Amplitude
        double t = 0.050;
        double testSin = A * Math.sin(w * t);
        return testSin;
    }
    
    private double constrain (double value, double m_limit){
    if(value > m_limit){
    value = m_limit;
    }else if (value < -m_limit){
    value = -m_limit;
    }
    return value; 
    }
} 
