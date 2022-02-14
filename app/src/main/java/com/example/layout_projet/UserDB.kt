package com.example.layout_projet

import android.R
import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.lang.Thread.sleep
import kotlin.concurrent.timer


class UserDB (name:String) {
    val name = name
    var classicScore = 0
    var classicRank = 0
    var sportScore = 0
    var sportRank = 0

    //Fonction appelée lorsque la page de classement est lancée
    //Surement à modifier ça pour que ce soit mis à jour chaque fois qu'on change de type de classement
    fun initScoreAndRank(jsonObject: JSONObject){
        val db = Firebase.firestore
        val player = hashMapOf(
            "Name" to name,
            "Score" to classicScore
        )

        var i = 0
        db.collection("Classement/Classic/All")
            .orderBy("Score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //Toast.makeText(this, "${document.id} => ${document.data}", Toast.LENGTH_SHORT).show()
                    i+=1
                    if (document.id == name){
                        jsonObject.put("ClassicRank", i)
                        break
                    }
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
                Log.e("azertyuio", "Error getting documents.")
            }

        var j = 0
        db.collection("Classement/Sport/All")
            .orderBy("Score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //Toast.makeText(this, "${document.id} => ${document.data}", Toast.LENGTH_SHORT).show()
                    j+=1
                    if (document.id == name){
                        jsonObject.put("SportRank", i)
                        //sportRank = jsonObject.getInt("SportRank")
                        break
                    }
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
                Log.e("azertyuio", "Error getting documents.")
            }
    }

    fun addToClassic() {
        val db = Firebase.firestore
        val player = hashMapOf(
            "Name" to name,
            "Score" to classicScore
        )

        db.collection("Classement/Classic/All").document(name).set(player)


        var top : MutableList<QueryDocumentSnapshot> = mutableListOf()

        db.collection("Classement/Classic/TOP10")
            .orderBy("Score")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.e("azertyuio", "${document.id} => ${document.data}")

                    //Toast.makeText(this, "${document.id} => ${document.data}", Toast.LENGTH_SHORT).show()
                    top.add(document)
                    Log.e("azertyuio", top.count().toString())
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
                Log.e("azertyuio", "Error getting documents."
                )
            }
            .addOnCompleteListener {
                Log.e("userDB", top.count().toString())
                var addScoreToTop = false
                for (elem in top){
                    //Cas où on est déjà dans le top 10
                    if (name == elem.get("Name").toString()){
                        Log.e("userDB", elem.get("Name").toString())
                        Log.e("userDB", name)
                        addScoreToTop = false
                        db.collection("Classement/Classic/TOP10").document(name).set(player)
                        //ddToTopClassic(name, true)
                        break
                    }
                    //Cas où on est pas encore dans le top 10 mais que l'on y rentre
                    if (classicScore > elem.get("Score").toString().toInt()){
                        addScoreToTop = true
                    }
                }
                Log.e("userDBname27", addScoreToTop.toString())
                if (addScoreToTop){
                    var minName : String = top[0].id
                    Log.e("replace in top10", minName)
                    db.collection("Classement/Classic/TOP10").document(minName).delete()
                    db.collection("Classement/Classic/TOP10").document(name).set(player)
                }
            }
    }

    fun addToSport() {
        val db = Firebase.firestore
        val player = hashMapOf(
            "Name" to name,
            "Score" to sportScore
        )

        db.collection("Classement/Sport/All").document(name).set(player)


        var top : MutableList<QueryDocumentSnapshot> = mutableListOf()

        db.collection("Classement/Sport/TOP10")
            .orderBy("Score")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.e("azertyuio", "${document.id} => ${document.data}")

                    //Toast.makeText(this, "${document.id} => ${document.data}", Toast.LENGTH_SHORT).show()
                    top.add(document)
                    Log.e("azertyuio", top.count().toString())
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
                Log.e("azertyuio", "Error getting documents."
                )
            }
            .addOnCompleteListener {
                Log.e("userDB", top.count().toString())
                var addScoreToTop = false
                for (elem in top){
                    //Cas où on est déjà dans le top 10
                    if (name == elem.get("Name").toString()){
                        Log.e("userDB", elem.get("Name").toString())
                        Log.e("userDB", name)
                        addScoreToTop = false
                        db.collection("Classement/Sport/TOP10").document(name).set(player)
                        //ddToTopClassic(name, true)
                        break
                    }
                    //Cas où on est pas encore dans le top 10 mais que l'on y rentre
                    if (sportScore > elem.get("Score").toString().toInt()){
                        addScoreToTop = true
                    }
                }
                Log.e("userDBname27", addScoreToTop.toString())
                if (addScoreToTop){
                    var minName : String = top[0].id
                    Log.e("replace in top10", minName)
                    db.collection("Classement/Sport/TOP10").document(minName).delete()
                    db.collection("Classement/Sport/TOP10").document(name).set(player)
                }
            }
    }


    fun getClassicRank(listView : ListView, context: RankingFragment, textDesign : Int){
        val db = Firebase.firestore

        var top : MutableList<QueryDocumentSnapshot> = mutableListOf()
        var rank = 1
        db.collection("Classement/Classic/TOP10")
            .orderBy("Score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    top.add(document)
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {

                val listItems = arrayOfNulls<String>(top.count()+2)
                for (i in 0 until top.count()) {
                    val player = top[i]
                    listItems[i] = "${i+1} : ${player.id}   --> ${player.get("Score")} points"
                }

                listItems[top.count()] = "..."
                listItems[top.count()+1] = "$classicRank : $name   --> $classicScore points"
                val adapter = ArrayAdapter(context.requireContext(), textDesign, listItems)
                listView.adapter = adapter
            }
    }


    fun getSportRank(listView : ListView, context: RankingFragment, textDesign : Int){
        val db = Firebase.firestore

        var top : MutableList<QueryDocumentSnapshot> = mutableListOf()
        var rank = 1
        db.collection("Classement/Sport/TOP10")
            .orderBy("Score", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    top.add(document)
                }
            }
            .addOnFailureListener { exception ->
                //Toast.makeText(this, "Error getting documents.", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                Log.e("test de top", top.count().toString())
                val listItems = arrayOfNulls<String>(top.count()+2)
                for (i in 0 until top.count()) {
                    val player = top[i]
                    listItems[i] = "${i+1} : ${player.id}   --> ${player.get("Score")} points"
                }
                listItems[top.count()] = "..."
                listItems[top.count()+1] = "$sportRank : $name   --> $sportScore points"

                val adapter = ArrayAdapter(context.requireContext(), textDesign, listItems)
                listView.adapter = adapter
            }
    }
}