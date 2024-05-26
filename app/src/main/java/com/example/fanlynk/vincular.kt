package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
class vincular : AppCompatActivity() {
    class vincular : AppCompatActivity() {
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    inicializarBluetooth()
                } else {
                    Toast.makeText(this, "Permiso de Bluetooth denegado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        private val REQUEST_BLUETOOTH_PERMISSIONS = 1
        private lateinit var bluetoothAdapter: BluetoothAdapter

        private fun inicializarBluetooth() {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth no está disponible en este dispositivo", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
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
                // El Bluetooth ya está activado, puedes comenzar a buscar dispositivos
                buscarDispositivosBluetooth()
            }
        }

        private fun buscarDispositivosBluetooth() {
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
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
            if (!bluetoothAdapter.startDiscovery()) {
                Toast.makeText(this, "No se pudo iniciar la búsqueda de dispositivos Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }

        private val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    // Aquí puedes manejar el dispositivo Bluetooth encontrado
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            unregisterReceiver(receiver)
        }
    }

//    private lateinit var bluetoothAdapter: BluetoothAdapter
//    private lateinit var dispositivosArrayAdapter: ArrayAdapter<String>
//    private lateinit var listaDispositivos: ListView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_vincular)
//
//        listaDispositivos = findViewById(R.id.listDispo)
//
//        // Verificar y solicitar permisos en tiempo de ejecución
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ),
//                REQUEST_BLUETOOTH_PERMISSIONS
//            )
//        } else {
//            inicializarBluetooth()
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
//            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//                inicializarBluetooth()
//            } else {
//                Toast.makeText(this, "Permiso de Bluetooth denegado", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }
//
//    private fun inicializarBluetooth() {
//        // Inicializar BluetoothAdapter
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//
//        // Verificar si el dispositivo soporta Bluetooth
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Este dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        // Verificar si el Bluetooth está habilitado
//        if (!bluetoothAdapter.isEnabled) {
//            solicitarHabilitarBluetooth()
//        } else {
//            mostrarDispositivosVinculados()
//        }
//    }
//
//    private fun solicitarHabilitarBluetooth() {
//        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == RESULT_OK) {
//                mostrarDispositivosVinculados()
//            } else {
//                Toast.makeText(this, "Bluetooth no habilitado, la aplicación se cerrará", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }
//
//    private fun conectarDispositivo(dispositivo: BluetoothDevice) {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH_CONNECT
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
//                PERMISSION_REQUEST_BLUETOOTH_CONNECT
//            )
//            return
//        }
//
//        val socket: BluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(null)
//
//        try {
//            socket.connect()
//            // Aquí puedes continuar con la lógica de tu aplicación después de conectarte al dispositivo
//            // Por ejemplo, podrías enviar o recibir datos a través del socket Bluetooth
//            // También deberías manejar las excepciones apropiadamente en un entorno de producción
//        } catch (e: IOException) {
//            e.printStackTrace()
//            // Manejar la excepción de conexión aquí
//        }
//    }
//
//    private fun mostrarDispositivosVinculados() {
//        val dispositivosVinculados: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
//        val nombresDispositivos = ArrayList<String>()
//        for (dispositivo in dispositivosVinculados) {
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }
//            nombresDispositivos.add(dispositivo.name)
//        }
//
//        dispositivosArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresDispositivos)
//        listaDispositivos.adapter = dispositivosArrayAdapter
//
//        // Manejar la selección de dispositivo
//        listaDispositivos.setOnItemClickListener { _, _, position, _ ->
//            val dispositivoSeleccionado = dispositivosVinculados.elementAt(position)
//            conectarDispositivo(dispositivoSeleccionado)
//        }
//    }
//
//    companion object {
//        private const val REQUEST_ENABLE_BT = 1
//        private const val REQUEST_BLUETOOTH_PERMISSIONS = 101
//        private const val PERMISSION_REQUEST_BLUETOOTH_CONNECT = 1
//    }

}