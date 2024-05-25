#include <SoftwareSerial.h>

// Define los pines de control para la velocidad del ventilador
const int ventiladorPin = 4;

// Inicializa el objeto de comunicación serial para Bluetooth
SoftwareSerial bluetoothSerial(10, 11); // RX, TX

void setup() {
  // Inicializa la comunicación serial para la depuración
  Serial.begin(9600);
  // Inicializa la comunicación serial para Bluetooth
  bluetoothSerial.begin(9600);

  // Configura los pines de control para la velocidad como salidas
  pinMode(ventiladorPin, OUTPUT);
}

void loop() {
  // Espera comandos desde el dispositivo emparejado a través de Bluetooth
  if (bluetoothSerial.available() > 0) {
     Serial.println("CONECTADO");
    // Lee el comando enviado desde el dispositivo Bluetooth
    String comando = bluetoothSerial.readString();
    // Imprime el comando recibido para depuración
    Serial.println("Comando recibido: " + comando);

    // Procesa los comandos recibidos
    if (comando == "1") {
      encenderVentilador();
    } else if (comando == "0") {
      apagarVentilador();
    }
  }
}

void encenderVentilador() {
  // Enciende el pin correspondiente a la velocidad baja del ventilador
  digitalWrite(ventiladorPin, HIGH);
}

void apagarVentilador() {
  // Apaga todos los pines de control del ventilador
  digitalWrite(ventiladorPin, LOW);
}
