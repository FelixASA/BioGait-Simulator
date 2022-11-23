package com.example.biogait_simulator
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.example.biogait_simulator.databinding.ActivitySimulatorBinding

class SimulatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        val binding = ActivitySimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val bundle = intent.extras
        val dato = bundle?.get("LLAVE")

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

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