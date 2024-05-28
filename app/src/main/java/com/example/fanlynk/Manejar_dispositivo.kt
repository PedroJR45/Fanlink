package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
    private var lastSignal = "0"
    private val deviceAddress = "00:21:13:00:D7:2E"
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

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

        // Inicializar y conectar el socket Bluetooth
        connectToBluetoothDevice()

        findViewById<ImageView>(R.id.imgAgregar).setOnClickListener {
            val intent = Intent(this, sincronizar::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imgVincular).setOnClickListener {
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        }


        val buttonSendSignal = findViewById<Button>(R.id.button)
        buttonSendSignal.setOnClickListener {
            toggleAndSendBluetoothSignal()
        }

        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            logout()
        }

        findViewById<ImageView>(R.id.imageEncendidoApagado).setOnClickListener {
            toggleAndSendBluetoothSignal()
        }
    }

    private fun connectToBluetoothDevice() {
        try {
            val device: BluetoothDevice = bluetoothAdapter!!.getRemoteDevice(deviceAddress)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            Toast.makeText(this, "Conectado a $deviceAddress", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al conectar el dispositivo Bluetooth", Toast.LENGTH_SHORT).show()
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
