//#include <IRremote.h>

#define PIN_IR 3
#define PIN_DETECT 2
#define PIN_STATUS 6
#define PIN_SENS_DIOD 3
#define PIN_SENS 0

char buffer[7];
byte commandSize = 6;

byte leftEngine = 9;
byte rightEngine = 10;

byte fullStop = 0;
byte slowStart = 100;
int fullStart = 255;
int val_sum = 0;

String recievedCmd;
String cmdStop = "00000";
String cmdStraight = "00001";
String cmdLeft = "00010";
String cmdRight = "00011";
String cmdSlowStraight = "10001";
String cmdSlowLeft = "10010";
String cmdSlowRight = "10011";

//IRsend irsend;
void setup()
{
  pinMode(leftEngine, OUTPUT);
  pinMode(rightEngine, OUTPUT);

  pinMode(PIN_DETECT, INPUT);
  pinMode(PIN_STATUS, OUTPUT);
  pinMode(PIN_SENS_DIOD, OUTPUT);
//  irsend.enableIROut(38);
//  irsend.mark(0);
//  pinMode(6, OUTPUT);
//    pinMode(5, OUTPUT);
  Serial.begin(9600);
  Serial.setTimeout(1000);
}
boolean isDetected = false;
int val = 0;

void loop() {
  
//irsend.space(0);
//delay(1);
//irsend.mark(0);
//digitalWrite(PIN_STATUS, !digitalRead(PIN_DETECT));
//isDetected = !digitalRead(PIN_DETECT);
//if (isDetected) {
//  command("00000");
//}
  
//delay(1);
  
//  grabStatistics();
//digitalWrite(PIN_STATUS, !digitalRead(PIN_DETECT));
if (Serial.available() > 0) {
    int n = Serial.readBytesUntil('\n', buffer, commandSize);
    if (n >= 0) {
      // set end of the string
      buffer[n] = '\0';
      recievedCmd = buffer;

      command(recievedCmd);

      // send response to server
      sendResponse(recievedCmd);
    }
  }
//   detectPresence();
//  sendResponse("" + );

//if (isObstacle()) {
//    Serial.println(val);
//    command("00000");
//}
  if (isObstacle()) {
        command("00000");
//        delay(1000);
        if (isObstacle()) {
          command("10011");
//          delay(3000);
//          command("00000");
          if (!isObstacle()) {
            command("00001");
          }
        }
  } else {
    command("00001");
  }
  Serial.println(val);

}


void sendResponse(String response) {
  Serial.print("cmd " + response);
  Serial.flush();
}

//void serialEvent() {
//  if (Serial.available() > 0) {
//    int n = Serial.readBytesUntil('\n', buffer, commandSize);
//    if (n >= 0) {
//      // set end of the string
//      buffer[n] = '\0';
//      recievedCmd = buffer;
//
//      command(recievedCmd);
//
//      // send response to server
//      sendResponse(recievedCmd);
//    }
//  }
//}

void command(String recievedCmd) {
  if (recievedCmd == cmdStop) 
  {
    analogWrite(leftEngine, fullStop);  
    analogWrite(rightEngine, fullStop);
  } 
  else if (recievedCmd == cmdStraight) 
  {   
    analogWrite(leftEngine, fullStart);  
    analogWrite(rightEngine, fullStart);  
  } 
  else if (recievedCmd == cmdLeft) 
  {   
    analogWrite(leftEngine, fullStop);  
    analogWrite(rightEngine, fullStart);  
  } 
  else if (recievedCmd == cmdRight) 
  {   
    analogWrite(leftEngine, fullStart);  
    analogWrite(rightEngine, fullStop);  
  } 
  else if (recievedCmd == cmdSlowStraight) 
  {   
    analogWrite(leftEngine, slowStart);  
    analogWrite(rightEngine, slowStart);
  } 
  else if (recievedCmd == cmdSlowLeft) 
  {   
    analogWrite(leftEngine, fullStop);  
    analogWrite(rightEngine, fullStart);  
  } 
  else if (recievedCmd == cmdSlowRight) 
  {   
    analogWrite(leftEngine, fullStart);  
    analogWrite(rightEngine, fullStop); 
  }
}

//void grabStatistics() {
//  val_sum = 0;
//  int counter = 0;
//  while(1){
//    int i = detectPresence();
//    if (i > 0) {
//      val_sum += i;
//      counter ++;  
//    }
//
//    if (counter == 5) {
//      break;
//    }    
//  }
//
//  if (val_sum > 600) {
//    digitalWrite(6, HIGH);
//  } 
//  else {
//    digitalWrite(6, LOW);    
//  }
//}
boolean isObstacle() {
  digitalWrite(PIN_SENS_DIOD, HIGH);    // зажигаем
//  digitalWrite(10, HIGH);
  delay(2); 
  val = analogRead(PIN_SENS);    // считываем значение с фототранзистора
//  int val2 = analogRead(1);
//  int val3 = analogRead(2);
//  int val4 = analogRead(3);

  digitalWrite(PIN_SENS_DIOD, LOW);     // гасим
//  digitalWrite(10, LOW);     // гасим
  delay(2);   
  val = val - analogRead(PIN_SENS);  // считываем значение с фототранзистора
//  val2 = val2 - analogRead(1);
//  val3 = val3 - analogRead(2);  // считываем значение с фототранзистора
//  val4 = val4 - analogRead(3);
  //  int updown = val2 - val3;
  //  int leftright = val - val4;

//  val = (val + val2 + val3 + val4)/4;
//  val = (val + val2)/2;

  if (val > 450) { // smth behind the sensor, 10-15 sm approximatly
    digitalWrite(PIN_STATUS, HIGH);
    return true;
  }
  else {//if (val > 10 && val < 180){
//    command("00001");
    digitalWrite(PIN_STATUS, LOW);
   return false;
  }
  
}




