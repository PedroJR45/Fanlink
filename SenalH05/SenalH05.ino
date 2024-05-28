#include <SoftwareSerial.h>

#define   LED   9          //Pin para el LED

SoftwareSerial ModuloHC05 (10, 11);  //pin RX, pin TX

void setup() {

 Serial.begin(9600);        //Inicializa puerto serie por hard
 ModuloHC05.begin(9600);    //Inicializa puerto serie por soft

 pinMode (LED, OUTPUT);      //Salida

 digitalWrite (LED, 0);
}

void loop() {
 char dato;
 if (ModuloHC05.available()) {              //Llega algo por bluetooth?
  dato=ModuloHC05.read();                   //Leer lo que llegó
  Serial.write(dato);                       //Sacarlo a la terminal
  if (dato=='1') digitalWrite (LED, 1);     //Si es "1", prende el LED
  if (dato=='0') digitalWrite (LED, 0);     //Si es "0", apaga el LED
 
 }
           

}