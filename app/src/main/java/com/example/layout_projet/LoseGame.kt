package com.example.layout_projet

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.json.JSONObject

class LoseGame : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {



        //Inflate the layout for this fragment
        val view : View = inflater.inflate(
            R.layout.lose, container, false
        )
        val localData = LocalData()
        val jsonObject = localData.readData(this.requireActivity())

        val scoreText = activity?.findViewById<TextView>(R.id.gameScore)
        val scoreLoseText = view.findViewById<TextView>(R.id.loseScore)
        scoreLoseText.text = scoreText?.text

        val leaveButton = view.findViewById(R.id.leaveLose) as Button

        leaveButton.setOnClickListener {
            if (!jsonObject.getBoolean("PubPremium") && !jsonObject.getBoolean(("SportPremium"))){
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("OFFRE PREMIUM")
                var message: String = "Pour augmenter vos possibilités de jeu et vous débarassez des pubs, " +
                        "rendez-vous tout de suite dans la boutique pour profiter de l'option PREMIUM à -50%"
                builder.setMessage(message)

                builder.setPositiveButton("Aller dans la boutique") { dialog, which ->
                    saveScore(jsonObject, localData, scoreLoseText)

                    val intent = Intent(activity,MainActivity::class.java).apply {
                        putExtra("previousScreen", "loseScreen")
                    }
                    //intent.putExtra("score",GameActivity.score)
                    startActivity(intent)
                }

                builder.setNegativeButton("Fin de partie") { _, _ ->
                    saveScore(jsonObject, localData, scoreLoseText)

                    val intent = Intent(activity,MainActivity::class.java)
                    //intent.putExtra("score",GameActivity.score)
                    startActivity(intent)
                }
                builder.show()
            }
            else{
                saveScore(jsonObject, localData, scoreLoseText)

                val intent = Intent(activity,MainActivity::class.java)
                //intent.putExtra("score",GameActivity.score)
                startActivity(intent)
            }
        }
        return (view)
    }



    fun saveScore(jsonObject: JSONObject, localData: LocalData, scoreLoseText: TextView) {
        val scoreValue = scoreLoseText.text.split(" ")

        var player = UserDB(jsonObject.getString("Pseudo"))

        //Cas où on bat notre score en mode classique
        if (jsonObject.get("GameMode") == "classic" && scoreValue[1].toInt() > jsonObject.getInt("ClassicScore")){
            //Mise à jour de notre score
            jsonObject.put("ClassicScore", scoreValue[1])
            player.classicScore = scoreValue[1].toInt()
            Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
            player.addToClassic()
        }
        else if(jsonObject.get("GameMode") == "sport" && scoreValue[1].toInt() > jsonObject.getInt("SportScore")){
            jsonObject.put("SportScore", scoreValue[1])
            player.sportScore = scoreValue[1].toInt()
            player.addToSport()
        }
        localData.saveData(this.requireActivity(), jsonObject)
    }
}