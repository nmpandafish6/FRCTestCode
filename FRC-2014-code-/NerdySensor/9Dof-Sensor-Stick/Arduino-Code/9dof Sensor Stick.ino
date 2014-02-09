// Demonstrates use of the Wire library
// Writes data to an I2C/TWI slave device
// Refer to the "Wire Slave Receiver" example for use with this

// Created 29 March 2006

// This example code is in the public domain.


#include <Wire.h>

int xAxisGyro, yAxisGyro, zAxisGyro;
int xAxisCompass, yAxisCompass, zAxisCompass;
int xAxisAccel,yAxisAccel, zAxisAccel;

double CMx,CMy; // CMx and CMy are vector magnet forces.
int heading, error; // 0 - 359 Compass Bearing and Current Offset from desired Angle

void setup()
{
  Wire.begin();
  Serial.begin(9600);
  Serial.println("Golf Wang");
  initAccel();
  setCompass();
  initGyro();  
 
}



void loop()
{
  updateGyro();
  updateCompassValues();
  updateAccel();
  Serial.print("Accelerometer\t\t");
  Serial.print(xAxisAccel);
  Serial.print(".");
  Serial.print(yAxisAccel);
  Serial.print(".");
  Serial.println(zAxisAccel);
  
  Serial.print("Compass\t\t\t");
  Serial.print(xAxisCompass);
  Serial.print(",");
  Serial.print(yAxisCompass);
  Serial.print(",");
  Serial.println(zAxisCompass);
  
  Serial.print("Gyro\t\t\t");
  Serial.print(xAxisGyro);
  Serial.print(".");
  Serial.print(yAxisGyro);
  Serial.print(".");
  Serial.println(zAxisGyro);
  delay(250);
}







void updateAccel(){
  Wire.beginTransmission(0x53);
  Wire.write(0x32);
  Wire.requestFrom(0x53, 6);
  int tempXYZ[6];
  int x = 0;
  
  
  while(Wire.available())    // slave may send less than requested
  { 
    tempXYZ[x] = Wire.read();
    x++;
  }
  Wire.endTransmission();

 xAxisAccel = (tempXYZ[1] << 8) | tempXYZ[0];
 yAxisAccel = (tempXYZ[3] << 8) | tempXYZ[2];
 zAxisAccel = (tempXYZ[5] << 8) | tempXYZ[4];
}

void initAccel(){
  Wire.beginTransmission(0x53);
  Wire.write(0x2D);
  Wire.write(0x08);
  Wire.endTransmission();
}

void setCompass(){
  Wire.beginTransmission(0x1E); //start call
  Wire.write(0x02);
  Wire.write(0x00);
  Wire.endTransmission();//hang up   
  
}

void updateCompassValues(){
 
   Wire.beginTransmission(0x1E); //transmit to device 4
  Wire.write(0x03); //write to address
  Wire.requestFrom(0x1E, 6);    // request 6 bytes from slave device #2
  int a = 0; // begin by setting a to zero for loop
  int tempXYZ[6];
  while(Wire.available())    // slave may send less than requested
  { 
    tempXYZ[a] = Wire.read(); // receive a byte as character
    a++;         // print the character
  }
  Wire.endTransmission();
  xAxisCompass = (tempXYZ[0] << 8) | tempXYZ[1];  //combine 2 bytes into one integer for x;
  zAxisCompass = (tempXYZ[2] << 8) | tempXYZ[3];  //combine 2 bytes into one integer for x;
  yAxisCompass = (tempXYZ[4] << 8) | tempXYZ[5];  //combine 2 bytes into one integer for x;
//Y and Z are switched to account for compass orientations
}

void updateGyro(){
  Wire.beginTransmission(0x68);
  Wire.write(0x1D);
  Wire.requestFrom(0x68, 6);
  int tempXYZ[6];
  int x = 0;

  while(Wire.available())
{
  tempXYZ[x] = Wire.read();
    x++;
}
  Wire.endTransmission();
 
  xAxisGyro = (tempXYZ[0] << 8) | tempXYZ[1];
  yAxisGyro = (tempXYZ[2] << 8) | tempXYZ[3];
  zAxisGyro = (tempXYZ[4] << 8) | tempXYZ[5];
  
}

void initGyro(){
  
  Wire.beginTransmission(0x68);
  Wire.write(0x3E);
  Wire.write(0x01);
  Wire.endTransmission();
}


void updateCMx(float pitch, float roll){

 CMx = xAxisCompass*cos(asin(xAxisAccel)) + yAxisCompass*sin(asin(yAxisAccel))*sin(asin(xAxisAccel)) + zAxisCompass*cos(asin(yAxisAccel))*sin(asin(xAxisAccel));
//   CMx = xAxisCompass;
}

void updateCMy(float roll){
 
  CMy = yAxisCompass*cos(asin(yAxisAccel)) - zAxisCompass*sin(asin(yAxisAccel));
//    CMy = yAxisCompass;
}

void updateHeading(){
 
 heading = atan(CMy/CMx) % 360;
}



boolean IsCWShorter(desiredAngle){

int CWDistance = abs(bearing - desiredAngle);
int CCWDistance = 360 - abs(bearing 0 desiredAngle);

if (CWDistance <= CCWDistance){
   error = CWDistance;
   return 1;
   }else{
   error = CCWDistance;
   return 0;
}

}
