package com.example.mobile_uas.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.admin.AdminActivity
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.example.mobile_uas.welcome.WelcomingActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@SplashScreenActivity)

        // Simulate a delay (e.g., 2000 milliseconds) using a handler
        val splashDuration = 2000L
        val tipe = sharedPreferencesHelper.getUserTipe()
        val isLoggedIn = sharedPreferencesHelper.isLoggedIn()

        if (isLoggedIn){
            if (tipe == 1){
                intent = Intent(this, BottomNavigationActivity::class.java)
            }else if(tipe == 2){
                intent = Intent(this, AdminActivity::class.java)
            }
        }else{
            intent = Intent(this, WelcomingActivity::class.java)
        }

        val handler = android.os.Handler()
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, splashDuration)

    }
}