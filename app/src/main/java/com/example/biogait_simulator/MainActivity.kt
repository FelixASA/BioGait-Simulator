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

        binding.btnInicio?.setOnClickListener {
            clickBoton()
        }

    }

    fun clickBoton(){
        val intent = Intent(this,SimulatorActivity::class.java)
        intent.putExtra("LLAVE","Hola activity2")
        startActivity(intent)
    }
}