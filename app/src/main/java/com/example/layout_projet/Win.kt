package com.example.layout_projet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.lang.Math.max


class Win : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.win, container, false
        )

        val localData = LocalData()
        val jsonObject = localData.readData(this.requireActivity())

        val scoreText = activity?.findViewById<TextView>(R.id.gameScore)
        val scoreWinText = view.findViewById<TextView>(R.id.winScore)
        scoreWinText.text = scoreText?.text

        val buttonQuit = view.findViewById(R.id.leaveWin) as Button



        buttonQuit.setOnClickListener {
            val scoreValue = scoreWinText.text.split(" ")

            var player = UserDB(jsonObject.getString("Pseudo"))
            if (jsonObject.get("GameMode") == "classic" && scoreValue[1].toInt() > jsonObject.getInt("ClassicScore")){
                //Mise à jour de notre score
                jsonObject.put("ClassicScore", scoreValue[1])
                player.classicScore = scoreValue[1].toInt()
                //Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
                player.addToClassic()
            }
            else if(jsonObject.get("GameMode") == "sport" && scoreValue[1].toInt() > jsonObject.getInt("SportScore")){
                jsonObject.put("SportScore", scoreValue[1])
                player.sportScore = scoreValue[1].toInt()
                player.addToSport()
            }
            localData.saveData(this.requireActivity(), jsonObject)

            val intent = Intent(activity,MainActivity::class.java)
            //intent.putExtra("score",GameActivity.score)
            startActivity(intent)
        }

        return (view)
    }


}