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
import com.example.biogait_simulator.SimulatorViewModel
import com.example.biogait_simulator.databinding.FragmentStatBinding

class StatFragment : Fragment() {

    private var _binding:FragmentStatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SimulatorViewModel

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
        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)

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

        binding.btnInicial?.setOnClickListener{
            Toast.makeText(activity,"Iniciar", Toast.LENGTH_LONG).show()
            //viewModel.setVariability(20.0)
            //viewModel.setLastChange(3)
            Log.i("viewModel", "Inicial")
        }

    }

}