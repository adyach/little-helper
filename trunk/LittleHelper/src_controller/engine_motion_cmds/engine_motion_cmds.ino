char buffer[6];
byte commandSize = 5;

byte leftEngine = 7;
byte rightEngine = 8;

byte fullStop = 0;
byte slowStart = 70;
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

void setup() {
  pinMode(6, OUTPUT);
  Serial.begin(9600);
  Serial.setTimeout(1000);
}

void loop() {
  val_sum = 0;
  int counter = 0;
  while(1){
    int i = detectPresence();
    if (i > 0) {
      val_sum += i;
      counter ++;  
    }

    if (counter == 5) {
      break;
    }    
  }
    
  if (val_sum > 600) {
    digitalWrite(6, HIGH);
  } else {
    digitalWrite(6, LOW);    
  }
}

void sendResponse(String response) {
  Serial.print(response);
  Serial.flush();
}

void serialEvent() {
  if (Serial.available() > 0) {
    int n = Serial.readBytesUntil('\n', buffer, commandSize);
    if (n >= 0) {
      // set end of the string
      buffer[n] = '\0';
      recievedCmd = buffer;

      motion(recievedCmd);

      // send response to server
      sendResponse("cmd " + recievedCmd);
    }
  }
}

void motion(String recievedCmd) {
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

int detectPresence() {
  digitalWrite(9, HIGH);    // зажигаем
  digitalWrite(10, HIGH);
  delay(2); 
  int val = analogRead(0);    // считываем значение с фототранзистора
  int val2 = analogRead(1);
  int val3 = analogRead(2);
  int val4 = analogRead(3);

  digitalWrite(9, LOW);     // гасим
  digitalWrite(10, LOW);     // гасим
  delay(2);   
  val = val - analogRead(0);  // считываем значение с фототранзистора
  val2 = val2 - analogRead(1);
  val3 = val3 - analogRead(2);  // считываем значение с фототранзистора
  val4 = val4 - analogRead(3);
  //  int updown = val2 - val3;
  //  int leftright = val - val4;

  val = (val + val2 + val3 + val4)/4;
  return val;
}


