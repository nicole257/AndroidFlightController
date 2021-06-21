package com.example.androidflightcontroller.models

import java.io.PrintWriter
import java.net.Socket

class ControllerModel {
        private var fg: Socket? = null
        private var out: PrintWriter? = null
        private var isConnected: Boolean? = false

        fun connect(ipAdd: String, portNum: Int){
            var thread = Thread {
                // try?
                fg = Socket(ipAdd, portNum)
                isConnected = true
                out = PrintWriter(fg!!.getOutputStream(), true)
            }
            thread.start()
            thread.join()
        }

        fun setAileron(data: Float){
            if (isConnected == true)
                sendData("aileron", data)
        }
        fun setRudder(data: Float){
            if (isConnected == true)
                sendData("rudder", data)
        }
        fun setElevator(data: Float){
            if (isConnected == true)
                sendData("elevator", data)
        }
        fun setThrottle(data: Float){
            if (isConnected == true)
                sendData("throttle", data)
        }

        // do this on different thread?
        private fun sendData(name: String, value: Float){
            val thread = Thread {
                out?.print("set /controls/flight/$name $value\r\n")
                out?.flush()
            }
            thread.start()
            thread.join()

        }

        fun disconnect() {
            out?.close()
            fg?.close()
            isConnected = false
        }
}