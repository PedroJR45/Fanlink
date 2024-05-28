package com.example.fanlynk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.io.OutputStream
import java.util.ArrayList
import java.util.UUID

class Manejar_dispositivo : AppCompatActivity() {

    private lateinit var mArrayAdapter: ArrayAdapter<String>
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private val btDeviceArray = ArrayList<BluetoothDevice>()
    private lateinit var connectAsyncTask: ConnectAsyncTask

    private var mBTAdapter: BluetoothAdapter? = null
    private val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var btn1: Button
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manejar_dispositivo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mBTAdapter = BluetoothAdapter.getDefaultAdapter()

        mArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView = findViewById(R.id.listView1)
        listView.adapter = mArrayAdapter

        connectAsyncTask = ConnectAsyncTask()

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBluetoothAdapter == null) {
            Toast.makeText(applicationContext, "Not support bluetooth", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (mBluetoothAdapter != null && !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        val pairedDevices: Set<BluetoothDevice> = mBluetoothAdapter?.bondedDevices ?: emptySet()
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
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
                mArrayAdapter.add("${device.name}\n${device.address}")
                btDeviceArray.add(device)
            }
        }

        btn1 = findViewById(R.id.button)
        btn1.setOnClickListener {
            Toast.makeText(applicationContext, "Conexion en proceso", Toast.LENGTH_SHORT).show()
            var mmOutStream: OutputStream? = null
            try {
                if (btSocket?.isConnected == true) {
                    mmOutStream = btSocket!!.outputStream
                    mmOutStream.write("1".toByteArray())
                }
            } catch (e: IOException) {
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val device = btDeviceArray[position]
            connectAsyncTask.execute(device)
            Toast.makeText(applicationContext, "Conectado", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class ConnectAsyncTask : AsyncTask<BluetoothDevice, Int, BluetoothSocket>() {
        private var mmSocket: BluetoothSocket? = null
        private var mmDevice: BluetoothDevice? = null

        override fun doInBackground(vararg devices: BluetoothDevice): BluetoothSocket? {
            mmDevice = devices[0]
            try {
                val mmUUID = "00001101-0000-1000-8000-00805F9B34FB"
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
                mmSocket = mmDevice?.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID))
                mmSocket?.connect()
            } catch (e: Exception) {
            }
            return mmSocket
        }

        override fun onPostExecute(result: BluetoothSocket?) {
            btSocket = result
        }
    }
}
