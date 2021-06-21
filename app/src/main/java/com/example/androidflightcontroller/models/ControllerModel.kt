package com.example.androidflightcontroller.models

import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

class ControllerModel {
        private var fg: Socket? = null
        private var out: PrintWriter? = null
        private var isConnected: Boolean? = false
        private var stop: Boolean = false
        private var dispatchQueue = LinkedBlockingQueue<Runnable>()

    init{
        Thread{
            while(!stop){
                dispatchQueue.take().run()
            }
        }.start()
    }
    fun connect(ipAdd: String, portNum: Int) {
        dispatchQueue.put(Runnable {
            fg = Socket(ipAdd, portNum)
            isConnected = true
            out = PrintWriter(fg!!.getOutputStream(), true)
        })
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
            dispatchQueue.put(Runnable {
                out?.print("set /controls/flight/$name $value\r\n")
                out?.flush()
            })
        }

        fun disconnect() {
            dispatchQueue.put(Runnable {
                stop = true
                isConnected = false
                out?.close()
                fg?.close()
            })
        }
}