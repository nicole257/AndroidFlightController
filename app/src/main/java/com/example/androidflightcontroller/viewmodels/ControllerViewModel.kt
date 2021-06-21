package com.example.androidflightcontroller.viewmodels

import androidx.lifecycle.ViewModel
import com.example.androidflightcontroller.models.ControllerModel

class ControllerViewModel: ViewModel() {

        private val model = ControllerModel()

        fun connect(ip: String, port: String) {
            model.connect(ip, port.toInt())
        }

        fun setAileron(data: Float){
            model.setAileron(data)
        }
        fun setRudder(data: Float){
            model.setRudder(data)
        }
        fun setElevator(data: Float){
            model.setElevator(data)
        }
        fun setThrottle(data: Float){
            model.setThrottle(data)
        }
        fun disconnect() = model.disconnect()
}