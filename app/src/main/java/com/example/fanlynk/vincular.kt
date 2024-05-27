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
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class vincular : AppCompatActivity() {
    private var mBtAdapter: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private var dispositivoSeleccionado: BluetoothDevice? = null
    private var myConexionBT: ConnectedThread? = null
    private val mNameDevices = ArrayList<String>()
    private var deviceAdapter: ArrayAdapter<String>? = null

    private var someActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    companion object {
        private const val TAG = "VincularActivity"
        private val BT_MODULE_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        const val REQUEST_ENABLE_BT = 1
        const val REQUEST_BLUETOOTH_CONNECT_PERMISSION = 3
        const val REQUEST_FINE_LOCATION_PERMISSION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vincular)

        someActivityResultLauncher = registerForActivityResult(
            StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == REQUEST_ENABLE_BT) {
                    Log.d(TAG, "Bluetooth activado")
                }
            }
        )

        findViewById<View>(R.id.IdBtnBuscar).setOnClickListener {
            dispositivosVinculados()
        }

        findViewById<View>(R.id.IdBtnConectar).setOnClickListener {
            conectarDispositivoBT()
        }
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

        requestBluetoothConnectPermission()
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FINE_LOCATION_PERMISSION
        )
    }

    private fun requestBluetoothConnectPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH),
            REQUEST_BLUETOOTH_CONNECT_PERMISSION
        )
    }

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

    private fun dispositivosVinculados() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBtAdapter == null) {
            showToast("Bluetooth no disponible en este dispositivo.")
            finish()
            return
        }

        if (!mBtAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            someActivityResultLauncher?.launch(enableBtIntent)
            return
        }

        val pairedDevices = mBtAdapter?.bondedDevices
        pairedDevices?.let {
            if (it.isNotEmpty()) {
                it.forEach { device ->
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
                    mNameDevices.add(device.name)
                }
                deviceAdapter?.notifyDataSetChanged()
            } else {
                showToast("No hay dispositivos Bluetooth emparejados.")
            }
        }
    }

    private fun conectarDispositivoBT() {
        if (dispositivoSeleccionado == null) {
            showToast("Selecciona un dispositivo Bluetooth.")
            return
        }

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
            btSocket = dispositivoSeleccionado!!.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
            btSocket!!.connect()
            myConexionBT = ConnectedThread(btSocket!!)
            myConexionBT?.start()
            showToast("Conexión exitosa.")
        } catch (e: IOException) {
            showToast("Error al conectar con el dispositivo.")
        }
    }

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

        fun write(input: Char) {
            try {
                mmOutStream?.write(input.code.toByte().toInt())
            } catch (e: IOException) {
                showToast("La conexión falló")
                finish()
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
