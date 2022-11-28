package com.example.biogait_simulator

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.biogait_simulator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.btnLineal?.setOnClickListener {
            val intent = Intent(this,SimulatorActivity::class.java)
            intent.putExtra("CASO","1")
            startActivity(intent)
        }

        binding.btnExponencial?.setOnClickListener {
            val intent = Intent(this,SimulatorActivity::class.java)
            intent.putExtra("CASO","2")
            startActivity(intent)
        }

        binding.btnAsintotica?.setOnClickListener {
            val intent = Intent(this,SimulatorActivity::class.java)
            intent.putExtra("CASO","3")
            startActivity(intent)
        }



    }

}