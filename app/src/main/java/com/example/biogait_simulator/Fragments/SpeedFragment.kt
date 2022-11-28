package com.example.biogait_simulator.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.biogait_simulator.R
import com.example.biogait_simulator.SimulatorViewModel
import com.example.biogait_simulator.databinding.FragmentSpeedBinding


class SpeedFragment : Fragment() {

    private var _binding:FragmentSpeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SimulatorViewModel

    private var vSpeed: Float = 2.0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)

        viewModel.speed.observe(viewLifecycleOwner, Observer { sp ->
            binding.txtSpeed?.text = getString(R.string.vSpeed, sp)
        })

        //  NOTA: no existe la funcion de mantener el click para incremental

        binding.btnMas?.setOnClickListener {
            if(vSpeed<14.0F){
                vSpeed += 0.1F
                viewModel.setSpeed(vSpeed)
                viewModel.setLastChange(1)
            }
        }

        binding.btnMenos?.setOnClickListener {
            if(vSpeed>0.1){
                vSpeed -= 0.1F
                viewModel.setSpeed(vSpeed)
                viewModel.setLastChange(1)
            }
        }

    }
}