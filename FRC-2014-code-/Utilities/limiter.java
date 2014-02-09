package NerdHerd.Utilities;



/**
 *
 * @author Jamari Ducre
 */
public class Limiter{
   double m_limit;
   
     public Limiter (){
         // default constructor.
        m_limit = 1e10; 
        
    }
    
    public Limiter (double limitIn){
        m_limit = limitIn; 
        
    }
public double getLimitedValue (double value){
    if(value > m_limit){
    value = m_limit;
    }else if (value < -m_limit){
    value = -m_limit;
    }
    return value; 
}
 }

