package com.example.biogait_simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulatorViewModel : ViewModel() {

    private var _variabilidad = MutableLiveData<Double>()

    val variabilidad: LiveData<Double> = _variabilidad

    fun setVariabilidad(v: Double){
        _variabilidad.value = v
    }
}