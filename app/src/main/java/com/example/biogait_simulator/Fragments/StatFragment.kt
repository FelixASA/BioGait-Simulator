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
import android.widget.RadioButton
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

    //  Para el tiempo de simulacion
    private var tiempo:Long = 0
    private var tiempoAbsoluto:Long = 0
    private val tiempoMin1_5:Long =  300000 //  Para sesion1
    private val tiempoMin25_30:Long = 1800000   //  Para sesion1
    private val tiempoMin571_575:Long = 34500000 //  Para sesion20
    private val tiempoMin595_600:Long = 36000000 //  Para sesion20
    private val tiempoCalibracion:Long = 60000 // 1 minuto
    private val tiempoRegresivo:Long = 150000 // 2.5 minutos
    private var sesion:Boolean = true // true = sesion1 , false = sesion20
    private var minuto:Boolean = true // true = inicios , false = finales
    private lateinit var timerSimular: CountDownTimer
    private lateinit var timerCalibrar: CountDownTimer


    //  Para la variabilidad
    private var value: Int = 0
    private var lastChange: Int = 0
    private var algoritmo: Int = 0  //  1 = linea, 2 = exponencial, 3 = asintotica

    //  Para creacion del archivo CSV
    private lateinit var fileFolder: File
    private lateinit var path: String
    private lateinit var timeStamp: String
    private lateinit var fileName: String
    private var flagFile: Boolean = false //    Para crear el timestamp del archivo

    //  Para escritura del excel
    private var p:Int = 0   // paciente
    private var s:Int = 0   // sesion
    private var tiempoCSV:String = "00:00"  // tiempo
    private var v:Int = 0   //  velocidad
    private var r:Int = 0   //  reto
    private var re:Int = 0  //  audio feedback
    private var va:Double = 0.00   //  variabilidad

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

        binding.txtCalibracion?.text = timeStringFromLong(0)
        binding.txtTiempo?.text = timeStringFromLong(0)

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)


        //--------------------------------TIMERS-----------------------------------------------------------------

        timerSimular = object : CountDownTimer(tiempoRegresivo, 1000){
            override fun onTick(t: Long) {
                tiempo = tiempoAbsoluto - ((t/1000)*2000) // el tiempo esta en incremento de 2s
                //Log.i("TIEMPO", TimeUnit.MILLISECONDS.toSeconds(tiempo).toString())
                binding.txtTiempo?.text = timeStringFromLong(tiempo)
                tiempoCSV = timeStringFromLong(tiempo)
                //  Escritura de csv
                var fileOutputStream: FileOutputStream = FileOutputStream(path+"/"+fileName+timeStamp+".csv", true)
                var cadena: String = getString(R.string.CSVContent,p,s,tiempoCSV,v,r,re,va)
                fileOutputStream.write(cadena.toByteArray())

                viewModel.setVariability((getVariability(TimeUnit.MILLISECONDS.toSeconds(tiempo))))
            }

            override fun onFinish() {
                binding.btnInicial?.isEnabled = true
                binding.sesion1?.isEnabled = true
                binding.sesion20?.isEnabled = true
                binding.min15?.isEnabled = true
                binding.min2530?.isEnabled = true
                disableAll(viewModel)
            }

        }

        timerCalibrar = object : CountDownTimer(tiempoCalibracion, 1000){
            override fun onTick(tiempo: Long) {
                binding.txtCalibracion?.text = timeStringFromLong(tiempo)
            }

            override fun onFinish() {
                viewModel.setVariability((getVariability(TimeUnit.MILLISECONDS.toSeconds(tiempo))))
            }

        }

        //------------------------------------VIEWMODELS-----------------------------------------

        //  Codigo inncesario, se puede cambiar las banderas en radiogroup change de sesion al igual que el livedata
        viewModel.sesion.observe(viewLifecycleOwner, Observer { s->
            this.sesion = s
            ajustarTiempo()
        })

        //  Codigo inncesario, se puede cambiar las banderas en radiogroup change de minutos al igual que el livedata
        viewModel.minuto.observe(viewLifecycleOwner, Observer { m ->
            this.minuto = m
            ajustarTiempo()
        })

        viewModel.paciente.observe(viewLifecycleOwner, Observer { p->
            binding.txtPaciente?.text = p.toString()
            this.algoritmo = p
            this.p = p
        })

        viewModel.audio.observe(viewLifecycleOwner, Observer { au ->
            if(au){
                this.re = 1
            }else{
                this.re = 0
            }
        })

        viewModel.challenge.observe(viewLifecycleOwner, Observer { ch ->
            this.r = ch
        })

        viewModel.speed.observe(viewLifecycleOwner, Observer { s ->
            this.value = s
            this.v = s
            if(flagFile) {
                timerCalibrar.start()
            }
        })

        viewModel.close.observe(viewLifecycleOwner, Observer { c ->
            if(c){
                stopTimer()
            }
        })

        //--------------------------------------------UI--------------------------------------------------------------
        //  Cambio de sesion
        binding.sesionGroup?.setOnCheckedChangeListener { radioGroup, i ->
            if(R.id.sesion1 == i){
                viewModel.setSesion(true)
            }else if (R.id.sesion20 == i){
                viewModel.setSesion(false)
            }
        }

        //  Cambio de minuto
        binding.minutoGroup?.setOnCheckedChangeListener { radioGroup, i ->
            if(R.id.min1_5 == i){
                viewModel.setMinuto(true)
            }else if (R.id.min25_30 == i){
                viewModel.setMinuto(false)
            }
        }

        //  Boton para inicial la simulacion
        binding.btnInicial?.setOnClickListener{
            binding.btnInicial?.isEnabled = false
            binding.sesion1?.isEnabled = false
            binding.sesion20?.isEnabled = false
            binding.min15?.isEnabled = false
            binding.min2530?.isEnabled = false

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
                this.flagFile = true
            }

            //  Hacer correr el timer de simulacion
            this.tiempo = 0
            timerSimular.start()
            enableAll(viewModel)
        }

        //  Desabilitamos los botones
        disableAll(viewModel)
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

    //  Auto ajuste de los tiempo de acuerdo a los parametros de sesion y minutos
    private fun ajustarTiempo(){
        //  sesion 1
        if(this.sesion){
            //  minuto 1-5
            this.s = 1
            if(this.minuto){
                this.tiempoAbsoluto = tiempoMin1_5
            }else{
                // minuto 25-30
                this.tiempoAbsoluto = tiempoMin25_30
            }
        }else{
            //  sesion 20
            this.s = 20
                //  minuto 571-575
            if(this.minuto){
                this.tiempoAbsoluto = tiempoMin571_575
            }else{
                // minuto 595-600
                this.tiempoAbsoluto = tiempoMin595_600
            }
        }
    }

    //  Sirve para detener los dos timers
    private fun stopTimer(){
        timerSimular.cancel()
        timerCalibrar.cancel()
    }

    //  Obtener variabilidad
    private fun getVariability(tiempo: Long): Double {
        Log.i("TIEMPO", tiempo.toString())
        var x:Double = (tiempo.toDouble() / (30 * 60 * 20))
        Log.i("X", x.toString())
        var newValue: Double = 0.00
        var value: Double = ((this.value).toDouble()/100)
        /*
        //  Si el ultimo cambio no es velocidad entonces
        if(lastChange!=1){
            this.value = 100
        */
        Log.i("VALUE", value.toString())
        when(algoritmo){
            1->{
                newValue = (x/(600*100))
                Log.i("NUEW-VALUE-1", newValue.toString())
            } //    Lineal
            2->{
                newValue = (exp(x)/exp(600.00)*100)
                Log.i("NUEW-VALUE-2", newValue.toString())
            } //    Expo
            3->{
                newValue = ((1-exp(-x/40)*100))
                Log.i("NUEW-VALUE-3", newValue.toString())
            }  //   Asint
        }
        this.va = abs(value + newValue)
        Log.i("VARIABILIDAD:",va.toString())
        return this.va
    }
}