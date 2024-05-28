package com.example.fanlynk

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class Manejar_dispositivo : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var lastSignal = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manejar_dispositivo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        findViewById<ImageView>(R.id.imgAgregar).setOnClickListener {
            val intent = Intent(this, sincronizar::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imgVincular).setOnClickListener {
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        }

        // Configuración del botón para enviar señal
        val buttonSendSignal = findViewById<Button>(R.id.button)
        buttonSendSignal.setOnClickListener {
            toggleAndSendBluetoothSignal()
        }

        // Configuración del botón de logout
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            logout()
        }

        // Configuración del ImageView para enviar señal
        findViewById<ImageView>(R.id.imageEncendidoApagado).setOnClickListener {
            toggleAndSendBluetoothSignal()
        }
    }

    private fun toggleAndSendBluetoothSignal() {
        lastSignal = if (lastSignal == "0") "1" else "0"
        sendBluetoothSignal(lastSignal)
    }

    private fun sendBluetoothSignal(signal: String) {
        try {
            bluetoothSocket?.outputStream?.write(signal.toByteArray())
            Toast.makeText(this, "Señal enviada: $signal", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al enviar la señal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        // Redirigir a la ventana Iniciar_sesion
        val intent = Intent(this, Iniciar_session::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
