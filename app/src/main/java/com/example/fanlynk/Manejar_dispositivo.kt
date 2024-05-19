package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.util.UUID

class Manejar_dispositivo : AppCompatActivity() {
    private lateinit var bluetoothManager: BluetoothManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manejar_dispositivo)

        bluetoothManager = BluetoothManager(this)

        val button = findViewById<ImageView>(R.id.imgAgregar)
        button.setOnClickListener {
            handleBluetoothConnection()
        }

        val imageEncendidoApagado = findViewById<ImageView>(R.id.imageEncendidoApagado)
        imageEncendidoApagado.setOnClickListener {
            handleBluetoothConnection()
        }

        val button2 = findViewById<ImageView>(R.id.imgVincular)
        button2.setOnClickListener {
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleBluetoothConnection() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothManager.getPairedDevices()
        if (pairedDevices.isNullOrEmpty()) {
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        } else {
            val device = pairedDevices.first()
            if (bluetoothManager.connectToDevice(device)) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                Toast.makeText(this, "Conectado a ${device.name}", Toast.LENGTH_SHORT).show()
                bluetoothManager.sendSignal("Conn")
            } else {
                Toast.makeText(this, "Error al conectar con el dispositivo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class BluetoothManager(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null

    fun getPairedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }

    fun connectToDevice(device: BluetoothDevice): Boolean {
        return try {
            val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para el servicio SPP
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun sendSignal(signal: String) {
        try {
            bluetoothSocket?.outputStream?.write(signal.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al enviar la señal", Toast.LENGTH_SHORT).show()
        }
    }
}
