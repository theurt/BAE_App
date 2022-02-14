package com.example.layout_projet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Button
import android.widget.ImageView

private const val REQUEST_CODE = 42
class PhotoGame : GameFragment() {

    private var red : Int = (0..255).random()
    private var green : Int = (0..255).random()
    private var blue : Int = (0..255).random()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        //Génération de la couleur à trouver


        //Inflate the layout for this fragment
        val myInflatedView = inflater.inflate(R.layout.photo_game, container, false)

        val couleurATrouver = myInflatedView.findViewById<ImageView>(R.id.couleur)

        //convertir rdb en hex
        //val hex = String.format("#%02x%02x%02x", rougeRand, vertRand, bleuRand)

        couleurATrouver.setBackgroundColor( Color.rgb(this.red, this.green, this.blue) )

        //on accède au bouton de notre layout pour prendre la photo
        val buttonPhoto = myInflatedView.findViewById<Button>(R.id.button_photo)

        //lorsqu'on appuie sur le bouton
        buttonPhoto.setOnClickListener {
            //on appelle l'intent pour ouvrir l'apn du téléphone
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //déclaration de variable nécessaire pour notre test
            val context: Context? = activity
            val packageManager = context!!.packageManager

            //on test si on peut ouvrir l'apn
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
            //si on arrive pas ouvrir l'apn, on affiche un toast au nutella =)
            else {
                Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
        return myInflatedView
    }

    //fonction qui retourne les résultats obtenu avec l'application d'apn via intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //on vérifie si on a bien prit la photo, et que le code de requete défini en variable globale est le meme
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //on converti notre fichier en bitmap
            val takenImage = data?.extras?.get("data") as Bitmap
            //avec ce code il possible d'avoir un aperçu de l'image qu'on vient de prendre, mais de mauvaise qualité
            //via intent seulement 1mb est autorisé, d'ou cette mauvaise résolution
            //pour pallier à cela, on peut enregitrer l'image en locale et l'a réouvrir
            //imageView.setImageBitmap(takenImage)
            //val buttonPhoto = view?.findViewById(R.id.button_photo) as Button
            //on prend les information du pixel au centre
            //val colorss: Int = takenImage.getPixel(takenImage.width / 2, takenImage.height / 2)

            //on obtient les valeurs de rgb, avec des décalages de bits, voir :
            //https://stackoverflow.com/questions/5669501/how-do-you-get-the-rgb-values-from-a-bitmap-on-an-android-device
            //val rouge: Int = colorss and 0xff0000 shr 16
            //val vert: Int = colorss and 0xff00 shr 8
            //val bleu: Int = colorss and 0xff

            val colorPredominante = getDominantColor(takenImage)

            val red = Color.red(colorPredominante)
            val green = Color.green(colorPredominante)
            val blue = Color.blue(colorPredominante)


            //buttonPhoto.text = colorss.toString()
            if(comparePixel(this.red, this.green, this.blue, red, green, blue))
                this.win = true
            else
                this.lose = true

            getDominantColor(takenImage)

        }
    }

    private fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    fun comparePixel(r1: Int, v1: Int, b1: Int, r2: Int, v2: Int, b2: Int): Boolean {
        if(compare2Color(r1,r2) && compare2Color(v1,v2) && compare2Color(b1,b2))
            return true
        return false
    }



    //a est la valeur random et b celle prise par le joueur
    fun compare2Color(a: Int, b: Int) : Boolean {
        if(moinsPos(a, 50) <=  b && b <= plusPos(a , 50))
            return true
        return false
    }

    fun moinsPos(a: Int, b: Int) : Int {
        if(a - b > 0) {
            return a - b
        }
        else {
            return 0
        }
    }

    fun plusPos(a: Int, b: Int) : Int {
        if(a + b <= 255) {
            return a + b
        }
        else {
            return 255
        }


    }


}


