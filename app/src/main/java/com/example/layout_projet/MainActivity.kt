package com.example.layout_projet


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_layout)
        val message = intent.getStringExtra("previousScreen")
        val test = findViewById<FragmentContainerView>(R.id.fragmentMenu)
        val manager : FragmentManager? = supportFragmentManager
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (message == "loseScreen"){
            // Begin the fragment transition using support fragment manager
            if (manager != null) {

                val transaction: FragmentTransaction = manager.beginTransaction()

                // Replace the fragment on container
                transaction.replace(R.id.fragmentMenu, ShopFragment())

                transaction.addToBackStack(null)

                // Finishing the transition
                transaction.commit()
            }
        }



    }

    override fun onBackPressed() {
        // do nothing.
    }
}