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
import androidx.appcompat.app.AlertDialog


class Manejar_dispositivo : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private val deviceAddress = "00:21:13:00:D7:2E"
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para el HC-05

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

        val imgBluetoothSettings: ImageView = findViewById(R.id.imgAgregar)
        imgBluetoothSettings.setOnClickListener {
            val intent = Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.imgVincular).setOnClickListener {
            val intent = Intent(this, vincular::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            val intent = Intent(this, Iniciar_session::class.java)
            startActivity(intent)
        }

        val buttons = listOf<Button>(
            findViewById(R.id.button),
        )


        buttons.forEach { button ->
            button.setOnClickListener {
                if (bluetoothSocket != null && bluetoothSocket!!.isConnected) {
                    sendBluetoothSignal("TOGGLE")
                } else {
                    Toast.makeText(this, "No hay ningún dispositivo conectado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<ImageView>(R.id.imageEncendidoApagado).setOnClickListener {
            if (bluetoothSocket != null && bluetoothSocket!!.isConnected) {
                sendBluetoothSignal("TOGGLE")
            } else {
                Toast.makeText(this, "No hay ningún dispositivo conectado", Toast.LENGTH_SHORT).show()
            }
        }

        connectToBluetoothDevice()
    }

    private fun connectToBluetoothDevice() {
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        try {
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
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            Toast.makeText(this, "Conectado a HC-05", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al conectar con HC-05", Toast.LENGTH_SHORT).show()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cerrar aplicación")
        builder.setMessage("¿Estás seguro de que quieres cerrar la aplicación?")
        builder.setPositiveButton("Sí") { dialog, _ ->
            finishAffinity() // Cierra la actividad actual y todas las actividades asociadas
            super.onBackPressed() // Llama al método onBackPressed de la clase base después de cerrar la actividad
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Cierra el diálogo sin hacer nada
        }
        val dialog = builder.create()
        dialog.show()
    }



}