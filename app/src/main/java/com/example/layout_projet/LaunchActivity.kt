package com.example.layout_projet

import androidx.appcompat.app.AppCompatActivity
import android.widget.VideoView
import android.os.Bundle
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer
import android.content.Intent
import android.net.Uri
import android.view.View
import java.lang.Thread.sleep

class LaunchActivity : AppCompatActivity() {
    var video: VideoView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bienvenu)
        video = findViewById<View>(R.id.anim_demarrage) as VideoView
        video!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.anim_demarrage))
        //Afficher une miniature en attendant de lancer la vidéo
        video!!.seekTo( 1 )


        //Permission de lire le son

        // video finish listener
        video!!.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer) {
                sleep(500)
                val intent : Intent = Intent(applicationContext,PseudoSelectionActivity::class.java)
                startActivity(intent)
            }
        })


    }

    public override fun onStart() {
        super.onStart()
        video!!.start()
    }

    public override fun onStop(){
        video = null
        super.onStop()
    }

    override fun onBackPressed() {
        // do nothing.
    }
}