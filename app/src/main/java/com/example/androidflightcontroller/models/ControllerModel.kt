package com.example.androidflightcontroller.models

import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

class ControllerModel {
        private var fg: Socket? = null
        private var out: PrintWriter? = null
        private var isConnected: Boolean = false
        private var stop: Boolean = false
        private var dispatchQueue = LinkedBlockingQueue<Runnable>()

    init{ startFlying() }
    private fun startFlying(){
        Thread{
            while(!stop){
                try { dispatchQueue.take().run() } catch(e: Exception){ e.printStackTrace() }
            }
        }.start()
    }
    fun isConnected(): Boolean{
        return this.isConnected
    }
    fun connect(ipAdd: String, portNum: Int) {
        if (stop){
            stop = false
            startFlying()
        }
        // try to connect to socket outside of queue, to get immediate feedback
            //dispatchQueue.put(Runnable {
        Thread {
            try {
                fg = Socket(ipAdd, portNum)
                out = PrintWriter(fg!!.getOutputStream(), true)
                if (fg != null)
                    isConnected = true
            }
            catch (e: Exception){e. printStackTrace()}
        }.start()
        //})
    }
    fun disconnect() {
        dispatchQueue.put(Runnable {
            stop = true
            isConnected = false
            out?.close()
            fg?.close()
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
                try {
                    out?.print("set /controls/$name $value \r\n")
                    out?.flush()
                }
                catch(e: IOException){ e.printStackTrace() }
            })
        }


}