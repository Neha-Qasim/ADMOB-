package com.neha.admobdemo.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.neha.admobdemo.R
import com.neha.admobdemo.AdsConfig
import com.neha.admobdemo.ads.AdManager
import com.neha.admobdemo.Activity.LoginActivity


open class BaseScreenActivity : AppCompatActivity() {
    protected lateinit var topAdContainer: FrameLayout
    protected lateinit var bottomAdContainer: FrameLayout
    protected lateinit var contentContainer: FrameLayout
    protected lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_screen)

        topAdContainer = findViewById(R.id.topAdContainer)
        bottomAdContainer = findViewById(R.id.bottomAdContainer)
        contentContainer = findViewById(R.id.contentContainer)
        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        attachBanners()
    }

    protected fun attachBanners() {
        try {
            topAdContainer.removeAllViews()
            bottomAdContainer.removeAllViews()

            val top = AdManager.createBanner(this, AdsConfig.banner)
            val bottom = AdManager.createBanner(this, AdsConfig.banner)

            topAdContainer.addView(top)
            bottomAdContainer.addView(bottom)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun inflateScreen(layoutRes: Int): View {
        val v = LayoutInflater.from(this).inflate(layoutRes, contentContainer, false)
        contentContainer.removeAllViews()
        contentContainer.addView(v)
        return v
    }
}  