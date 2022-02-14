package com.example.layout_projet


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import java.lang.Thread.sleep
import java.util.Random
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;









class GameActivity : AppCompatActivity() , GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    //Temps avant l'affichage du prochain écran
    private val timeBetweenScreen : Long = 350.toLong()

    //Est-ce qu'un mini-jeu est en train de rouler
    private var miniGameActive : Boolean = true

    //Numéro du jeu à lancer
    private var game = 0

    //Score du joueur pour la partie courante
    private var score = 0

    //Chronomètre
    lateinit var timer: CountDownTimer

    //Valeur initiale du chronomètre
    val timerValue = 10

    //Timer affiché ?
    var timerOutAndNotFocus = false

    //Référence vers l'activité
    val act = this

    //Mode de jeu
    private var gameMode  = "classic"

    //Fragment de jeu courant
    private lateinit var fragmentCourant : Fragment

    private val DEBUG_TAG: String? = "Gestures"
    private var mDetector: GestureDetectorCompat? = null

    //Pour les pubs
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "GameActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mini_game)

        MobileAds.initialize(this) {}

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(TAG, adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.e(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })



        val scoreText = findViewById<TextView>(R.id.gameScore)
        scoreText.text = "score: $score"
        val timerText = findViewById<TextView>(R.id.gameTimer)

        val localData = LocalData()
        val jsonObject = localData.readData(this)
        gameMode = jsonObject.getString("GameMode")

        //Initialisation fragment courant
        this.fragmentCourant = supportFragmentManager.findFragmentById(R.id.fragment_game_place)!! as GameFragment

        timerText.text = timerValue.toString()

        timer = object: CountDownTimer(timerValue.toLong() * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished/1000).toString()

                //Le joueur a gagné, on stoppe le chronomètre et on enregistre le score
                if ( fragmentCourant is GameFragment && (fragmentCourant as GameFragment).win){

                    //Stopper le chronometre
                    timer.cancel()

                    //Calculer le score
                    score += (millisUntilFinished/1000).toInt()
                    val scoreText = findViewById<TextView>(R.id.gameScore)
                    scoreText.text = "score: " + score

                    //Ecran de victoire
                    if (!act.hasWindowFocus()){
                        timerOutAndNotFocus = true
                    }
                    else{
                        displayWinLayout()
                    }
                }

                //Le joueur a perdu, on stoppe le chronomètre et on enregistre le score
                if ( fragmentCourant is GameFragment && (fragmentCourant as GameFragment).lose){

                    //Stopper le chronometre
                    timer.cancel()

                    //Calculer le score
                    val scoreText = findViewById<TextView>(R.id.gameScore)
                    scoreText.text = "score: " + score

                    //Ecran de victoire
                    if (!act.hasWindowFocus()){
                        timerOutAndNotFocus = true
                    }
                    else{
                        displayLoseLayout()
                    }
                }
            }

            override fun onFinish() {
                if (!act.hasWindowFocus()){
                    timerOutAndNotFocus = true
                }
                else{
                    displayLoseLayout()
                }
            }
        }
        timer.start()
        mDetector = GestureDetectorCompat(this, this)
        // Set the gesture detector as the double tap
        // listener.
        mDetector!!.setOnDoubleTapListener(this)
    }

    override fun onPause(){
        super.onPause()
        timerOutAndNotFocus = false
    }

    override fun onResume() {
        super.onResume()
        if (timerOutAndNotFocus){
            displayLoseLayout()
        }
    }


    //Cache le score et le timer et affiche le fragment de victoire
    private fun displayWinLayout(){
        val fr: Fragment
        this.miniGameActive = false
        fr = Win()

        sleep(timeBetweenScreen)
        replaceMiniGameFragment(fr)

        val scoreText = findViewById<TextView>(R.id.gameScore)
        scoreText.visibility = View.GONE

        val timerText = findViewById<TextView>(R.id.gameTimer)
        timerText.visibility = View.GONE
    }

    //Cache le score et le timer et affiche le fragment de défaite
    private fun displayLoseLayout(){
        val fr: Fragment
        fr = LoseGame()
        miniGameActive = false

        sleep(timeBetweenScreen)
        replaceMiniGameFragment(fr)

        val scoreText = findViewById<TextView>(R.id.gameScore)
        scoreText.visibility = View.GONE

        val timerText = findViewById<TextView>(R.id.gameTimer)
        timerText.visibility = View.GONE
    }

    //Affiche le score et le timer et affiche un mini jeu aléatoire
    private fun displayRandomMiniGame(){
        miniGameActive = true
        val scoreText = findViewById<TextView>(R.id.gameScore)
        scoreText.visibility = View.VISIBLE
        scoreText.text = "score: " + score;
        timer.start()

        val timerText = findViewById<TextView>(R.id.gameTimer)
        timerText.visibility = View.VISIBLE

        val fr: GameFragment
        game = if (gameMode == "classic"){
            Random().nextInt(5)
        } else {
            Random().nextInt(6)
        }

        when (game) {
            0 -> {
                Log.e(DEBUG_TAG, "Compass Game Launch")
                fr = CompassGame()
                game += 1
            }
            1 -> {

                fr = GlassGame()
                Log.e(DEBUG_TAG, "Glass Game Launch")
                game += 1
            }
            2 -> {
                fr = FingerGame()
                Log.e(DEBUG_TAG, "Finger Game Launch")
                game = 0
            }
            3 -> {
                fr = LuminosityGame()
                Log.e(DEBUG_TAG, "Luminosity Game Launch")
                game += 1
            }
            4 -> {
                fr = PhotoGame()
                Log.e(DEBUG_TAG, "Photo Game Launch")
                game += 1
            }
            else -> {
                fr = GpsGame()
                Log.e(DEBUG_TAG, "Gps Game Launch")
                game += 1
            }
        }

        replaceMiniGameFragment(fr)
        this.fragmentCourant = fr
    }

    //Affiche le fragment donné en paramètre
    private fun replaceMiniGameFragment(fr : Fragment){

        val manager = supportFragmentManager

        // Begin the fragment transition using support fragment manager
        val transaction = manager.beginTransaction()

        // Replace the fragment on container
        transaction.replace(R.id.fragment_game_place,fr)
        transaction.addToBackStack(null)

        // Finishing the transition
        transaction.commit()

        this.fragmentCourant = fr
    }

    //Activé lorsque le bouton "Continuer" du fragment de victoire est sélectionné
    //Relance un nouveau mini jeu
    fun nextWinOnClick(v : View){
        displayRandomMiniGame()
    }

    //Activé lorsque le bouton "Continuer" du fragment de défaite est sélectionné
    //Relance un nouveau mini jeu
    fun nextLoseOnClick(v : View){
        val localData = LocalData()
        val jsonObject = localData.readData(this)
        if (jsonObject.getBoolean("PubPremium")){
            if (jsonObject.getInt("PubNumber") > 0){
                jsonObject.put("PubNumber",jsonObject.getInt("PubNumber")-1 )
                localData.saveData(this, jsonObject)
                displayRandomMiniGame()
            }
            else{
                Toast.makeText(this, "Dernière chance déjà utilisée", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if (jsonObject.getInt("PubNumber") > 0){
                jsonObject.put("PubNumber",jsonObject.getInt("PubNumber")-1 )
                localData.saveData(this, jsonObject)

                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.e(TAG, "Ad was dismissed.")
                        displayRandomMiniGame()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        Log.e(TAG, "Ad failed to show.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.e(TAG, "Ad showed fullscreen content.")
                        mInterstitialAd = null
                    }
                }

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)
                } else {
                    Log.e(TAG, "The interstitial ad wasn't ready yet.")
                }
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("OFFRE SUPPRESION DES PUBS")
                var message: String = "Pour pouvoir continuer à jouer sans avoir à regarder de pubs, achetez" +
                        "l'option suppression de pub dans la boutique"
                builder.setMessage(message)

                builder.setPositiveButton("Aller dans la boutique") { dialog, which ->
                    val scoreLoseText = findViewById<TextView>(R.id.loseScore)
                    val scoreValue = scoreLoseText.text.split(" ")

                    var maxScore = kotlin.math.max(
                        jsonObject.get("ClassicScore").toString().toInt(),
                        jsonObject.get("SportScore").toString().toInt()
                    )
                    if (scoreValue[1].toInt()> maxScore){
                        if (jsonObject.get("GameMode") == "classic"){
                            jsonObject.put("ClassicScore", scoreValue[1])
                        }
                        else{
                            jsonObject.put("SportScore", scoreValue[1])
                        }
                        localData.saveData(this, jsonObject)
                    }

                    val intent = Intent(this,MainActivity::class.java).apply {
                        putExtra("previousScreen", "loseScreen")
                    }
                    //intent.putExtra("score",GameActivity.score)
                    startActivity(intent)
                }

                builder.setNegativeButton("Annuler") { _, _ -> }
                builder.show()
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event != null){
            return if (mDetector!!.onTouchEvent(event)) {
                true
            } else super.onTouchEvent(event)
        }
        else {
            return false
        }
    }

    override fun onDown(event: MotionEvent?): Boolean {
        // Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true
    }

    override fun onFling(
        event1: MotionEvent?, event2: MotionEvent?,
        velocityX: Float, velocityY: Float
    ): Boolean {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        Log.d(DEBUG_TAG, "onFling: ")
        return true
    }

    override fun onLongPress(event: MotionEvent?) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
        Log.d(DEBUG_TAG, "onLongPress: ")
    }

    override fun onScroll(
        event1: MotionEvent?, event2: MotionEvent?, distanceX: Float,
        distanceY: Float
    ): Boolean {
        //Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        Log.d(DEBUG_TAG, "onScroll: ")
        return true
    }

    override fun onShowPress(event: MotionEvent?) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
        Log.d(DEBUG_TAG, "onShowPress: ")
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        // Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        Log.d(DEBUG_TAG, "onSingleTapUp: ")
        return true
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        // Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        Log.d(DEBUG_TAG, "onDoubleTap: ")
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            return when (event.actionMasked) {
                //Si on touche l'écran et qu'un mini-jeu est actif, on affiche le fragment de victoire
                MotionEvent.ACTION_DOWN -> {
                    if (this.miniGameActive) {
                        score += 1
                        val scoreText = findViewById<TextView>(R.id.gameScore)
                        scoreText.text = "score: " + score
                        displayWinLayout()
                        timer.cancel()
                    }
                    return true
                }
                else -> return false
            }
        }
        else {
            return false
        }
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        //Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: ")
        return true
    }

    override fun onBackPressed() {
        // do nothing.
    }
}
