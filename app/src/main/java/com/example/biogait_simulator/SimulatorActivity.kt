package com.example.biogait_simulator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Insets.add
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.biogait_simulator.Fragments.BarFragment
import com.example.biogait_simulator.Fragments.ChallengeFragment
import com.example.biogait_simulator.Fragments.SpeedFragment
import com.example.biogait_simulator.Fragments.StatFragment
import com.example.biogait_simulator.databinding.ActivitySimulatorBinding

class SimulatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulatorBinding
    private lateinit var viewModel: SimulatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        val binding = ActivitySimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "SimulatorActivity"
        actionbar.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }

        //Landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val bundle = intent.extras
        val dato = bundle?.get("CASO")

        viewModel = ViewModelProvider(this).get(SimulatorViewModel::class.java)
        viewModel.setPaciente(Integer.parseInt(dato as String))
        resetVMValue()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<StatFragment>(R.id.FrameStat)
            add<SpeedFragment>(R.id.FrameSpeed)
            add<ChallengeFragment>(R.id.FrameChallenge)
            add<BarFragment>(R.id.FrameBar)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        viewModel.setClose(true)
        super.onBackPressed()
        this.finish()
    }

    //  reseteamos todos los valores del viewmodel
    private fun resetVMValue(){
        viewModel.setSesion(true)
        viewModel.setMinuto(true)
        viewModel.setUI(false)
        viewModel.setChallenge(0)
        viewModel.setAudio(false)
        viewModel.setSpeed(0)
        viewModel.setVariability(0.00)
        viewModel.setClose(false)
    }
}