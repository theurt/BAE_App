package com.example.layout_projet

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RankingFragment : Fragment() {
    private lateinit var listView : ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        val view : View = inflater.inflate(
            R.layout.rank, container, false
        )
        val bouttonRetour = view.findViewById(R.id.backRank) as Button

        bouttonRetour.setOnClickListener {

            val manager: FragmentManager? = activity?.supportFragmentManager

            // Begin the fragment transition using support fragment manager
            if (manager != null) {

                val transaction = manager.beginTransaction()

                // Replace the fragment on container
                transaction.replace(R.id.fragmentMenu, MainMenuFragment())
                transaction.addToBackStack(null)

                // Finishing the transition
                transaction.commit()
            }
        }

        listView = view.findViewById<ListView>(R.id.rankingListView)

        val localData = LocalData()
        val jsonObject = localData.readData(this.requireActivity())
        var player = UserDB(jsonObject.getString("Pseudo"))

        player.classicScore = jsonObject.getInt("ClassicScore")
        player.sportScore = jsonObject.getInt("SportScore")

        player.classicRank = jsonObject.getInt("ClassicRank")
        player.sportRank = jsonObject.getInt("ClassicRank")

        player.getClassicRank(listView, this, R.layout.textdesignlistviewranking)

        val bouttonSport = view.findViewById(R.id.sportRank) as Button
        bouttonSport.isSelected = false

        val bouttonClassic = view.findViewById(R.id.classicRank) as Button
        bouttonClassic.isSelected = true



        bouttonSport.setOnClickListener {
            bouttonSport.isSelected = true
            bouttonClassic.isSelected = false
            player.getSportRank(listView, this, R.layout.textdesignlistviewranking)
        }

        bouttonClassic.setOnClickListener {
            bouttonClassic.isSelected = true
            bouttonSport.isSelected = false
            player.getClassicRank(listView, this, R.layout.textdesignlistviewranking)
        }


        return (view)

    }


}