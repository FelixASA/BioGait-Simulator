package com.example.biogait_simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimulatorViewModel : ViewModel() {

    private var _variability = MutableLiveData<Double>().apply { value = 0.00 } // Refleja el cambio de la variabilidad con las formulas
    private var _lastChange = MutableLiveData<Int>().apply { value = 1 } // Ultimo cambio :: 1 = velocidad, 2 = reto, 3 = audio
    private var _challenge = MutableLiveData<Int>() // Reto :: 1 = mariposa, 2 = globos, 3 = mascota, 0 = desabilitado
    private var _speed = MutableLiveData<Int>().apply { value = 0 } // Refleja el cambio de velocidad ingresado por el usuario
    private var _audio = MutableLiveData<Boolean>() // On/Off del audio feedback
    private var _paciente = MutableLiveData<Int>() // paciente 1, 2, 3
    private var _sesion = MutableLiveData<Boolean>().apply { value = true } // True = sesion1, False = sesion20
    private var _minuto = MutableLiveData<Boolean>().apply { value = true } // True = 1-5, False = 25-30
    private var _ui = MutableLiveData<Boolean>().apply { value = false } // On/Off imageButons
    private var _close = MutableLiveData<Boolean>().apply { false }

    val paciente: LiveData<Int> = _paciente
    val variability: LiveData<Double> = _variability
    val lastChange: LiveData<Int> = _lastChange
    val challenge: LiveData<Int> = _challenge
    val speed: LiveData<Int> = _speed
    val audio: LiveData<Boolean> = _audio
    val sesion: LiveData<Boolean> = _sesion
    val ui: LiveData<Boolean> = _ui
    val minuto: LiveData<Boolean> = _minuto
    val close: LiveData<Boolean> = _close

    fun setVariability(v: Double){
        _variability.value = v
    }

    fun setLastChange(l : Int){
        _lastChange.value = l
    }

    fun setChallenge(c : Int){
        _challenge.value = c
    }

    fun setSpeed(s: Int){
        _speed.value = s
    }

    fun setAudio(a : Boolean){
        _audio.value = a
    }

    fun setPaciente(p : Int){
        _paciente.value = p
    }

    fun setSesion(s : Boolean){
        _sesion.value = s
    }

    fun setUI(o : Boolean){
        _ui.value = o
    }

    fun setMinuto(m : Boolean){
        _minuto.value = m
    }

    fun setClose(c : Boolean){
        _close.value = c
    }
}