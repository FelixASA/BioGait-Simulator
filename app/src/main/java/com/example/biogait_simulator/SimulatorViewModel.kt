package com.example.biogait_simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulatorViewModel : ViewModel() {

    private var _variability = MutableLiveData<Double>().apply { value = 0.00 } // Refleja el cambio de la variabilidad con las formulas
    private var _lastChange = MutableLiveData<Int>() // Ultimo cambio :: 1 = velocidad, 2 = reto, 3 = audio
    private var _challenge = MutableLiveData<Int>() // Reto :: 1 = mariposa, 2 = globos, 3 = mascota, 0 = desabilitado
    private var _speed = MutableLiveData<Float>() // Refleja el cambio de velocidad ingresado por el usuario
    private var _audio = MutableLiveData<Boolean>() // On/Off del audio feedback

    val variability: LiveData<Double> = _variability
    val lastChange: LiveData<Int> = _lastChange
    val challenge: LiveData<Int> = _challenge
    val speed: LiveData<Float> = _speed
    val audio: LiveData<Boolean> = _audio

    fun setVariability(v: Double){
        _variability.value = v
    }

    fun setLastChange(l : Int){
        _lastChange.value = l
    }

    fun setChallenge(c : Int){
        _challenge.value = c
    }

    fun setSpeed(s : Float){
        _speed.value = s
    }

    fun setAudio(a : Boolean){
        _audio.value = a
    }

}