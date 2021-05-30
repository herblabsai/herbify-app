package com.herblabs.herbifyapp.utils

import android.app.Activity
import android.app.AlertDialog
import com.herblabs.herbifyapp.R

class LoadingDialog(var activity: Activity) {
    private var dialog: AlertDialog? = null
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null))
        dialog = builder.create()
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }
}