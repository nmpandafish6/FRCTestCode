package NerdHerd;


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
        //for(int i = 0; i < 256; i++){
        accel = module.getI2C(0xA7);
        accel.write(0x2D, 0x08);
        accel.write(0x31, 0x08 | 0x03);
        byte initbyte[] = new byte[1];
        accel.read(0x0, 1, initbyte);
        System.out.println("Init byte is : " + initbyte[0] + "Code is : ");
        //}
    }
    
    public void read(){
        byte tempXYZ[] = new byte[6];
        accel.read(0x32, 6, tempXYZ);
        xAxisAccel = (double)(((short)tempXYZ[1] << 8) | (short)tempXYZ[0]) *  0.00390625;  //combine 2 bytes into one integer for x;
        yAxisAccel = (double)(((short)tempXYZ[3] << 8) | (short)tempXYZ[2]) *  0.00390625;  //combine 2 bytes into one integer for y;
        zAxisAccel = (double)(((short)tempXYZ[5] << 8) | (short)tempXYZ[4]) *  0.00390625;  //combine 2 bytes into one integer for z;
    }
    public double getAxisX() {
        return xAxisAccel;
    }
    public double getAxisY() {
        return yAxisAccel;
    }
    public double getAxisZ() {
        return zAxisAccel;
    }  
}
