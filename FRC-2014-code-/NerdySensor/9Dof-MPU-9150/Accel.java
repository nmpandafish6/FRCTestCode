package NerdHerd.Sensor;


import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SensorBase;




/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */


public class NerdyAccel extends SensorBase {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private I2C accel;
    private double xAxisAccel, yAxisAccel, zAxisAccel;
    
    public NerdyAccel() {
        DigitalModule module = DigitalModule.getInstance(1);
        accel = module.getI2C(0x69);//Else Try 68
        accel.setCompatabilityMode(true);
        accel.write(0x6B, 0x80);//Power
		accel.write(0x1A, 0x01);//Basic Config
		accel.wrtie(0x1C, 0x10);//Accel Config
		

    }
    
    public void read(){
        byte tempXYZ[] = new byte[6];
        accel.read(0x3B, 6, tempXYZ);
        xAxisAccel = ( ((short) (tempXYZ[1]) & 0xff00) << 8) | (((short)tempXYZ[0]) & 0xff);  //combine 2 bytes into one integer for x;
        yAxisAccel = ( ((short) (tempXYZ[3]) & 0xff00) << 8) | (((short)tempXYZ[2]) & 0xff);  //combine 2 bytes into one integer for x;
        zAxisAccel = ( ((short) (tempXYZ[5]) & 0xff00) << 8) | (((short)tempXYZ[4]) & 0xff);  //combine 2 bytes into one integer for x;
    }
    public double getAxisX() {
        return xAxisAccel *  0.00390625;
    }
    public double getAxisY() {
        return yAxisAccel *  0.00390625;
    }
    public double getAxisZ() {
        return zAxisAccel *  0.00390625;
    }  
}
