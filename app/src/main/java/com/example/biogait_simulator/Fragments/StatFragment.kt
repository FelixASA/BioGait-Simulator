package com.example.biogait_simulator.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.biogait_simulator.R
import com.example.biogait_simulator.SimulatorViewModel
import com.example.biogait_simulator.databinding.FragmentStatBinding

import java.util.*

class StatFragment : Fragment() {

    private var _binding:FragmentStatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SimulatorViewModel

    private val tiempoCalibracion:Long = 5000 // 5 segundos
    private val tiempoSS1:Long = 300000 // 5 minutos
    private val tiempoSS2:Long = 1800000 // 30 minutos
    private var sesion:Boolean = true // true = sesion1 , false = sesion20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSiguiente?.isEnabled = false
        binding.txtCalibracion?.text = timeStringFromLong(0)
        binding.txtTiempo?.text = timeStringFromLong(0)

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)

        //  Timer para simular los 5 minutos
        val timerSimulacion = object : CountDownTimer(60000, 1000){
            override fun onTick(t2: Long) {
                var tiempo: Long
                if(sesion){
                    tiempo = tiempoSS1 - t2
                }else {
                    tiempo = tiempoSS2 - t2
                }
                binding.txtTiempo?.text = timeStringFromLong(tiempo)
                //  Escritura de csv
            }

            override fun onFinish() {
                //  Habilitar el boton siguiente
                binding.btnSiguiente?.isEnabled = true
                binding.btnInicial?.isEnabled = true // Por si quiere repetir
                disableAll(viewModel)
            }

        }

        //  Timer para simular los 5 segundos
        val timerCali = object : CountDownTimer(5000, 1000){
            override fun onTick(t1: Long) {
                binding.txtCalibracion?.text = timeStringFromLong(t1)
            }

            override fun onFinish() {
                Toast.makeText(activity,"Empieza la simulacion", Toast.LENGTH_LONG).show()
                enableAll(viewModel)
                timerSimulacion.start()
            }

        }

        viewModel.paciente.observe(viewLifecycleOwner, Observer { p->
            when(p){
                1->{
                    binding.txtPaciente?.text = p.toString()
                    // algoritmo
                }
                2->{
                    binding.txtPaciente?.text = p.toString()
                    //  algoritmo
                }
                3->{
                    binding.txtPaciente?.text = p.toString()
                    //  algoritmo
                }
            }
        })

        viewModel.sesion.observe(viewLifecycleOwner, Observer { s->
            this.sesion = s
            if(s){
                binding.txtSesion?.text = "1, 00:00 a 05:00"
            }else{
                binding.txtSesion?.text = "20, 25:00 a 30:00"
            }
        })

        //  Desabilitamos los botones
        disableAll(viewModel)

        //  Boton para inicial la simulacion
        binding.btnInicial?.setOnClickListener{
            Toast.makeText(activity,"Iniciar", Toast.LENGTH_LONG).show()
            //viewModel.setVariability(20.0)
            //viewModel.setLastChange(3)
            binding.btnInicial?.isEnabled = false
            //  Hacer correr el timer de calibracion
            timerCali.start()
        }

        //  Boton para pasar al siguiente sesion de simulacion
        binding.btnSiguiente?.setOnClickListener{
            viewModel.setSesion(false)
            binding.btnSiguiente?.isEnabled = false
        }

    }

    //  Funcion de auto formato de tiempo
    private fun timeStringFromLong(ms: Long): String{
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        //val hours = (ms / (1000 * 60 * 60) % 24) // No se utiliza
        return makeTimeString(minutes, seconds)
    }

    //  Funcion para recibir el tiempo en string
    private fun makeTimeString(minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d", minutes, seconds)
    }
    //  Sirve para activar y desactiva los botones del interfaz
    private fun disableAll(viewModel: SimulatorViewModel){
        viewModel.setUI(false)
    }

    private  fun enableAll(viewModel: SimulatorViewModel){
        viewModel.setUI(true)
    }
}