/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package NerdHerd.Utilities;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

    
public class TrapezoidalIntegrator {
   
    private double m_previousValue, m_sampleTime, m_accumulation, m_accumulationLimit;
    private long m_lastValue = 0;
            
     
    //Constructor
    public TrapezoidalIntegrator(double sampleTimeIn) {
            m_sampleTime = sampleTimeIn;
    }
    
   
    //This is the default constructor
    public TrapezoidalIntegrator() {
            m_sampleTime = .05; 
    }

    /**
     * This function sets the sample time
     * @param sampleTimeIn
     */
    public void setSampleTime(double sampleTimeIn) {
            m_sampleTime = sampleTimeIn;
    }
    

    /**
     * This function returns the sample time
     * @return 
     */
    public double getsampleTime() {
        return m_sampleTime;
        
    }
    
    public void resetAccumulation() {
            m_accumulation = 0;
            m_lastValue = 0;
    }
    
    public double updateAccumulation(double currentValue){
      m_accumulation = m_accumulation + ((m_lastValue + currentValue)*m_sampleTime)/2;
        if (m_accumulation > m_accumulationLimit){
            m_accumulation = m_accumulationLimit; 
        }
        else if(m_accumulation < -m_accumulationLimit){
            m_accumulation = -m_accumulationLimit;
        }
      return m_accumulation;
        
    }
    
}
