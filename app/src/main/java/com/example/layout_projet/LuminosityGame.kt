package com.example.layout_projet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.properties.Delegates
import android.util.Log


//var CURBRIGHT : Int = 0

class LuminosityGame : GameFragment() {

    private var lightsOff : Boolean = false
    private var loose : Boolean = false
    private var curBright : Int = 0
    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Inflate the layout for this fragment
        var myInflatedView = inflater.inflate(R.layout.luminosity_game, container, false)
        var image = myInflatedView.findViewById<ImageView>(R.id.image_lumiere)

        //Enregistrer la luminosité actuelle
        this.curBright = Settings.System.getInt(activity?.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        //CURBRIGHT = this.curBright
        //On accède aux attributs de l'écran
        val attributes = activity?.window?.attributes
        //Choisir nbreHasard entre 2 et 6 (car on va dire le timer du jeu c'est 7 secondes)
        var nbreHasard = (2..6).random()

        //Quand timer == nbreHasard
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                //Baisser luminosité à 0
                lightsOff = true

                // On peut modifier la valeur de la luminosité (entre 0 et 255)
                if (attributes != null && !loose) {
                    //0F c'est sombre et 1F c'est clair à vérifier
                    attributes.screenBrightness = 0F
                }
                activity?.window?.attributes = attributes
            }
        }, (nbreHasard*1000).toLong())

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                // On peut modifier la valeur de la luminosité (entre 0 et 255)
                if (attributes != null && lightsOff) {
                    //0F c'est sombre et 1F c'est clair à vérifier
                    attributes.screenBrightness = curBright.toFloat()
                }
                activity?.window?.attributes = attributes
            }
        }, (9.5*1000).toLong())


        image.setOnClickListener() {
            if (lightsOff) {
                // On remet à la luminosité initiale
                if (attributes != null) {
                    attributes.screenBrightness = this.curBright.toFloat()
                }
                activity?.window?.attributes = attributes
                this.win = true
            }
            else {
                // On remet à la luminosité initiale
                // On va pouvoir skip l'action du handler, grace à ce booléan
                this.loose = true
                this.lose = true
            }
        }
        return myInflatedView

    }

    override fun onPause() {
        this.loose = true
        super.onPause()
    }

    override fun onDestroy() {
        this.loose = true
        super.onDestroy()
    }

    override fun onDestroyView() {
        this.loose = true
        super.onDestroyView()
    }


    /*override fun onDestroy() {
        super.onDestroy()
        if(this.lose) {
            //On accède aux attributs de l'écran
            val attributes = activity?.window?.attributes
            if (attributes != null) {
                attributes.screenBrightness = CURBRIGHT.toFloat()
            }
        }
    }*/

}
