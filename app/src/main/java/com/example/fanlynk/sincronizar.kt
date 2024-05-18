package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class sincronizar : AppCompatActivity() {
    private lateinit var listViewDevices: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var conn: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sincronizar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listViewDevices = findViewById(R.id.listViewDevices)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listViewDevices.adapter = arrayAdapter
        listViewDevices.setOnItemClickListener { _, _, position, _ ->
            val deviceName = arrayAdapter.getItem(position)
            connectToDevice(deviceName)
        }

        try {
            Class.forName("com.mysql.jdbc.Driver")
            conn = DriverManager.getConnection("localhost/fanlink", "usuario", "12345")
            loadDevicesFromDatabase()

            // Iniciar la detección de dispositivos Bluetooth
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null) {
                if (!bluetoothAdapter.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
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
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                } else {
                    discoverDevices(bluetoothAdapter)
                }
            } else {
                Toast.makeText(this, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al conectar a la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDevicesFromDatabase() {
        val query = "SELECT nombre_dispositivo FROM dispositivo"
        val statement: PreparedStatement = conn.prepareStatement(query)
        val resultSet: ResultSet = statement.executeQuery()
        while (resultSet.next()) {
            val deviceName = resultSet.getString("nombre_dispositivo")
            arrayAdapter.add(deviceName)
        }
    }

    private fun discoverDevices(bluetoothAdapter: BluetoothAdapter) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
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
        bluetoothAdapter.startDiscovery()
    }

    private fun connectToDevice(deviceName: String?) {

        Toast.makeText(this, "Conectando a $deviceName", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            conn.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
