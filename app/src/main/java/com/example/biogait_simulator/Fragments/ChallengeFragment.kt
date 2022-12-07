package com.example.biogait_simulator.Fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.biogait_simulator.R
import com.example.biogait_simulator.SimulatorViewModel
import com.example.biogait_simulator.databinding.FragmentChallengeBinding

class ChallengeFragment : Fragment() {

    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SimulatorViewModel

    private var stateButtlefly: Boolean = false
    private var stateBallons: Boolean = false
    private var stateMascot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SimulatorViewModel::class.java)

        //  Boton mariposa
        binding.btnButtlefly?.setOnClickListener{
            if(!stateButtlefly){
                viewModel.setLastChange(2)
                viewModel.setChallenge(1)
                stateButtlefly = true
                stateBallons = false
                stateMascot = false
                binding.btnButtlefly?.setBackgroundResource(R.drawable.round_green)
                binding.btnBallons?.setBackgroundResource(R.drawable.round_button)
                binding.btnMascot?.setBackgroundResource(R.drawable.round_button)
            }else{
                viewModel.setLastChange(2)
                viewModel.setChallenge(0)
                stateButtlefly = false
                binding.btnButtlefly?.setBackgroundResource(R.drawable.round_button)
            }
        }

        // boton globos
        binding.btnBallons?.setOnClickListener {
            if(!stateBallons){
                viewModel.setLastChange(2)
                viewModel.setChallenge(2)
                stateButtlefly = false
                stateBallons = true
                stateMascot = false
                binding.btnButtlefly?.setBackgroundResource(R.drawable.round_button)
                binding.btnBallons?.setBackgroundResource(R.drawable.round_green)
                binding.btnMascot?.setBackgroundResource(R.drawable.round_button)
            }else{
                viewModel.setLastChange(2)
                viewModel.setChallenge(0)
                stateBallons = false
                binding.btnBallons?.setBackgroundResource(R.drawable.round_button)
            }
        }

        // boton mascota
        binding.btnMascot?.setOnClickListener {
            if(!stateMascot){
                viewModel.setLastChange(2)
                viewModel.setChallenge(3)
                stateButtlefly = false
                stateBallons = false
                stateMascot = true
                binding.btnButtlefly?.setBackgroundResource(R.drawable.round_button)
                binding.btnBallons?.setBackgroundResource(R.drawable.round_button)
                binding.btnMascot?.setBackgroundResource(R.drawable.round_green)
            }else{
                viewModel.setLastChange(2)
                viewModel.setChallenge(0)
                stateMascot = false
                binding.btnMascot?.setBackgroundResource(R.drawable.round_button)
            }
        }

        //  El formate del codigo se hizo solo, asi que no se...
        binding.switchAudio?.setOnCheckedChangeListener{compoundButton, b ->
            run {
                viewModel.setAudio(
                    binding.switchAudio!!.isChecked
                )
                viewModel.setLastChange(3)
            }
        }

        //  Nomas para comprobar el cambio de estado
        viewModel.audio.observe(viewLifecycleOwner, Observer{ au->
            if(au){
                binding.switchAudio?.setTextColor(Color.parseColor("#023020"))
            }else{
                binding.switchAudio?.setTextColor(Color.parseColor("#A9A9A9")) // DarkGrey code
            }
        })

        viewModel.ui.observe(viewLifecycleOwner, Observer { ui ->
            if(ui){
                binding.switchAudio?.isEnabled = true
                binding.btnMascot?.isEnabled = true
                binding.btnBallons?.isEnabled = true
                binding.btnButtlefly?.isEnabled = true
            }else{
                binding.switchAudio?.isEnabled = false
                binding.btnMascot?.isEnabled = false
                binding.btnBallons?.isEnabled = false
                binding.btnButtlefly?.isEnabled = false
            }
        })
    }

}