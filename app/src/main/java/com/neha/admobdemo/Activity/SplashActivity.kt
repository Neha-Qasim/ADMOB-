package com.neha.admobdemo.Activity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.neha.admobdemo.MainActivity

class SplashScreen : AppCompatActivity() {

    private var appOpenAd: AppOpenAd? = null
    private var isAdShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load ad before starting main activity
        loadAppOpenAd()
    }

    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(
            this,
            "ca-app-pub-3940256099942544/3419835294", // Test Ad Unit ID
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    showAdIfAvailable()
                }

                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    // If failed, just continue
                    goToMainActivity()
                }
            }
        )
    }

    private fun showAdIfAvailable() {
        if (appOpenAd != null && !isAdShown) {
            appOpenAd?.show(this as Activity)
            isAdShown = true

            // Delay moving to MainActivity until the ad is closed
            appOpenAd?.fullScreenContentCallback =
                object : com.google.android.gms.ads.FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        goToMainActivity()
                    }
                }
        } else {
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
