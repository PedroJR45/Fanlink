package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*

class vincular : AppCompatActivity() {

    // Declaración de variables
    private var mBtAdapter: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private var dispositivoSeleccionado: BluetoothDevice? = null
    private var myConexionBT: ConnectedThread? = null
    private val mNameDevices = ArrayList<String>()
    private var deviceAdapter: ArrayAdapter<String>? = null
    private var someActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    companion object {
        private const val TAG = "VincularActivity"
        // UUID para el módulo Bluetooth (RFCOMM genérico)
        private val BT_MODULE_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        // Códigos de solicitud
        const val REQUEST_ENABLE_BT = 1
        const val REQUEST_BLUETOOTH_CONNECT_PERMISSION = 3
        const val REQUEST_FINE_LOCATION_PERMISSION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vincular)

        // Inicializar el ActivityResultLauncher para el resultado de la activación de Bluetooth
        someActivityResultLauncher = registerForActivityResult(
            StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == REQUEST_ENABLE_BT) {
                    Log.d(TAG, "Bluetooth activado")
                }
            }
        )

        // Configurar OnClickListener para el botón de búsqueda de dispositivos vinculados
        findViewById<View>(R.id.IdBtnBuscar).setOnClickListener {
            dispositivosVinculados()
        }

        // Configurar OnClickListener para el botón de conexión
        findViewById<View>(R.id.IdBtnConectar).setOnClickListener {
            conectarDispositivoBT()
        }

        // Configurar OnClickListener para el botón de desconexión
        findViewById<View>(R.id.IdBtnDesconectar).setOnClickListener {
            if (btSocket != null) {
                try {
                    btSocket!!.close()
                } catch (e: IOException) {
                    Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }


        // Configurar adaptador y listener para el Spinner de dispositivos encontrados
        deviceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mNameDevices)
        deviceAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val IdDisEncontrados = findViewById<Spinner>(R.id.IdDisEncontrados)
        IdDisEncontrados.adapter = deviceAdapter
        IdDisEncontrados.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                dispositivoSeleccionado = getBluetoothDeviceByName(mNameDevices[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                dispositivoSeleccionado = null
            }
        }

        // Configurar OnClickListener para el botón 'button2'
        findViewById<View>(R.id.button2).setOnClickListener {
            myConexionBT?.write('1')
            Toast.makeText(baseContext, "Comando enviado", Toast.LENGTH_SHORT).show()
        }

        // Solicitar permisos necesarios
        requestBluetoothConnectPermission()
        requestLocationPermission()
    }

    // Método para solicitar permiso de ubicación
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FINE_LOCATION_PERMISSION
        )
    }

    // Método para solicitar permiso de conexión Bluetooth
    private fun requestBluetoothConnectPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH),
            REQUEST_BLUETOOTH_CONNECT_PERMISSION
        )
    }

    // Método para obtener un dispositivo Bluetooth emparejado por su nombre
    private fun getBluetoothDeviceByName(name: String): BluetoothDevice? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Permiso de Bluetooth no otorgado")
            return null
        }
        val pairedDevices = BluetoothAdapter.getDefaultAdapter()?.bondedDevices
        pairedDevices?.forEach { device ->
            if (device.name == name) {
                return device
            }
        }
        return null
    }

    // Método para listar dispositivos Bluetooth emparejados
    private fun dispositivosVinculados() {
        // Obtener el adaptador Bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBtAdapter == null) {
            showToast("Bluetooth no disponible en este dispositivo.")
            finish()
            return
        }

        // Verificar si el Bluetooth está activado
        if (!mBtAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            someActivityResultLauncher?.launch(enableBtIntent)
            return
        }

        // Obtener dispositivos Bluetooth emparejados
        val pairedDevices = mBtAdapter?.bondedDevices
        pairedDevices?.let {
            if (it.isNotEmpty()) {
                it.forEach { device ->
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Solicitar permisos si es necesario
                        return
                    }
                    mNameDevices.add(device.name)
                }
                deviceAdapter?.notifyDataSetChanged()
            } else {
                showToast("No hay dispositivos Bluetooth emparejados.")
            }
        }
    }

    // Método para conectar con un dispositivo Bluetooth

    private fun requestBluetoothConnectPermissions() {
        // Verificar si el permiso ya está otorgado
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si el permiso no está otorgado, solicitarlo al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_BLUETOOTH_CONNECT_PERMISSION
            )
        }
    }

    private fun conectarDispositivoBT() {
        // Verificar si se ha seleccionado un dispositivo Bluetooth
        if (dispositivoSeleccionado == null) {
            showToast("Selecciona un dispositivo Bluetooth.")
            return
        }

        try {
            // Verificar permisos de conexión Bluetooth
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicitar permiso al usuario si no está otorgado
                requestBluetoothConnectPermissions()
                return
            }

            // Crear socket Bluetooth y conectar
            btSocket = dispositivoSeleccionado!!.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
            btSocket!!.connect()

            // Iniciar hilo de comunicación Bluetooth
            myConexionBT = ConnectedThread(btSocket!!)
            myConexionBT?.start()


            showToast("Conexión exitosa.")
        } catch (e: IOException) {

            showToast("Error al conectar con el dispositivo.")
        }
    }
    // Clase interna para manejar la comunicación Bluetooth
    private inner class ConnectedThread(socket: BluetoothSocket) : Thread() {
        private val mmOutStream: OutputStream?

        init {
            var tmpOut: OutputStream? = null
            try {
                tmpOut = socket.outputStream
            } catch (e: IOException) {
                showToast("Error al crear el flujo de datos.")
            }
            mmOutStream = tmpOut
        }

        // Método para escribir datos en el socket Bluetooth
        fun write(input: Char) {
            try {
                mmOutStream?.write(input.code.toByte().toInt())
            } catch (e: IOException) {
                showToast("La conexión falló")
                finish()
            }
        }
    }

    // Método para mostrar un mensaje Toast
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}

