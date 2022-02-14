package com.example.layout_projet

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.widget.ImageView
import android.widget.Toast
import kotlin.math.abs

abstract class AccelerometerListener(fragment: GameFragment) :
    SensorListener(fragment) {

    //Capteur Accelerometre
    protected var accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Capteur Magnetometre
    protected var magnometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    // Stocker les données de l'accéléromètre
    private var accelerometerOutput: FloatArray? = null

    // Stocker les données du magnetometre
    private var magnometerOutput: FloatArray? = null

    //Angles du téléphone
    private lateinit var orientation: FloatArray

    //Angles initiaux du téléphone
    private var startOrientation: FloatArray? = null

    private var x = 0f
    private var y = 0f
    private var z = 0f


    //Reset les données pour un nouveau jeu
    fun newGame() {
        //Reset startOrientation
        startOrientation = null
        x = 0F
        y = 0F
        z = 0F
    }

    //S'enregistrer comme listener du gyroscope
    open fun register() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME)
    }

    //Se désenregistrer comme listener du gyroscope
    fun pause() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    //Call-back appelé quand le téléphone bouge
    override fun onSensorChanged(event: SensorEvent) {

        synchronized(this){
            //Enregistrer les nouvelles données produites par le mouvement de l'utilisateur
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                accelerometerOutput = event.values

            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                magnometerOutput = event.values
            }

            //Calculer l'angle parcouru par le téléphone à partir de l'accéléromètre et du magnétomètre
            if (accelerometerOutput != null && magnometerOutput != null) {

                //Matrice de rotation
                val rotation = FloatArray(9)

                //Matrice d'inclinaison
                val inclinaison = FloatArray(9)

                //Calculer la matrice de rotation
                val rotationSuccess = SensorManager.getRotationMatrix(
                    rotation,
                    inclinaison,
                    accelerometerOutput,
                    magnometerOutput
                )

                //Calcul réussi
                if (rotationSuccess) {

                    //Déduire la matrice des orientations
                    orientation = FloatArray(3)
                    SensorManager.getOrientation(rotation, orientation)

                    //Cas initial : aucun point de comparaison
                    if (startOrientation == null) {

                        //Position initiale du téléphone pour comparaison
                        startOrientation = FloatArray(orientation.size)
                        System.arraycopy(
                            orientation as Any,
                            0,
                            startOrientation as Any,
                            0,
                            orientation.size
                        )
                    }
                    // l'azimuth
                    x = Math.toDegrees(
                        orientation!![0].toDouble()
                    ).toFloat()

                    // le pitch
                    y = Math.toDegrees(startOrientation!![1].toDouble()).toFloat() - Math.toDegrees(
                        orientation[1].toDouble()
                    ).toFloat()

                    // le roll
                    z = Math.toDegrees(startOrientation!![2].toDouble()).toFloat() - Math.toDegrees(
                        orientation[2].toDouble()
                    ).toFloat()

                    //Le téléphone a bien été retourné, on passe l'état du jeu à gagné
                    modificateGame(x, y, z);

                }
            }
        }
    }

    //Méthode à override définissant ce qu'on doit faire des angles calculés (réutilisé par compassListsner ou GyrsocopeListener par exemple)
    abstract fun modificateGame(azimut : Float,pitch : Float, roll : Float);

    }

