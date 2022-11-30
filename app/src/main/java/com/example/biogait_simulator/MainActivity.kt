package com.example.biogait_simulator


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.biogait_simulator.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var dexter: DexterBuilder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //getPermissions()

        binding.btnLineal?.setOnClickListener {
            if(checkPermission()) {
                val intent = Intent(this, SimulatorActivity::class.java)
                intent.putExtra("CASO", "1")
                startActivity(intent)
            }
        }

        binding.btnExponencial?.setOnClickListener {
            if(checkPermission()) {
                val intent = Intent(this, SimulatorActivity::class.java)
                intent.putExtra("CASO", "2")
                startActivity(intent)
            }
        }

        binding.btnAsintotica?.setOnClickListener {
            if(checkPermission()) {
                val intent = Intent(this, SimulatorActivity::class.java)
                intent.putExtra("CASO", "3")
                startActivity(intent)
            }
        }



    }

    private fun getPermissions() {
        dexter = Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report.let{
                        if(report!!.areAllPermissionsGranted()){
                            Toast.makeText(this@MainActivity, "Pemisos ortogados", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@MainActivity, "Se necesita los permisos", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(this,it.name,Toast.LENGTH_LONG).show()
            }
        dexter.check()
    }

    private fun checkPermission():Boolean{
        when {
            ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                return true
            }
            else -> {
                getPermissions()
            }
        }
        return false
    }
}