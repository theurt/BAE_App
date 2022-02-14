package com.example.layout_projet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup




class GlassGame : GameFragment() {

    private lateinit var gyroListener : GyroscopeListener

    override fun onCreate(savedInstanceState: Bundle?) {

        gyroListener = activity?.supportFragmentManager?.findFragmentById(R.id.fragment_game_place)?.let {
            GyroscopeListener(
                it as GameFragment
            )
        }!!
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {


        //Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.glass_game, container, false
        )
    }

    override fun onStart(){
        super.onStart()
        this.gyroListener.newGame()
    }

    override fun onResume() {
        this.gyroListener.register()
        super.onResume()
    }

    override fun onPause() {
        this.gyroListener.pause()
        super.onPause()

    }

}