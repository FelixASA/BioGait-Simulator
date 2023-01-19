package com.example.biogait_simulator.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

    private var vSpeed: Int = 0

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
            vSpeed = sp
            binding.txtSpeed?.text = getString(R.string.vSpeed, vSpeed)
        })

        binding.barSpeed?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                vSpeed = progress
                binding.txtSpeed?.setTextColor(Color.parseColor("GREEN"))
                binding.txtSpeed?.text = getString(R.string.vSpeed, vSpeed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setSpeed(seekBar.progress)
                binding.txtSpeed?.setTextColor(Color.parseColor("#A9A9A9"))
                viewModel.setLastChange(1)
            }

        })

        viewModel.ui.observe(viewLifecycleOwner, Observer { ui ->
            binding.barSpeed?.isEnabled = ui
        })

        viewModel.speed.observe(viewLifecycleOwner, Observer{ sp ->
            vSpeed = sp
            binding.txtSpeed?.text = getString(R.string.vSpeed, vSpeed)
            binding.barSpeed?.progress = vSpeed
        })

    }
}