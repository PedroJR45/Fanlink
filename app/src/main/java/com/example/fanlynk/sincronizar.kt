package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Sincronizar : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var listaDispositivos: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sincronizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listaDispositivos = findViewById(R.id.listaDispositivos)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        findViewById<TextView>(R.id.textView).setOnClickListener {
            val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            startActivity(intent)
        }

        mostrarDispositivoConectado()
    }

    private fun mostrarDispositivoConectado() {
        val dispositivoConectado: BluetoothDevice? = bluetoothAdapter.bondedDevices.firstOrNull {
            it.bondState == BluetoothDevice.BOND_BONDED && it.isConnected()
        }

        val dispositivos = mutableListOf<String>()
        dispositivoConectado?.let {
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
            dispositivos.add(it.name ?: "Dispositivo Desconocido")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dispositivos)
        listaDispositivos.adapter = adapter
    }

    private fun BluetoothDevice.isConnected(): Boolean {
        return try {
            val method = this.javaClass.getMethod("isConnected")
            method.invoke(this) as Boolean
        } catch (e: Exception) {
            false
        }
    }
}