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
    private lateinit var messageText: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageText = findViewById<TextView>(R.id.message)

        findViewById<Button>(R.id.connect_button).setOnClickListener {
            if (isValidInput()){
                connectByInput()
            }
        }
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

    private fun setMessageOn(messageText: TextView, text: String, color: Int){
        messageText.text = text
        messageText.setTextColor(color)
        messageText.visibility = View.VISIBLE
    }

    private fun isValidInput(): Boolean{
        val ipEditBox = findViewById<EditText>(R.id.ip_box)
        val ip = ipEditBox.text.toString()
        val portEditBox = findViewById<EditText>(R.id.port_box)
        val port = portEditBox.text.toString()

        if (areFieldsNotEmpty(ip, port))
            if (isValidIPAddress(ip))
                return true
        return false
    }
    private fun areFieldsNotEmpty(ip: String, port: String): Boolean{
        if (ip.isEmpty() && port.isEmpty()){
            setMessageOn(messageText, getString(R.string.emptyFields), Color.RED)
            return false
        }
        if(ip.isEmpty()){
            setMessageOn(messageText, getString(R.string.emptyIPField), Color.RED)
            return false
        }
        if(port.isEmpty()){
            setMessageOn(messageText, getString(R.string.emptyPortField), Color.RED)
            return false
        }
        return true
    }
    private fun connectByInput() {
        val ipEditBox = findViewById<EditText>(R.id.ip_box)
        val ip = ipEditBox.text.toString()
        val portEditBox = findViewById<EditText>(R.id.port_box)
        val port = portEditBox.text.toString()
        val messageText = findViewById<TextView>(R.id.message)

        val valid = areFieldsNotEmpty(ip, port)
        if (!valid)
            return

        // got ip and port
        vm.connect(ip, port)
        Thread.sleep(1000)
        if (vm.isConnected()){
            setMessageOn(messageText, getString(R.string.connectSuccess), Color.GREEN)
            /*messageText.text = getString(R.string.connectSuccess)
            messageText.setTextColor(Color.GREEN)
            messageText.visibility = View.VISIBLE*/
            // make text of connection
        }
        //catch(e: Exception){
        else{
            setMessageOn(messageText, getString(R.string.connectError), Color.RED)
            //e.printStackTrace()
            /*messageText.text = getString(R.string.connectError)
            messageText.setTextColor(Color.RED)
            messageText.visibility = View.VISIBLE*/
        }
    }

    fun isValidIPAddress(ip: String?): Boolean {

        // Regex for digit from 0 to 255.
        val zeroTo255 = ("(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])")

        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        val regex = (zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255)

        // Compile the ReGex
        val p: Pattern = Pattern.compile(regex)

        // If the IP address is empty
        // return false
        if (ip == null) {
            return false
        }

        // Pattern class contains matcher() method
        // to find matching between given IP address
        // and regular expression.
        val m: Matcher = p.matcher(ip)

        // Return if the IP address
        // matched the ReGex
        //return m.matches()
        if (m.matches())
            return true
        setMessageOn(messageText, getString(R.string.invalidIP), Color.RED)
        return false
    }

}