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

    /*
    * function starts a thread that will run in background
    *  and use the blocking queue to handle runnable tasks.
     */
    private fun startFlying(){
        Thread{
            // Run while not signaled to stop
            while(!stop){
                // take will block the thread until a new runnable task will be added to queue
                try { dispatchQueue.take().run() } catch(e: Exception){ e.printStackTrace() }
            }
        }.start()
    }
    // Return true if model has established connection to the given ip and port
    fun isConnected(): Boolean{
        return this.isConnected
    }
    /*
    * Function will open a socket with the given details, and get it's PrintWriter for sending data
     */
    fun connect(ipAdd: String, portNum: Int) {
        // if reconnecting - start the background handling thread (ole thread has finished)
        if (stop){
            stop = false
            startFlying()
        }
        Thread {
            try {
                fg = Socket(ipAdd, portNum)
                out = PrintWriter(fg!!.getOutputStream(), true)
                if (fg != null)
                    isConnected = true
            }
            catch (e: Exception){e. printStackTrace()}
        }.start()
    }
    // Disconnect from current connection. Close Socket and PrintWriter
    fun disconnect() {
        dispatchQueue.put(Runnable {
            stop = true
            isConnected = false
            out?.close()
            fg?.close()
        })
    }
    // Set aileron to the given value
    fun setAileron(data: Float){
        if (isConnected())
            sendData("flight/aileron", data)
    }
    // Set rudder to the given value
    fun setRudder(data: Float){
        if (isConnected())
            sendData("flight/rudder", data)
    }
    // Set elevator to the given value
    fun setElevator(data: Float){
        if (isConnected())
            sendData("flight/elevator", data)
    }
    // Set throttle to the given value
    fun setThrottle(data: Float){
        if (isConnected())
            sendData("engines/current-engine/throttle", data)
    }
    /*
    * Function gets a string and a float value and adding to queue a Runnable.
    * The runnable creates a string using the given attribute name and value
    * and sends the configuration line to the Server
     */
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