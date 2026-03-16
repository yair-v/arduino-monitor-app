package com.arduinosensormonitor

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.io.InputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        statusText = TextView(this)
        statusText.text = "Arduino Sensor Monitor"
        setContentView(statusText)

        connectBluetooth()
    }

    private fun connectBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val device: BluetoothDevice =
            bluetoothAdapter.bondedDevices.first()

        val socket: BluetoothSocket =
            device.createRfcommSocketToServiceRecord(uuid)

        socket.connect()

        val inputStream: InputStream = socket.inputStream

        Thread {
            while (true) {
                val buffer = ByteArray(1024)
                val bytes = inputStream.read(buffer)
                val data = String(buffer, 0, bytes)

                runOnUiThread {
                    statusText.text = data
                }
            }
        }.start()
    }
}
