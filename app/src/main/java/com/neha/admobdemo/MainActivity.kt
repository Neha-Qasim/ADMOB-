package com.neha.admobdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neha.admobdemo.Activity.Screen1Activity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(Intent(this, com.neha.admobdemo.Activity.LoginActivity::class.java))
        } else {
            startActivity(Intent(this, Screen1Activity::class.java))
        }
        finish()
    }
}
