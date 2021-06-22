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

    private fun startFlying(){
        Thread{
            while(!stop){
                dispatchQueue.take().run()
            }
        }.start()
    }
    init{
        startFlying()
    }

    fun connect(ipAdd: String, portNum: Int) {
        if (stop){
            stop = false
            startFlying()
        }
        dispatchQueue.put(Runnable {
            fg = Socket(ipAdd, portNum)
            out = PrintWriter(fg!!.getOutputStream(), true)
            isConnected = true
        })
    }
        fun setAileron(data: Float){
            if (isConnected == true)
                sendData("flight/aileron", data)
        }
        fun setRudder(data: Float){
            if (isConnected == true)
                sendData("flight/rudder", data)
        }
        fun setElevator(data: Float){
            if (isConnected == true)
                sendData("flight/elevator", data)
        }
        fun setThrottle(data: Float){
            if (isConnected == true)
                sendData("engines/current-engine/throttle", data)
        }

        private fun sendData(name: String, value: Float){
            dispatchQueue.put(Runnable {
                println("set /controls/$name $value\r\n")
                out?.print("set /controls/$name $value \r\n")
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