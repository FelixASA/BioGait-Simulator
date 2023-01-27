package com.example.biogait_simulator.Components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.biogait_simulator.databinding.DialogInputBinding

class InputDialog(
    private val onSubmitClickListener: (ip:String, port:Int) -> Unit
): DialogFragment() {
    private lateinit var binding: DialogInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogInputBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btnConectar?.setOnClickListener{
            onSubmitClickListener.invoke(binding.serverIp?.text.toString(),
                binding.serverPort?.text.toString().toInt())
            dismiss()
        }

        binding.btnCancel?.setOnClickListener{
            dismiss()
        }

        val dialog = builder.create()
        return dialog
    }
}