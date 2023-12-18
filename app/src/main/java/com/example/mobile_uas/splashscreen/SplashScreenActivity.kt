package com.example.mobile_uas.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile_uas.R
import com.example.mobile_uas.welcome.WelcomingActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        // Simulate a delay (e.g., 2000 milliseconds) using a handler
        val splashDuration = 2000L
        val intent = Intent(this, WelcomingActivity::class.java)

        val handler = android.os.Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, splashDuration)

    }
}