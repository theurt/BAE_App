package com.example.layout_projet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*

class CompassGame : GameFragment() {

    //Controleur du jeu
    private lateinit var compassListener : CompassListener

    //Nombre de directions à considérer (Nord, Est, Sud, Ouest)
    //TO DO : utiliser une énumération
    private val nombreDirections = 4

    //Générateur pour sélectinner une direction aléatoirement
    private val randomGenerator : Random = Random()

    //NOms des directions, TO DO : enumération 
    private val direction0 : String = "Le Nord"

    private val direction1 : String = "L'Est"

    private val direction2 : String = "Le Sud"

    private val direction3 : String = "L'Ouest"

    //Numéro de la direction tirée aléatoirement
    private var numeroDirection : Int = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        compassListener = activity?.supportFragmentManager?.findFragmentById(R.id.fragment_game_place)?.let {
            CompassListener(
                it as GameFragment
            )
        }!!
        super.onCreate(savedInstanceState)

        numeroDirection = randomGenerator.nextInt(nombreDirections);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Inflate the layout for this fragment
        var fragmentView : View =  inflater.inflate(
            R.layout.compass_test, container, false
        )

        //Nom de la direction poiuyr affichage de la consigne au joueur
        val textDirections = fragmentView.findViewById<TextView>(R.id.direction);
        textDirections.text = onStringDirection(numeroDirection);
        return fragmentView

    }

    override fun onStart(){
        super.onStart();
        this.compassListener.newGame(numeroDirection);
    }

    override fun onResume() {
        this.compassListener.register()
        super.onResume()
    }

    override fun onPause() {
        this.compassListener.pause()
        super.onPause()

    }

    //Conversion numéro de direction en String pour l'affichage
    //TO DO : utiliser une énumération à la place
    private fun onStringDirection(numero : Int) : String? {

        if (numero == 0){
            return direction0;
        }

        else if (numero == 1){
            return direction1;
        }

        else if (numero == 2){
            return direction2;
        }

        else if (numero == 3){
            return direction3;
        }

        else {
            return null
        }
    }
}