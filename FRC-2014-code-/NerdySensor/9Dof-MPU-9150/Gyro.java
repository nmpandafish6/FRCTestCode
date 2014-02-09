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


public class NerdyGyro extends SensorBase {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private I2C gyro;
    private double xAxisGyro, yAxisGyro, zAxisGyro;
    
    public NerdyGyro() {
        DigitalModule module = DigitalModule.getInstance(1);
        gyro = module.getI2C(0x69);//Else Try 68
        gyro.setCompatabilityMode(true);
        gyro.write(0x6B, 0x80);//Power
		gyro.write(0x1A, 0x01);//Basic Config
		gyro.write(0x1B, 0x00);//Gyro Config
		
        
        byte x[] = new byte[1];
        gyro.read(0x75, 1, x);
        System.out.println("InitByte is : " + x[0] );
    }
    
    public void read(){
        byte tempXYZ[] = new byte[6];
        gyro.read(0x43, 6, tempXYZ);
        xAxisGyro = ( ((short) (tempXYZ[1]) & 0xff00) << 8) | (((short)tempXYZ[0]) & 0xff);  //combine 2 bytes into one integer for x;
        yAxisGyro = ( ((short) (tempXYZ[3]) & 0xff00) << 8) | (((short)tempXYZ[2]) & 0xff);  //combine 2 bytes into one integer for x;
        zAxisGyro = ( ((short) (tempXYZ[5]) & 0xff00) << 8) | (((short)tempXYZ[4]) & 0xff);  //combine 2 bytes into one integer for x;
    }
    public double getAxisX() {
        return xAxisGyro;
    }
    public double getAxisY() {
        return yAxisGyro;
    }
    public double getAxisZ() {
        return zAxisGyro;
    }  
}
