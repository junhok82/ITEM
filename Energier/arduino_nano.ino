int id=99;
#include <SoftwareSerial.h>

#include "EmonLibA.h"
// Include Emon Library
EnergyMonitor emon1;
// Create an instance

unsigned long prev_time=0;
int cnt1 = 0;
int cnt2 = 0;
int cnt3 = 0;

SoftwareSerial sw(2, 3); // RX, TX vbgb   
void setup() {
 Serial.begin(115200);
 Serial.println("Interfacfing arduino with nodemcu");
 sw.begin(115200);
 emon1.voltage(A1, 1, 1.7);
 emon1.current(A0, 55.56); 
}

void loop() {

 //double Irms = emon1.calcIrms(4000);
   unsigned long current_time = millis();
   emon1.calcVI(20,4000);
   /*
   if((current_time-prev_time)>3000){
     emon1.serialprint(); 
     prev_time = current_time;
    }
  */
   
   double Irms = emon1.ShowIrms(); 
   double Vrms = emon1.ShowVrms();
   double realPower = emon1.ShowrealPower();
   double apparentPower = emon1.ShowapparentPower();
   double Power_fee = apparentPower/120000;
   
   if(apparentPower < 200000)
   {
    Power_fee = Power_fee * 78.3 + 730; 
   }
   else if(apparentPower < 400000)
   {
    Power_fee = Power_fee * 147.3 + 1260;
   }
   else
   {
    Power_fee = Power_fee * 215.6;
   }
    
    /*
    ShowIrms();
    ShowVrms();
    ShowapparentPower();
    ShowrealPower();
    ShowpowerFactor();

   */
    
   Serial.println("Sending data to nodemcu");
   Serial.print("Irms : ");
   Serial.println(Irms);
   Serial.print("Vrms : ");
   Serial.println(Vrms);
   Serial.print("Real_power : ");
   Serial.println(realPower);
   Serial.print("apparentPower : ");
   Serial.println(apparentPower);
   Serial.print("fee : ");
   Serial.println(Power_fee);


// delay(30);
 sw.print("arduino");
 sw.print(' ');
 sw.print(apparentPower);//offset
 sw.print(' ');
 sw.print(Power_fee);
 sw.println();
 delay(3000);
}
