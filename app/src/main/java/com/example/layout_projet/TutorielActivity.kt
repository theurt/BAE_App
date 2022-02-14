package com.example.layout_projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

class TutorielActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        //Vérifier si l'utilisateur veut lancer le tuto
        val localData = LocalData()
        val jsonObject = localData.readData(this)
        jsonObject.put("Tutorial", "true")

        localData.saveData(this, jsonObject)

        //Charger l'écran de tuto
        setContentView(R.layout.activity_tutoriel)

        val previousScreen = intent.extras!!.getString("previousScreen", "")


        //Bouton passer
        val buttonSkipTutorial = findViewById<Button>(R.id.skipTutorial)
        buttonSkipTutorial.setOnClickListener{
            if (previousScreen.equals("Pseudo") ){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else if (previousScreen == "Settings") {
                finish()
            }

        }

        val tutorialLayout = findViewById<LinearLayout>(R.id.tutorialLayout)
        val imageTutorial = findViewById<ImageView>(R.id.imageTutorial)
        var image = 0
        val buttonNextTutorial = findViewById<Button>(R.id.nextTutorial)
        buttonNextTutorial.setOnClickListener{
            when (image) {
                0 -> {
                    imageTutorial.setImageResource(R.drawable.menu_principal2)
                    image += 1
                }
                1 -> {
                    imageTutorial.setImageResource(R.drawable.menu_principal3)
                    image += 1
                }
                2 -> {
                    imageTutorial.setImageResource(R.drawable.menu_principal4)
                    image += 1
                }
                3 -> {
                    imageTutorial.setImageResource(R.drawable.menu_principal5)
                    image += 1
                }
                4 -> {
                    imageTutorial.setImageResource(R.drawable.menu_principal6)
                    image += 1
                }
                5 -> {
                    tutorialLayout.setBackgroundResource(R.color.orange)
                    imageTutorial.setImageResource(R.drawable.jeu)
                    image += 1
                }
                6 -> {
                    imageTutorial.setImageResource(R.drawable.jeu2)
                    image += 1
                }
                7 -> {
                    imageTutorial.setImageResource(R.drawable.jeu3)
                    image += 1
                }
                8 -> {
                    if (previousScreen.equals("Pseudo") ){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else if (previousScreen == "Settings") {
                        finish()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        // do nothing.
    }
}