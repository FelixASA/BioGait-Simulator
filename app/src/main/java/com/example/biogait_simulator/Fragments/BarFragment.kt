package com.example.biogait_simulator.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.biogait_simulator.R
import com.example.biogait_simulator.SimulatorViewModel
import com.example.biogait_simulator.databinding.FragmentBarBinding

class BarFragment : Fragment() {

    private var _binding: FragmentBarBinding? = null
    private val binding get()= _binding!!
    private var variability: Double = 0.00
    private var lastChange: Int = 0 // Ultimo cambio :: 1 = velocidad, 2 = reto, 3 = audio

    private lateinit var viewModel: SimulatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Evita que el usuario pueda mover el seekBar
        binding.barAudio?.setOnTouchListener{v, event-> true}
        binding.barVelocidad?.setOnTouchListener{v, event-> true}
        binding.barReto?.setOnTouchListener{v, event-> true}

        //  Establecer valor
        binding.vVelocidad?.text = getString(R.string.vVariability,variability)
        binding.vAudio?.text = getString(R.string.vVariability,variability)
        binding.vReto?.text = getString(R.string.vVariability,variability)

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)

        //  Ultimos cambios, dar color a las barras
        viewModel.lastChange.observe(viewLifecycleOwner, Observer{lc ->
            lastChange = lc //   Asignamos para utilizarlo en el otro case
            when (lc){
                1 -> {
                    binding.barVelocidad?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrascolor)
                    binding.barReto?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)
                    binding.barAudio?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)

                } // velocidad
                2 -> {
                    binding.barVelocidad?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)
                    binding.barReto?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrascolor)
                    binding.barAudio?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)
                }// reto
                3 -> {
                    binding.barVelocidad?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)
                    binding.barReto?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrasbw)
                    binding.barAudio?.progressDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.barrascolor)
                } // audio
                else -> {
                    //  no pasa nada
                }
            }
        })
        //Log.i("viewModel", "Hay cambio2")
        //  Cambios en la variabilidad
        viewModel.variability.observe(viewLifecycleOwner, Observer{v ->
            when(lastChange){
                1->{
                    binding.vVelocidad?.text = getString(R.string.vVariability,v)
                    binding.barVelocidad?.progress = v.toInt()
                } // velocidad

                2->{
                    binding.vReto?.text = getString(R.string.vVariability,v)
                    binding.barReto?.progress = v.toInt()
                } // reto

                3->{
                    binding.vAudio?.text = getString(R.string.vVariability,v)
                    binding.barAudio?.progress = v.toInt()
                } // audio

                else ->{

                }
            }

        })
    }
}

