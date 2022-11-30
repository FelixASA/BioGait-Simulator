package com.example.biogait_simulator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

        val newStatFragment = StatFragment()
        val newSpeedFragment = SpeedFragment()
        val newBarFragment = BarFragment()
        val newChallengeFragment = ChallengeFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.FrameStat, newStatFragment )
        transaction.add(R.id.FrameSpeed, newSpeedFragment )
        transaction.add(R.id.FrameBar, newBarFragment )
        transaction.add(R.id.FrameChallenge, newChallengeFragment )
        transaction.addToBackStack(null)
        transaction.commit()

    }
}