package com.keremkulac.okeyscore.util

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.keremkulac.okeyscore.R

fun Context.toast(message: String, iconID: Int) {
    val nullParent: ViewGroup? = null
    val layout = LayoutInflater.from(this).inflate(R.layout.custom_toast_message, nullParent)
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

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density + 0.5f).toInt()
}

fun Fragment.observeValidationMessage(validationMessage: LiveData<String>) {
    validationMessage.observe(viewLifecycleOwner) { message ->
        requireContext().toast(message, R.drawable.ic_warning)
    }
}

fun BottomSheetDialog.hideSystemBars() {
    this.window?.let { window ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}

