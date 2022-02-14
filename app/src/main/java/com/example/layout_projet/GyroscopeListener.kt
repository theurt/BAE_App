package com.example.layout_projet

import android.widget.ImageView
import kotlin.math.abs

class GyroscopeListener(fragment: GameFragment) : AccelerometerListener(fragment) {
    
    override fun modificateGame(azimut: Float, pitch: Float, roll: Float) {
        if (abs(pitch) > 60 ){
            val glassView : ImageView = fragment.requireActivity().findViewById(R.id.glass_image) as ImageView
            glassView.setImageResource(R.drawable.glass_vide )
            fragment.win = true
        }
    }
}