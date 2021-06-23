package com.example.androidflightcontroller.views

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.example.androidflightcontroller.R
import com.example.androidflightcontroller.viewmodels.ControllerViewModel

class MainActivity : AppCompatActivity() {
    private var vm = ControllerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.connect_button).setOnClickListener { connectByInput() }
        val messageText = findViewById<TextView>(R.id.message)
        findViewById<Button>(R.id.disconnect_button).setOnClickListener {
            try{
                    vm.disconnect()
                    messageText.text = getString(R.string.disconnectSuccess)
                    messageText.setTextColor(Color.BLACK)
                    messageText.visibility = View.VISIBLE
                    // make text of connection
            }
                catch(e: Exception){
                    messageText.text = getString(R.string.disconnectError)
                    messageText.setTextColor(Color.RED)
                    messageText.visibility = View.VISIBLE
                }
        }

        val joystick: Joystick = findViewById(R.id.joystick)
        joystick.onChange = { aileron: Float, elevator: Float ->
            vm.setAileron(aileron)
            vm.setElevator(elevator)
        }

        val seekRudder = findViewById<SeekBar>(R.id.rudder_bar)
        seekRudder?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                val progAbs = progress.toFloat()
                //vm.setThrottle(seekBar.progress.toFloat())
                vm.setRudder((progAbs - 50) / 50)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    val progAbs = seekBar.progress.toFloat()
                    vm.setRudder((progAbs - 50)/50)
                }
            }
        })

        val seekThrottle = findViewById<SeekBar>(R.id.throttle_bar)
        seekThrottle?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                val progAbs = progress.toFloat()
                //vm.setThrottle(seekBar.progress.toFloat())
                vm.setThrottle(progAbs/100)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    val progAbs = seekBar.progress.toFloat()
                    //vm.setThrottle(seekBar.progress.toFloat())
                    vm.setThrottle(progAbs/100)
                }
            }
        })
    }

    private fun connectByInput() {
        val ipEditBox = findViewById<EditText>(R.id.ip_box)
        val ip = ipEditBox.text.toString()
        val portEditBox = findViewById<EditText>(R.id.port_box)
        val port = portEditBox.text.toString()
        val messageText = findViewById<TextView>(R.id.message)
        //try{
            vm.connect(ip, port)
            Thread.sleep(1500)
            if (vm.isConnected()){
            messageText.text = getString(R.string.connectSuccess)
            messageText.setTextColor(Color.GREEN)
            messageText.visibility = View.VISIBLE
            // make text of connection
        }
        //catch(e: Exception){
        else{
            //e.printStackTrace()
            messageText.text = getString(R.string.connectError)
            messageText.setTextColor(Color.RED)
            messageText.visibility = View.VISIBLE
        }
    }



}