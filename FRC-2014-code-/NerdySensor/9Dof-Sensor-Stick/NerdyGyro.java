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


public class NerdyGyro extends SensorBase {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    private I2C gyro;
    private double xAxisGyro, yAxisGyro, zAxisGyro;
    
    public NerdyGyro() {
        DigitalModule module = DigitalModule.getInstance(1);
        gyro = module.getI2C(0xD0);
        gyro.write(0x15, 0x20);//Roughly 30 Hz. See formula in spec sheet.
        gyro.write(0x16, 0x1B);//Configs to full scale range. 11. Configs to 42Hz. 011.
        gyro.write(0x3E, 0x00);//Configs power to on. And clock to internal oscillator.
    }

    
    public void read(){
        byte tempXYZ[] = new byte[6];
        gyro.read(0x1D, 6, tempXYZ);
        xAxisGyro = (double)(((short)tempXYZ[0] << 8) | (short)tempXYZ[1]) / 14.375;  //combine 2 bytes into one integer for x;
        yAxisGyro = (double)(((short)tempXYZ[2] << 8) | (short)tempXYZ[3]) / 14.375;  //combine 2 bytes into one integer for y;
        zAxisGyro = (double)(((short)tempXYZ[4] << 8) | (short)tempXYZ[5]) / 14.375;  //combine 2 bytes into one integer for z; 
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
