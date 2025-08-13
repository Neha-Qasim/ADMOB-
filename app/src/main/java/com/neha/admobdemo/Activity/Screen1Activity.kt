package com.neha.admobdemo.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.nativead.NativeAdView
import com.neha.admobdemo.R
import com.neha.admobdemo.ads.AdManager
import com.neha.admobdemo.AdsConfig


class Screen1Activity : BaseScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateScreen(R.layout.activity_screen)

        val title = findViewById<TextView>(R.id.screenTitle)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnReward = findViewById<Button>(R.id.btnReward)
        val nativeContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)

        title.text = "Screen 1"

        btnNext.setOnClickListener {
            startActivity(Intent(this, Screen2Activity::class.java))
        }
        btnBack.setOnClickListener { finish() }

        // Preload rewarded ad
        AdManager.loadRewarded(this, AdsConfig.rewarded)

        btnReward.setOnClickListener {
            if (AdManager.isRewardedLoaded()) {
                AdManager.showRewarded(this) { rewarded ->
                    if (rewarded) Toast.makeText(this, "Reward earned!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Rewarded ad not ready", Toast.LENGTH_SHORT).show()
                AdManager.loadRewarded(this, AdsConfig.rewarded)
            }
        }

        // Load native ad
        AdManager.loadNative(this, AdsConfig.native) { ad ->
            nativeContainer.removeAllViews()
            ad?.let {
                val adView = LayoutInflater.from(this)
                    .inflate(R.layout.native_ad_layout, nativeContainer, false) as NativeAdView
                // TODO: Bind ad assets (headline, body, icon) to adView
                nativeContainer.addView(adView)
            }
        }
    }}