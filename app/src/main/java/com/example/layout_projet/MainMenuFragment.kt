package com.example.layout_projet

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer
import androidx.core.app.ActivityCompat.requestPermissions
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import android.Manifest.permission
import org.json.JSONObject


class MainMenuFragment : Fragment() {



    //To change of menu
    private val changeMenu = View.OnClickListener { view ->

        var fragment : Fragment? = null


        when (view.id) {
            R.id.settingsButtonMainMenu-> {

                fragment = SettingsFragment()
            }
            R.id.rankingButtonMainMenu-> {
                fragment = RankingFragment()
            }

            R.id.shopButtonMainMenu->{
                fragment = ShopFragment()
            }

        }

        val manager : FragmentManager? = activity?.supportFragmentManager

        // Begin the fragment transition using support fragment manager
        if (manager != null) {

            val transaction: FragmentTransaction = manager.beginTransaction()

            // Replace the fragment on container
            if (fragment != null) {
                transaction.replace(R.id.fragmentMenu, fragment)
            }
            transaction.addToBackStack(null)

            // Finishing the transition
            transaction.commit()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        val view = inflater.inflate(
            R.layout.main_menu, container, false
        )

        val localData = LocalData()
        val jsonObject = localData.readData(this.requireActivity())

        val scoreValue = view.findViewById<TextView>(R.id.scoreDisplayMainMenu)
        if (jsonObject.getInt("ClassicScore") >= jsonObject.getInt("SportScore")){
            scoreValue.text = jsonObject.get("ClassicScore").toString()
        }
        else{
            scoreValue.text = jsonObject.get("SportScore").toString()
        }


        // get reference to parametersButton
        val buttonParam : Button = view.findViewById(R.id.settingsButtonMainMenu) as Button

        // get reference to parametersButton
        val buttonClassic : Button = view.findViewById(R.id.classicButtonMainMenu) as Button

        // get reference to parametersButton
        val buttonSport : Button = view.findViewById(R.id.sportsButtonMainMenu) as Button

        // get reference to parametersButton
        val buttonRanking : Button = view.findViewById(R.id.rankingButtonMainMenu) as Button

        // get reference to parametersButton
        val buttonShop : Button = view.findViewById(R.id.shopButtonMainMenu) as Button

        buttonParam.setOnClickListener(changeMenu)
        buttonRanking.setOnClickListener(changeMenu)
        buttonShop.setOnClickListener(changeMenu)
        buttonSport.setOnClickListener {
            if (jsonObject.get("SportPremium").toString() == "true") {
                askPermission(jsonObject, localData)
            } else {
                Toast.makeText(activity, "Achetez l'option SPORT dans la boutique pour débloquer", Toast.LENGTH_SHORT).show()
            }
        }

        buttonClassic.setOnClickListener{//Start a new game

            val intent : Intent = if (jsonObject.get("Tutorial").toString()=="true"){
                Intent(activity,GameActivity::class.java)
            } else {
                Intent(activity,TutorielActivity::class.java).apply {
                    putExtra("previousScreen","MainMenu")
                }
            }
            jsonObject.put("GameMode","classic")
            jsonObject.put("PubNumber", 1)
            localData.saveData(this.requireActivity(), jsonObject)
            startActivity(intent)

       }
        return view
    }


    private fun askPermission(jsonObject : JSONObject, localData: LocalData)
    {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(permission.ACTIVITY_RECOGNITION)) {
                explain()
            } else {
                askForPermission()
            }
        } else {
            //call()
            val intent : Intent = if (jsonObject.get("Tutorial").toString()=="true"){
                Intent(activity,GameActivity::class.java)
            } else {
                Intent(activity,TutorielActivity::class.java).apply {
                    putExtra("previousScreen","MainMenu")
                }
            }
            jsonObject.put("GameMode","sport")
            jsonObject.put("PubNumber", 1)
            localData.saveData(this.requireActivity(), jsonObject)
            startActivity(intent)
        }
    }

    private fun askForPermission() {
        requestPermissions(this.requireActivity(), arrayOf(permission.ACTIVITY_RECOGNITION), 2)
    }

    private fun explain() {
        Snackbar.make(
            this.requireView(),
            "Une permission est nécessaire pour utiliser l'option SPORT",
            Snackbar.LENGTH_LONG
        ).setAction("Activer", object : View.OnClickListener {
            override fun onClick(view: View?) {
                askForPermission()
            }
        }).show()
    }


}