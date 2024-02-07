package com.keremkulac.okeyscore.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.keremkulac.okeyscore.R

fun Context.toast(message: String,iconID : Int) {
    val nullParent : ViewGroup? = null
    val layout = LayoutInflater.from(this).inflate(R.layout.custom_toast_message, nullParent )
    val textView = layout.findViewById<TextView>(R.id.toastMessageText)
    textView.text = message
    val icon = layout.findViewById<ImageView>(R.id.toastMessageIcon)
    icon.setImageResource(iconID)
    val toast = Toast(this)
    toast.apply {
        setGravity(Gravity.BOTTOM, 0, 100)
        this.view = layout
        duration = Toast.LENGTH_SHORT
        show()
    }
}