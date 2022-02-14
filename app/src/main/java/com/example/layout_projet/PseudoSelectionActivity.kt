package com.example.layout_projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class PseudoSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localData = LocalData()

        val jsonObject = localData.readData(this)

        //Si le pseudo est vide on lance l'écran de choix de selection
        if(jsonObject.get("Pseudo")==""){
            setContentView(R.layout.pseudo_selection)

            //Bouton pour lancer l'ecran suivant
            val endPseudoSelectionButton = findViewById<Button>(R.id.endPseudoSelection)

            //Bouton pour editer le pseudo
            val pseudoEditText = findViewById<EditText>(R.id.pseudoSelectionEditText)

            endPseudoSelectionButton.setOnClickListener {

                //On accepte pas les pseudos vides
                if (pseudoEditText.text.toString() == ""){
                    Toast.makeText(this, "Le pseudo ne peut pas être vide", Toast.LENGTH_SHORT).show()
                }
                else{
                    val intent : Intent = Intent(this,TutorielActivity::class.java).apply {
                        putExtra("previousScreen","Pseudo")
                    }

                    jsonObject.put("Pseudo", pseudoEditText.text.toString())
                    jsonObject.put("ClassicScore", 0)
                    jsonObject.put("SportScore", 0)
                    jsonObject.put("Music", true)
                    jsonObject.put("Sound", true)
                    jsonObject.put("PubPremium", false)
                    jsonObject.put("SportPremium", false)
                    jsonObject.put("ButtonSize", 15)
                    jsonObject.put("Tutorial", "false")
                    jsonObject.put("GameMode", "")
                    jsonObject.put("PubNumber", 0)
                    jsonObject.put("ClassicRank", 0)
                    jsonObject.put("SportRank", 0)

                    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    jsonObject.put("FirstStartDate", currentDate )
                    localData.saveData(this, jsonObject)

                    //intent.putExtra("score",GameActivity.score)
                    startActivity(intent)
                }
            }
        }
        else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // do nothing.
    }
}