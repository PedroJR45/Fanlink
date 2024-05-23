package com.example.fanlynk


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class vincular : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var listDispo: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vincular)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
//        listDispo = findViewById(R.id.listDispo)
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//
//        findViewById<TextView>(R.id.textView3).setOnClickListener {
//            val intent = Intent(this, Manejar_dispositivo::class.java)
//            startActivity(intent)
//            finish() // Opcional: cierra la actividad actual si no deseas que el usuario vuelva atrás
//        }
//
//        mostrarDispositivosVinculados()
//    }
//
//    private fun mostrarDispositivosVinculados() {
//        val dispositivosVinculados: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
//        val nombresDispositivos = dispositivosVinculados.map { if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//            it.name ?: "Dispositivo Desconocido" }
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresDispositivos)
//        listDispo.adapter = adapter
//    }

