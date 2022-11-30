package com.example.biogait_simulator.Fragments


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.abs
import java.lang.Math.exp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class StatFragment : Fragment() {

    private var _binding:FragmentStatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SimulatorViewModel

    private val tiempoCalibracion:Long = 5000 // 5 segundos
    private val tiempoSS1:Long = 300000 // 5 minutos
    private val tiempoSS2:Long = 1800000 // 30 minutos
    private var sesion:Boolean = true // true = sesion1 , false = sesion20

    //  Para la variabilidad
    private var value: Float = 0F
    private var lastChange: Int = 0
    private var algoritmo: Int = 0

    private lateinit var fileFolder: File
    private lateinit var path: String
    private lateinit var timeStamp: String
    private lateinit var fileName: String
    private var flagFile: Boolean = false //    Para crear el timestamp del archivo

    private var p:Int = 0
    private var s:Int = 0
    private var t:String = "00:00"
    private var v:Int = 0
    private var r:Int = 0
    private var re:Int = 0
    private var va:Float = 0F

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
        val timerSimulacion = object : CountDownTimer(300000, 1000){
            override fun onTick(t2: Long) {
                var tiempo: Long
                if(sesion){
                    tiempo = tiempoSS1 - t2
                }else {
                    tiempo = tiempoSS2 - t2
                }
                binding.txtTiempo?.text = timeStringFromLong(tiempo)
                t = timeStringFromLong(tiempo)
                //  Escritura de csv
                var fileOutputStream: FileOutputStream = FileOutputStream(path+"/"+fileName+timeStamp+".csv", true)
                var cadena: String = getString(R.string.CSVContent,p,s,t,v,r,re,va)
                fileOutputStream.write(cadena.toByteArray())

                /*
                File(path+"/"+fileName+timeStamp+".csv").printWriter().use{
                        out-> out.println("$p, $s, $t, $v, $r, $re, $va")
                }*/
                Log.i("VALIABILIDAD",getVariability(TimeUnit.MILLISECONDS.toSeconds(tiempo)).toString())
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
            binding.txtPaciente?.text = p.toString()
            this.algoritmo = p
            this.p = p
        })

        viewModel.sesion.observe(viewLifecycleOwner, Observer { s->
            this.sesion = s
            if(s){
                binding.txtSesion?.text = "1, 00:00 a 05:00"
                this.s = 1
            }else{
                binding.txtSesion?.text = "20, 25:00 a 30:00"
                this.s = 20
            }
        })

        //  Obtener los valores
        viewModel.lastChange.observe(viewLifecycleOwner, Observer { ls ->
            this.lastChange = ls
            when(ls){
                1->{
                    this.v = 1
                    this.r = 0
                    this.re = 0
                }
                2->{
                    this.v = 0
                    this.r = 1
                    this.re = 0
                }
                3->{
                    this.v = 0
                    this.r = 0
                    this.re = 1
                }
            }
        })

        viewModel.speed.observe(viewLifecycleOwner, Observer { s ->
            this.value = s
        })

        //  Desabilitamos los botones
        disableAll(viewModel)

        //  Boton para inicial la simulacion
        binding.btnInicial?.setOnClickListener{
            binding.btnInicial?.isEnabled = false

            //  Crear archivo csv
            fileFolder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"BioGaitSimulator")
            if(!fileFolder.exists()){ fileFolder.mkdir() }
            this.path = fileFolder.absolutePath
            if(!this.flagFile){
                fileName = "BGSLog_"
                timeStamp = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Date())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val p = "Paciente"
                    val s = "Sesion"
                    val t = "Tiempo"
                    val v = "Velocidad"
                    val r = "Reto"
                    val re = "Retroalimentacion"
                    val va = "Variabilidad"
                    if (Environment.isExternalStorageManager()) {
                        File(path+"/"+fileName+timeStamp+".csv").printWriter().use{
                                out-> out.println("$p, $s, $t, $v, $r, $re, $va")
                        }
                    }
                }

                //fileCSV.write(getString(R.string.CSVHeader).toByteArray())
                this.flagFile = true
            }

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
    //  Sirve para activar y desactiva los botones del interfaz
    private  fun enableAll(viewModel: SimulatorViewModel){
        viewModel.setUI(true)
    }

    //  Obtener variabilidad
    private fun getVariability(tiempo: Long): Float {
        var x = tiempo / (30 * 60)
        Log.i("X", x.toString())
        var newValue: Float = 0F
        //  Si el ultimo cambio no es velocidad entonces
        if(lastChange!=1){
            this.value = 100F
        }
        Log.i("VALUE", this.value.toString())
        when(algoritmo){
            1->{
                newValue = (x / (20) * 100).toFloat()
                Log.i("NUEW-VALUE", newValue.toString())
            } //    Lineal
            2->{
                newValue = (exp(x.toDouble()) / exp(20.0) * 100).toFloat()
                Log.i("NUEW-VALUE", newValue.toString())
            } //    Expo
            3->{
                newValue = ((1 - exp(x.toDouble() * -1)) * 100).toFloat()
                Log.i("NUEW-VALUE", newValue.toString())
            }  //   Asint
        }
        this.va = abs(this.value - newValue)
        return this.va
    }
}