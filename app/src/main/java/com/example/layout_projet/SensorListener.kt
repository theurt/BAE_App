package com.example.layout_projet

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class SensorListener(protected val fragment: GameFragment)  : SensorEventListener{

    //Gestionnaire pour tous les capteurs du téléphone
    protected var sensorManager: SensorManager = fragment.activity?.getSystemService(Activity.SENSOR_SERVICE) as SensorManager;

    abstract override fun onAccuracyChanged(sensor: Sensor, accuracy: Int);

    //Call-back appelé quand le téléphone bouge
    abstract override fun onSensorChanged(event: SensorEvent);
}