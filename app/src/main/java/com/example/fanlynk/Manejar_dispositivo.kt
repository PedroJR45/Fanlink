package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.util.*

class Manejar_dispositivo : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private val deviceAddress = "00:21:13:00:D7:2E"
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para el HC-05
    private var lastSignal = "0"
    private var isConnected = false

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
            if (!isConnected) {
                connectToBluetoothDevice()
            } else {
                toggleAndSendBluetoothSignal()
            }
        }

        // Configuración del botón de logout
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            logout()
        }

        // Configuración del ImageView para enviar señal
        findViewById<ImageView>(R.id.imageEncendidoApagado).setOnClickListener {
            if (!isConnected) {
                connectToBluetoothDevice()
            } else {
                toggleAndSendBluetoothSignal()
            }
        }
    }

    private fun connectToBluetoothDevice() {
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )
                return
            }
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            isConnected = true
            Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            isConnected = false
            Toast.makeText(this, "Error al conectar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleAndSendBluetoothSignal() {
        if (!isConnected) {
            Toast.makeText(this, "Dispositivo no conectado", Toast.LENGTH_SHORT).show()
            return
        }
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
