package com.example.fanlynk

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class vincular : AppCompatActivity() {
    private lateinit var listViewDevices: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var conn: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vincular)
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

            if (arrayAdapter.count > 0) {
                val firstDeviceName = arrayAdapter.getItem(0)
                connectToDevice(firstDeviceName)
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

    private fun connectToDevice(deviceName: String?) {
        // Implementa la lógica para conectarte al dispositivo seleccionado aquí
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
}
