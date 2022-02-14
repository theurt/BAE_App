package com.example.layout_projet

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class GpsListener(fragment: GameFragment) :
    SensorListener(fragment) {


        //Sensor pour les pas
        private val sensorPas : Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        //nombre de pas courants
        private var nombrePasCourant :Int = 0;

        //nombre de pas avant refresh du listener
        private var nombreTotalPasCourantPrecedent : Float = 0f;

        //nombre de pas a faire
        private var nombrePasAFaire : Int = 10;


    //Reset les données pour un nouveau jeu
    fun newGame(pas : Int) {
        nombrePasAFaire = pas;
    }

    fun register() {
        sensorManager.registerListener(this, sensorPas, SensorManager.SENSOR_DELAY_GAME)
    }

    fun pause() {
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {

        //var offsetPas : Float = 0F;
        //Toast.makeText(fragment.activity?.applicationContext,"MAJ",Toast.LENGTH_SHORT).show();

        //if (event != null) {

                //Initiatilisation
                //if (nombreTotalPasCourant == 0F){
                 //   offsetPas = event.values[0];
                //}

                //nombreTotalPasCourant = event.values[0]

                //MAJ le nombre de pas courant
                //val nombrePasCourant = nombreTotalPasCourant.toInt() - nombreTotalPasCourantPrecedent.toInt()
                //nombreTotalPasCourantPrecedent = nombreTotalPasCourant;
                
                //Mettre à jour le texte
                val textViewCurrentStep : TextView? = fragment.activity?.findViewById<TextView>(R.id.stepsCurrent);
                if (textViewCurrentStep != null) {
                    textViewCurrentStep.text = (nombrePasCourant++).toString()
                }

                //MAJ de la barre circulaire
                val progressBar : CircularProgressBar? = fragment.activity?.findViewById<CircularProgressBar>(R.id.progressBar);
                progressBar?.apply{setProgressWithAnimation(nombrePasCourant.toFloat())
                    progressMax = nombrePasAFaire.toFloat()
                }

                //Vérifier si le jeux est fini
                if(nombrePasCourant  >=nombrePasAFaire.toFloat()){
                    fragment.win = true;
                }

            //}
        }

    override fun onAccuracyChanged(sensor: Sensor, p1: Int) {
    //Ne rien faire
    }

}