package com.example.biogait_simulator


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.biogait_simulator.Fragments.BarFragment
import com.example.biogait_simulator.Fragments.ChallengeFragment
import com.example.biogait_simulator.Fragments.SpeedFragment
import com.example.biogait_simulator.Fragments.StatFragment
import com.example.biogait_simulator.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SimulatorViewModel
    lateinit var dexter: DexterBuilder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        //  Seguridad para comunicacion
        if(Build.VERSION.SDK_INT > 8){
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        //  Solicitud de permiso Storage
        if(checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivity(intent)
                }
            }
        }

        //  Agregar los fragmentos
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<StatFragment>(R.id.FrameStat)
            add<SpeedFragment>(R.id.FrameSpeed)
            add<ChallengeFragment>(R.id.FrameChallenge)
            add<BarFragment>(R.id.FrameBar)
        }

    }

    private fun getPermissions() {
        dexter = Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
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