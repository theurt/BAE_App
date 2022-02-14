package com.example.layout_projet

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ShopFragment : Fragment() {

    private var localData : LocalData? = null

    //Fichier json
    private var jsonObject : JSONObject? = null

    //Charger les préférences depuis le json
    override fun onCreate(savedInstanceState: Bundle?) {

        this.localData = LocalData()
        this.jsonObject = localData?.readData(this.requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {

        val view : View = inflater.inflate(
            R.layout.shop, container, false
        )

        val bouttonRetour = view.findViewById(R.id.shopBack) as Button

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

        val buttonPremium = view.findViewById(R.id.premiumShop) as Button

        buttonPremium.setOnClickListener {
            val jsonObject = localData?.readData(this.requireActivity())
            if (jsonObject != null) {
                if (jsonObject.get("PubPremium").toString() == "false" && jsonObject.get("SportPremium").toString()=="false"){
                    premiumPopUp(jsonObject)
                }
                else if (jsonObject.get("PubPremium").toString() == "false" && jsonObject.get("SportPremium").toString()=="true"){
                    pubPopUp(jsonObject)
                }
                else if (jsonObject.get("PubPremium").toString() == "true" && jsonObject.get("SportPremium").toString()=="false"){
                    sportPopUp(jsonObject)
                }
                else {
                    Toast.makeText(activity, "Offre déjà acquise", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val buttonSport = view.findViewById(R.id.sportShop) as Button

        buttonSport.setOnClickListener {
            val jsonObject = localData?.readData(this.requireActivity())
            if (jsonObject != null) {
                if (jsonObject.get("SportPremium").toString()=="false"){
                    sportPopUp(jsonObject)
                }
                else{
                    Toast.makeText(activity, "Offre déjà acquise", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val buttonNoPubShop = view.findViewById(R.id.noPubShop) as Button

        buttonNoPubShop.setOnClickListener {
            val jsonObject = localData?.readData(this.requireActivity())
            if (jsonObject != null) {
                if (jsonObject.get("PubPremium").toString()=="false"){
                    pubPopUp(jsonObject)
                }
                else{
                    Toast.makeText(activity, "Offre déjà acquise", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return (view)
    }

    private fun pubPopUp(jsonObject: JSONObject){
        val hours = compareDate(jsonObject)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("OFFRE SUPPRESION DES PUBS")
        var message: SpannableString
        if (hours >= 10){
            message = SpannableString("Souhaitez vous souscrire à l'offre de suppression des pubs à $2.99?")
        }
        else{
            val mStrikeThrough = StrikethroughSpan() //strike through
            message = SpannableString("Souhaitez vous souscrire à l'offre de suppression des pubs à $2.99 $1.49?")
            message.setSpan(mStrikeThrough, 61, 66, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        }
        builder.setMessage(message)

        builder.setPositiveButton("Oui") { dialog, which ->
            jsonObject.put("PubPremium", true)
            localData?.saveData(this.requireActivity(), jsonObject)
        }

        builder.setNegativeButton("Non") { _, _ -> }
        builder.show()
    }

    fun sportPopUp(jsonObject: JSONObject){
        val hours = compareDate(jsonObject)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("OFFRE SPORT")
        var message: SpannableString
        if (hours >= 10){
            message = SpannableString("Souhaitez vous souscrire à l'offre sport à $2.99?")
        }
        else{
            val mStrikeThrough = StrikethroughSpan() //strike through
            message = SpannableString("Souhaitez vous souscrire à l'offre sport à $2.99 $1.49?")
            message.setSpan(mStrikeThrough, 43, 48, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        }
        builder.setMessage(message)

        builder.setPositiveButton("Oui") { dialog, which ->
            jsonObject.put("SportPremium", true)
            localData?.saveData(this.requireActivity(), jsonObject)
        }

        builder.setNegativeButton("Non") { _, _ -> }
        builder.show()
    }

    fun premiumPopUp(jsonObject: JSONObject){
        val hours = compareDate(jsonObject)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("OFFRE PREMIUM")
        var message: SpannableString
        if (hours >= 10){
            message = SpannableString("Souhaitez vous souscrire à l'offre premium comprenant la partie sport " +
                    "ainsi que la suppression des pubs à $4.99?")
        }
        else{
            val mStrikeThrough = StrikethroughSpan() //strike through
            message = SpannableString("Souhaitez vous souscrire à l'offre premium comprenant la partie sport " +
                    "ainsi que la suppression des pubs à $4.99 $2.49?")
            message.setSpan(mStrikeThrough, 106, 111, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        }
        builder.setMessage(message)

        builder.setPositiveButton("Oui") { dialog, which ->
            jsonObject.put("PubPremium", true)
            jsonObject.put("SportPremium", true)
            localData?.saveData(this.requireActivity(), jsonObject)
        }

        builder.setNegativeButton("Non") { _, _ -> }
        builder.show()
    }

    private fun compareDate(jsonObject: JSONObject): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val startDate = jsonObject.getString("FirstStartDate")
        val date1: Date = sdf.parse(startDate)

        val currentDate = sdf.format(Date())
        val date2: Date = sdf.parse(currentDate)
        val diff: Long = date2.time - date1.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return hours
    }
}