char buffer[8];
int commandSize = 8;

byte leftEngine = 9;
byte rightEngine = 10;

String recievedCmd;
String cmdStop = "00000000";
String cmdStraight = "00000001";
String cmdLeft = "00000010";
String cmdRight = "00000011";
String cmdSlowStraight = "10000001";
String cmdSlowLeft = "10000010";
String cmdSlowRight = "10000011";
  
void setup() {
  Serial.begin(9600);
  Serial.setTimeout(1000);
}
  
byte fullStop = 0;
byte slowStart = 70;
int fullStart = 255;
  
void loop() {
    
  
      
//  if (off && brightness > 0) {    
//     brightness = brightness - fadeAmount;
//  } else if (on && brightness < 255){
//     brightness = brightness + fadeAmount;
//  }
//  
//  analogWrite(9, brightness);  
//  delay(30);
}

void serialEvent() {
  if (Serial.available() > 0) {
      int n = Serial.readBytesUntil('\n', buffer, commandSize);
      if (n >= 0) {
        buffer[n] = '\0';
        recievedCmd = buffer;
        Serial.print(recievedCmd);
      }
    
      if (recievedCmd == cmdStop) 
      {
        analogWrite(leftEngine, fullStop);  
        analogWrite(rightEngine, fullStop);
      } else if (recievedCmd == cmdStraight) 
      {   
        analogWrite(leftEngine, fullStart);  
        analogWrite(rightEngine, fullStart);  
      } else if (recievedCmd == cmdLeft) 
      {   
        analogWrite(leftEngine, fullStop);  
        analogWrite(rightEngine, fullStart);  
      } else if (recievedCmd == cmdRight) 
      {   
        analogWrite(leftEngine, fullStart);  
        analogWrite(rightEngine, fullStop);  
      } else if (recievedCmd == cmdSlowStraight) 
      {   
        analogWrite(leftEngine, slowStart);  
        analogWrite(rightEngine, slowStart);
      } else if (recievedCmd == cmdSlowLeft) 
      {   
        analogWrite(leftEngine, fullStop);  
        analogWrite(rightEngine, fullStart);  
      } else if (recievedCmd == cmdSlowRight) 
      {   
        analogWrite(leftEngine, fullStart);  
        analogWrite(rightEngine, fullStop); 
      }      
  }
}

