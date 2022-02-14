package com.example.layout_projet


import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

import java.util.*

class FingerGame : GameFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        //on defini la position random de l'empreinte et on active le onclick

        //Inflate the layout for this fragment
        val infla : View  =  inflater.inflate(R.layout.finger_layout, container, false)
        configureImageButton(infla)
        return infla
    }



    private fun configureImageButton(view : View) {
        val fingerButton = view.findViewById<ImageButton>(R.id.finger_btn)
        // Get the last ImageButton's layout parameters
        //val params_btn = finger_button.layoutParams as RelativeLayout.LayoutParams

        val displaymetrics = DisplayMetrics()
        activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displaymetrics)
        //generation aleatoire de la position l'empreinte
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread(Runnable {
                    val R = Random()
                    if(fingerButton != null) {
                        val dx: Float =
                            R.nextFloat() * (displaymetrics.widthPixels - fingerButton.width)
                        val dy: Float =
                            R.nextFloat() * (displaymetrics.heightPixels - 2 * fingerButton.height)
                        val timer = Timer()
                        fingerButton?.animate()
                            ?.x(dx)
                            ?.y(dy)
                            ?.setDuration(0)
                            ?.start()
                    }

                })
            }
        }, 0, 1000)

        // Configuration du click
        fingerButton?.setOnClickListener {
            win = true
        }
    }
}