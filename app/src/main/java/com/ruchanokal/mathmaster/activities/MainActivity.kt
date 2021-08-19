package com.ruchanokal.mathmaster.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.games.LeaderboardsClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.classes.NetworkConnection


class MainActivity : AppCompatActivity() {

    private lateinit var networkConnection: NetworkConnection
    private lateinit var alertDialog: AlertDialog.Builder
    private val RC_SIGN_IN = 9001

    var googleSignInClient : GoogleSignInClient? = null
    var leaderboardClient : LeaderboardsClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        MobileAds.initialize(this)

        supportActionBar?.hide()

        networkCheck()

        signInSilently()


        /*
        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        println("googleApiCheck1: " + resultCode)

        if (resultCode == ConnectionResult.SUCCESS){

            println("gerçek success " + resultCode)

        } else if (resultCode == ConnectionResult.SERVICE_MISSING) {

            println("SERVICE_MISSING")

        } else if (resultCode == ConnectionResult.SERVICE_DISABLED) {

            println("SERVICE_DISABLED")

        } else if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {

            println("SERVICE_VERSION_UPDATE_REQUIRED")

        }

         */


    }





    private fun signInSilently() {

        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            val signedInAccount = account
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            val signInClient = GoogleSignIn.getClient(this, signInOptions)
            signInClient
                .silentSignIn()
                .addOnCompleteListener(
                    this,
                    object : OnCompleteListener<GoogleSignInAccount?> {
                        override fun onComplete(task: Task<GoogleSignInAccount?>) {
                            if (task.isSuccessful()) {
                                // The signed in account is stored in the task's result.
                                val signedInAccount: GoogleSignInAccount? = task.getResult()
                            } else {

                                // Player will need to sign-in explicitly using via UI.
                                // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                                // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                                // Interactive Sign-in.

                                println("Maalesef sign in başarısız oldu" + task.exception)

                                startSignInIntent()

                            }
                        }
                    })
        }
    }

    private fun startSignInIntent() {

        val signInClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
        )
        val intent = signInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result?.isSuccess == true) {

                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount

            } else {
                var message = result?.status?.statusMessage

                println("hataaa: " + message)
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error)
                }
                AlertDialog.Builder(this).setMessage(message)
                    .setNeutralButton(android.R.string.ok, null).show()
            }
        }
    }

    private fun networkCheck() {

        networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->


            if (!isConnected) {

                alertDialog = AlertDialog.Builder(this)

                alertDialog.setTitle(getString(R.string.baglanti_problemi))
                alertDialog.setMessage(getString(R.string.baglanti_problemi_aciklama))
                alertDialog.setCancelable(false)
                alertDialog.setNeutralButton(getString(R.string.baglanti_problemi_button)) {dialog,which ->

                        networkCheck()

                }

                alertDialog.show()

            }


        })

    }




    override fun onBackPressed() {

        super.onBackPressed()
    }







    }






