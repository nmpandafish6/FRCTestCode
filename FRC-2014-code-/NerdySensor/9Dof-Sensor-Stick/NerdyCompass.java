/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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


public class NerdyCompass extends SensorBase {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private I2C compass;
    private int xAxisCompass, yAxisCompass, zAxisCompass;
    private double heading;
    private double headingOffset = 0;
    
    public NerdyCompass() {
        DigitalModule module = DigitalModule.getInstance(1);
       
        compass = module.getI2C(0x3C);
        //compass.write(0x2D, 0x08);//TODO : add comments
        compass.write(0x00, 0x54);//Bits CRA6 and CRA5 are set to 10 to get a 4 sample average.CRA 4 - CRA 2 are set to 101 for 30 hertz reading. CRA 1 and CRA 0 are set to 00 for normal measurement configuration. 
        compass.write(0x01, 0x20);//Configs gain for default
        compass.write(0x02, 0x00);//Continuous Measurement Mode
    }

    public void read(){
        byte tempXYZ[] = new byte[6];
        compass.read(0x03, 6, tempXYZ);//TODO : add comments
        xAxisCompass = ((int)tempXYZ[0] << 8) | tempXYZ[1];  //combine 2 bytes into one integer for x;
        zAxisCompass = ((int)tempXYZ[2] << 8) | tempXYZ[3];  //combine 2 bytes into one integer for z;
        yAxisCompass = ((int)tempXYZ[4] << 8) | tempXYZ[5];  //combine 2 bytes into one integer for y; 
    }
    public int getAxisX() {
        return xAxisCompass;
    }
    public int getAxisY() {
        return yAxisCompass;
    }
    public int getAxisZ() {
        return zAxisCompass;
    }

}
