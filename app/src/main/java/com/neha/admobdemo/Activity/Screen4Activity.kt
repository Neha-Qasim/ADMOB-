package com.neha.admobdemo.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.neha.admobdemo.R

class Screen4Activity : BaseScreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateScreen(R.layout.activity_screen)

        val title = findViewById<TextView>(R.id.screenTitle)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<Button>(R.id.btnBack)

        title.text = "Screen 4"

        btnNext.setOnClickListener {
            startActivity(Intent(this, Screen5Activity::class.java))
        }
        btnBack.setOnClickListener { finish() }
    }
}
