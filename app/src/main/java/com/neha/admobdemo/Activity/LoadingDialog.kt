package com.neha.admobdemo.Activity

import android.app.AlertDialog
import android.app.Activity
import android.view.LayoutInflater
import com.neha.admobdemo.R

class LoadingDialog(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    fun show() {
        if (dialog?.isShowing == true) return
        val builder = AlertDialog.Builder(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null)
        builder.setView(view).setCancelable(false)
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        try {
            dialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
