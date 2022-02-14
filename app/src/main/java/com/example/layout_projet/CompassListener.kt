package com.example.layout_projet

import android.graphics.Interpolator
import android.hardware.SensorManager
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Toast
import java.lang.Thread.sleep
import kotlin.math.abs

//Cette classe étend AccelerometerListener pour appliquer un traitement spécifique quand l'azimut (angle avec le nord)
//change, il s'agit du controleur de fragment CompassGame
class CompassListener(fragment: GameFragment) : AccelerometerListener(fragment) {

    //Azimuth courant
    private var curectAzimut : Float = 0.0f;

    //AZimut a atteindre pour valider le jeux
    private var angleAtteindre : Float = 0F

    //Angle seuil pour mettre à jour la boussole lors d'une rotation de l'azimuth
    private val seuilDetectionDirection : Float = 5F

    //Animation mettant à jour la rotation de la boussole
    private var animation = RotateAnimation(
        -curectAzimut, 0F, Animation.RELATIVE_TO_SELF,
        0.5F, Animation.RELATIVE_TO_SELF, 0.5F)

    //Méthode overide appelée à chaque changement de l'azimuth, met à jour la vue de la boussole (vue) et vérifie que le jeux ait terminé (model)
    override fun modificateGame(azimut: Float, pitch: Float, roll: Float) {

        //Nouvel azimuth
        var newazimut = (azimut+360)%360;

        //L'azimuth n'a pas encore de valeur initiale, on ne fait rien tourner
        if (curectAzimut ==0F){
            curectAzimut = newazimut;
        }

        //Variation de l'azimuth détectée, on fait tourner la boussole
        else {

            //Mise à jour de l'aniamtions
                animation = RotateAnimation(
                        -curectAzimut, -newazimut, Animation.RELATIVE_TO_SELF,
                        0.5F, Animation.RELATIVE_TO_SELF, 0.5F
                );

            //paramètres animation
            animation.duration = 0;
            animation.repeatCount = 0
            animation.fillAfter = true;
            animation.interpolator = DecelerateInterpolator()

            //Lancer l'animationde rotation de ll boussole
            fragment.activity?.findViewById<ImageView>(R.id.compassImage)
                        ?.startAnimation(animation);

            //Vérfier si le jeu est fini
            if (abs(newazimut - angleAtteindre) < seuilDetectionDirection) {
                fragment.win = true;
            }

            curectAzimut = newazimut;

        }

    }

    //S'enregistrer comme listener du gyroscope
    override fun register() {
        sensorManager.registerListener(this, accelerometer, 2)
        sensorManager.registerListener(this, magnometer, 2)
    }

    //Reset les données pour un nouveau jeu
    fun newGame(direction : Int) {
        super.newGame();
        angleAtteindre = convertDirection(direction);
    }

    //Correspondance entre l'angl et le numéro de la direction (de 0 à 4)
    //TO DO : utilsier une enumération
    private fun convertDirection(numero : Int) : Float{
        if (numero ==0){
            return 0F;
        }

        else if (numero ==1){
            return 90F;
        }

        else if (numero ==2) {
            return 180F;
        }
        else {
                return 270F;
        }
    }

}