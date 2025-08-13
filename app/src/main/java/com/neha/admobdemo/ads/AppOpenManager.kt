package com.neha.admobdemo.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.neha.admobdemo.AdsConfig

class AppOpenManager(private val app: Application) : Application.ActivityLifecycleCallbacks {

    init {
        app.registerActivityLifecycleCallbacks(this)
        // Preload the next App Open Ad for later use
        AdManager.loadAppOpen(app, AdsConfig.app_open)
    }

    override fun onActivityResumed(activity: Activity) {
        // Not forcing ads here — SplashActivity handles the first mandatory ad
        // You could optionally auto-show on resume if desired
    }

    // Unused lifecycle methods
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
