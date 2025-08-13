package com.neha.admobdemo.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.neha.admobdemo.AdsConfig

object AdManager {

    private const val TAG = "AdManager"
    private const val MAX_AD_AGE = 4 * 60 * 60 * 1000 // 4 hours in ms

    // ===================== App Open =====================
    private var appOpenAd: AppOpenAd? = null
    private var appOpenLoadTime: Long = 0

    fun loadAppOpen(context: Context, adUnitId: String = AdsConfig.app_open) {
        if (isAppOpenAdAvailable()) {
            Log.d(TAG, "AppOpen already loaded, skipping load")
            return
        }
        Log.d(TAG, "Loading AppOpen Ad...")
        AppOpenAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    appOpenLoadTime = System.currentTimeMillis()
                    Log.d(TAG, "✅ AppOpen Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    appOpenAd = null
                    Log.w(TAG, "❌ AppOpen Ad failed: ${loadAdError.message}")
                }
            }
        )
    }

    private fun isAppOpenAdAvailable(): Boolean {
        return appOpenAd != null && (System.currentTimeMillis() - appOpenLoadTime) < MAX_AD_AGE
    }

    fun showAppOpen(activity: Activity, onComplete: () -> Unit) {
        if (!isAppOpenAdAvailable()) {
            Log.d(TAG, "No AppOpen Ad available to show")
            onComplete()
            return
        }
        appOpenAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "AppOpen Ad dismissed")
                    appOpenAd = null
                    onComplete()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.w(TAG, "AppOpen Ad failed to show: ${adError.message}")
                    appOpenAd = null
                    onComplete()
                }
            }
            ad.show(activity)
        }
    }

    // ===================== Banner =====================
    fun createBanner(context: Context, adUnitId: String = AdsConfig.banner): AdView {
        return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    }

    // ===================== Interstitial =====================
    private var interstitialAd: InterstitialAd? = null

    fun loadInterstitial(context: Context, adUnitId: String = AdsConfig.interstitial) {
        Log.d(TAG, "Loading Interstitial Ad...")
        InterstitialAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d(TAG, "✅ Interstitial Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    Log.w(TAG, "❌ Interstitial failed: ${loadAdError.message}")
                }
            })
    }

    fun showInterstitial(activity: Activity, onComplete: () -> Unit) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    Log.d(TAG, "Interstitial dismissed")
                    onComplete()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    Log.w(TAG, "Interstitial failed to show: ${adError.message}")
                    onComplete()
                }
            }
            ad.show(activity)
        } ?: run {
            Log.d(TAG, "No Interstitial Ad to show")
            onComplete()
        }
    }

    // ===================== Rewarded =====================
    private var rewardedAd: RewardedAd? = null

    fun loadRewarded(context: Context, adUnitId: String = AdsConfig.rewarded) {
        if (rewardedAd != null) {
            Log.d(TAG, "Rewarded Ad already loaded, skipping load")
            return
        }
        Log.d(TAG, "Loading Rewarded Ad...")
        RewardedAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d(TAG, "✅ Rewarded Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    rewardedAd = null
                    Log.w(TAG, "❌ Rewarded failed: ${loadAdError.message}")
                }
            })
    }

    fun showRewarded(activity: Activity, onComplete: (Boolean) -> Unit) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    Log.d(TAG, "Rewarded dismissed")
                    onComplete(false)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    Log.w(TAG, "Rewarded failed to show: ${adError.message}")
                    onComplete(false)
                }
            }
            ad.show(activity) { onComplete(true) }
        } ?: run {
            Log.d(TAG, "No Rewarded Ad to show")
            onComplete(false)
        }
    }

    fun isRewardedLoaded(): Boolean = rewardedAd != null

    // ===================== Native =====================
    private var nativeAd: NativeAd? = null

    fun loadNative(context: Context, adUnitId: String = AdsConfig.native, onLoaded: (NativeAd?) -> Unit) {
        Log.d(TAG, "Loading Native Ad...")
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                nativeAd?.destroy() // cleanup old ad to prevent memory leaks
                nativeAd = ad
                Log.d(TAG, "✅ Native Ad loaded")
                onLoaded(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "❌ Native ad failed: ${loadAdError.message}")
                    onLoaded(null)
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun destroyNative() {
        nativeAd?.destroy()
        nativeAd = null
    }
}
