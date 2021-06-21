package com.example.androidflightcontroller.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import com.example.androidflightcontroller.R
import com.example.androidflightcontroller.viewmodels.ControllerViewModel

class MainActivity : AppCompatActivity() {
    private var vm = ControllerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.connect_button).setOnClickListener { connectByInput(it) }

        val seekRudder = findViewById<SeekBar>(R.id.rudder_bar)
        seekRudder?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    vm.setRudder(seekBar.progress.toFloat())
                }
            }
        })

        val seekThrottle = findViewById<SeekBar>(R.id.throttle_bar)
        seekThrottle?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    vm.setThrottle(seekBar.progress.toFloat())
                }
            }
        })
    }

    private fun connectByInput(view: View) {
        val ipEditBox = view.findViewById<EditText>(R.id.ip_box)
        val ip = ipEditBox.text.toString()
        val portEditBox = view.findViewById<EditText>(R.id.port_box)
        val port = portEditBox.text.toString()
        vm.connect(ip, port)
    }

    fun setJoystickData(a: Float, e: Float){
        vm.setAileron(a)
        vm.setElevator(e)
    }
    fun setRudder(){
        val RudderBar = findViewById<SeekBar>(R.id.rudder_bar)
        val prog = RudderBar.progress
        // maybe should make rudder into values between 1 to -1
        vm.setRudder(prog.toFloat())
    }

    fun setThrottle(){
        val ThrottleBar = findViewById<SeekBar>(R.id.rudder_bar)
        val prog = ThrottleBar.progress
        // maybe should make rudder into values between 1 to -1
        vm.setRudder(prog.toFloat())
    }
}