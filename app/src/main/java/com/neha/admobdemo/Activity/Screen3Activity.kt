package com.neha.admobdemo.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.neha.admobdemo.R
import com.neha.admobdemo.ads.AdManager
import com.neha.admobdemo.AdsConfig

class Screen3Activity : BaseScreenActivity() {
    private val loading by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateScreen(R.layout.activity_screen)

        val title = findViewById<TextView>(R.id.screenTitle)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<Button>(R.id.btnBack)

        title.text = "Screen 3"

        // Preload interstitial ad
        AdManager.loadInterstitial(this, AdsConfig.interstitial)

        btnNext.setOnClickListener {
            loading.show()
            AdManager.showInterstitial(this) {
                loading.dismiss()
                startActivity(Intent(this, Screen4Activity::class.java))
            }
        }
        btnBack.setOnClickListener { finish() }
    }
}
