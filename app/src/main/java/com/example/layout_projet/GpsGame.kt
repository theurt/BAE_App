package com.example.layout_projet

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class GpsGame : GameFragment(){


    //Controleur du jeu
    private lateinit var gpsListener : GpsListener

    //nombre de pas a faire
    private var nombrePasAFaire : Int = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        gpsListener = activity?.supportFragmentManager?.findFragmentById(R.id.fragment_game_place)?.let {
            GpsListener(
                it as GameFragment
            )
        }!!
        super.onCreate(savedInstanceState)

        //TO DO nombre de pas
        //numeroDirection = randomGenerator.nextInt(nombreDirections);
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Inflate the layout for this fragment
        val fragmentView : View =  inflater.inflate(
            R.layout.gps_game, container, false
        )

        val textViewCurrentStep :TextView = fragmentView.findViewById<TextView>(R.id.stepsCurrent);
        textViewCurrentStep.text="0";

        val textViewMaxStep :TextView = fragmentView.findViewById<TextView>(R.id.stepsObectives);
        textViewMaxStep.text=nombrePasAFaire.toString();
        return fragmentView;
    }

    override fun onStart(){
        this.gpsListener.newGame(nombrePasAFaire);
        super.onStart();
    }

    override fun onResume() {
        this.gpsListener.register()
        super.onResume()
    }

    override fun onPause() {
        this.gpsListener.pause()
        super.onPause()

    }
}
