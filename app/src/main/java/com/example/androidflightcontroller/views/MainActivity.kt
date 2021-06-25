package com.example.androidflightcontroller.views

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidflightcontroller.R
import com.example.androidflightcontroller.viewmodels.ControllerViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private var vm = ControllerViewModel()
    private lateinit var ipEditText: EditText
    private lateinit var portEditText: EditText
    private lateinit var messageText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageText = findViewById<TextView>(R.id.message)
        ipEditText = findViewById<EditText>(R.id.ip_box)
        portEditText = findViewById<EditText>(R.id.port_box)

        // Add connect button click listener
        findViewById<Button>(R.id.connect_button).setOnClickListener {
            if (vm.isConnected()){
                setMessageOn(getString(R.string.alreadyConnected), Color.BLACK)
            }
            else {
                // extract strings from EditTexts
                val ip = ipEditText.text.toString()
                val port = portEditText.text.toString()
                // if ip and port are valid, connect
                if (isValidInput(ip, port)) {
                    connectByInput(ip, port)
                }
            }
        }

        // Add disconnect button click listener
        findViewById<Button>(R.id.disconnect_button).setOnClickListener {
            // need to be changed
            try{
                if (vm.isConnected()) {
                    vm.disconnect()
                    setMessageOn(getString(R.string.disconnectSuccess), Color.BLACK)
                }
                else {
                    setMessageOn(getString(R.string.alreadyDisconnected), Color.BLACK)
                }
            }
                catch(e: Exception){
                    setMessageOn(getString(R.string.disconnectError), Color.RED)
                }
        }

        val joystick: Joystick = findViewById(R.id.joystick)
        joystick.onChange = { aileron: Float, elevator: Float ->
            vm.setAileron(aileron)
            vm.setElevator(elevator)
        }

        // Add rudder SeekBar progress change listener
        val seekRudder = findViewById<SeekBar>(R.id.rudder_bar)
        seekRudder?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    val progAbs = progress.toFloat()
                    // Set value from [0, 100] to [-1, 1]
                    vm.setRudder((progAbs - 50) / 50)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        // Add throttle SeekBar progress change listener
        val seekThrottle = findViewById<SeekBar>(R.id.throttle_bar)
        seekThrottle?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seek: SeekBar,
                                               progress: Int, fromUser: Boolean) {
                    val progAbs = progress.toFloat()
                    // Set value from [0, 100] to [0, 1]
                    vm.setThrottle(progAbs/100)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    // Make message TextView visible with the given text and color
    private fun setMessageOn(text: String, color: Int){
        messageText.text = text
        messageText.setTextColor(color)
        messageText.visibility = View.VISIBLE
    }

    // Check if port and ip input are valid (not empty, and ip regex check)
    private fun isValidInput(ip: String, port: String): Boolean{
        if (areFieldsNotEmpty(ip, port))
            if (isValidIPAddress(ip))
                return true
        return false
    }

    // Return true if the given Strings are not empty
    private fun areFieldsNotEmpty(ip: String, port: String): Boolean{
        if (ip.isEmpty() && port.isEmpty()){
            setMessageOn(getString(R.string.emptyFields), Color.RED)
            return false
        }
        if(ip.isEmpty()){
            setMessageOn(getString(R.string.emptyIPField), Color.RED)
            return false
        }
        if(port.isEmpty()){
            setMessageOn(getString(R.string.emptyPortField), Color.RED)
            return false
        }
        return true
    }

    // Try connecting to the input ip and port
    private fun connectByInput(ip: String, port: String) {
        // got ip and port
        vm.connect(ip, port)
        // wait to asynchronous connection
        Thread.sleep(500)
        // generate message according to feedback
        if (vm.isConnected()){
            setMessageOn(getString(R.string.connectSuccess), Color.GREEN)
        }
        else{
            setMessageOn(getString(R.string.connectError), Color.RED)
        }
    }

    // Return true if the ip input is matching valid regex
    private fun isValidIPAddress(ip: String?): Boolean {
        // Regex for digit from 0 to 255.
        val zeroTo255 = ("(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])")

        // Regex for a digit from 0 to 255 and followed by a dot, repeat 4 times.
        val regex = (zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255)

        // Compile the ReGex
        val p: Pattern = Pattern.compile(regex)

        // If the IP address is empty return false
        if (ip == null) {
            return false
        }
        // use Matcher to find matching between given IP address and regular expression.
        val m: Matcher = p.matcher(ip)

        // Return if the IP address matched the regex
        if (m.matches())
            return true
        // If IP address didn't match regex, set invalidation message and return false
        setMessageOn(getString(R.string.invalidIP), Color.RED)
        return false
    }

}