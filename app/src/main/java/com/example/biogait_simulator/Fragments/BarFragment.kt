package com.example.biogait_simulator.Fragments

import android.os.Bundle
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
import com.example.biogait_simulator.databinding.FragmentBarBinding

class BarFragment : Fragment() {

    private var _binding: FragmentBarBinding? = null
    private val binding get()= _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Evita que el usuario pueda mover el seekBar
        binding.barAudio?.setOnTouchListener{v, event-> true}
        binding.barVelocidad?.setOnTouchListener{v, event-> true}
        binding.barReto?.setOnTouchListener{v, event-> true}

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)
        viewModel.variabilidad.observe(viewLifecycleOwner, Observer{v ->
            Log.i("viewModel", "Hay cambio2")
            binding.vVelocidad?.text = getString(R.string.vVelocidad,v)
            binding.barVelocidad?.progress = v.toInt()
        })
    }
}

