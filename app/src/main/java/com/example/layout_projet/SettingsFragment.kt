package com.example.layout_projet

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.json.JSONObject


class SettingsFragment : Fragment() {

    //Lecteur de json
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
            R.layout.activity_parametres, container, false
        )

        val editText = view.findViewById(R.id.pseudoModif) as EditText

        //Bouton Retour
        val bouttonRetour = view.findViewById(R.id.btnRetour) as Button
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

        //Boton tuto
        val buttonTutorial = view.findViewById(R.id.btnTutorial) as Button

        buttonTutorial.setOnClickListener {

            val intent : Intent = Intent(activity,TutorielActivity::class.java).apply {
                putExtra("previousScreen","Settings")
            }

            startActivity(intent)
        }

        //Boutton appliquer
        val buttonApply = view.findViewById(R.id.btnAppliquer) as Button

        buttonApply.setOnClickListener {
            val jsonObject = localData?.readData(this.requireActivity())
            jsonObject?.put("Pseudo", editText.text.toString())
            if (jsonObject != null) {
                localData?.saveData(this.requireActivity(), jsonObject)
            }
        }

        //Afficher le pseudo du joueur stocké dans le json
        editText.setText(jsonObject?.get("Pseudo").toString())

        return (view)

    }


}