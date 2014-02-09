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
    private CANJaguar Ltmain, Ltsub, Rtmain, Rtsub;
    private Joystick LtJoystick, RtJoystick;
    
    
    public void robotInit() {
        
        LtJoystick  = new Joystick(1);
        RtJoystick  = new Joystick(2);
        try{
            Ltmain  = new CANJaguar(2);
            Ltsub   = new CANJaguar(3);
            Rtmain  = new CANJaguar (4);
            Rtsub   = new CANJaguar (5);
        
        } catch (Exception e){
            System.out.println(e);
        }
                
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
        double rotationvalue = LtJoystick.getX();
        double motionXvalue  = RtJoystick.getX();
        double motionYvalue  = RtJoystick.getY();
        
        //math goes here
        try{
        Ltmain.set(+ motionXvalue + rotationvalue - motionYvalue);
        Ltsub.set(- motionXvalue + rotationvalue - motionYvalue);
        Rtmain.set(-motionXvalue - rotationvalue + motionYvalue);
        Rtsub.set(+motionXvalue - rotationvalue + motionYvalue);
        //constraints
        }catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
