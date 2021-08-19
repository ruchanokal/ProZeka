package com.ruchanokal.mathmaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.ruchanokal.mathmaster.R

class SplashScreenActivity : AppCompatActivity() {

    var runnable : Runnable = Runnable {  }
    val handler : Handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        handler.postDelayed( object : Runnable{
            override fun run() {

                val intent = Intent(this@SplashScreenActivity,SignInActivity::class.java)
                startActivity(intent)
                finish()


            }


        },750)



    }



}