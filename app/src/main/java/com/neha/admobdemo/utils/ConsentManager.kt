package com.neha.admobdemo.utils

import android.app.Activity
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object ConsentManager {
    fun requestConsent(activity: Activity, onComplete: (Boolean) -> Unit) {
        val params = ConsentRequestParameters.Builder().build()
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.requestConsentInfoUpdate(activity, params, {
            if (consentInformation.isConsentFormAvailable) {
                UserMessagingPlatform.loadConsentForm(activity, { form ->
                    form.show(activity) {
                        onComplete(true)
                    }
                }, { error ->
                    // can't load consent form — continue
                    onComplete(true)
                })
            } else {
                onComplete(true)
            }
        }, { error ->
            // failed to update consent info
            onComplete(false)
        })
    }
}
