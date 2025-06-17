package com.keremkulac.okeyscore.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import androidx.core.graphics.drawable.toDrawable

fun updateResources(activity: Activity, locale: Locale) {
    Locale.setDefault(locale)
    val resources = activity.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun updateTheme(isNightModeActive: Boolean) {
    if (!isNightModeActive) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

fun translateTR(language: String): String {
    val languageToShow = when (language) {
        TURKISH_TR -> TURKISH_EN
        TURKISH_EN -> TURKISH_TR
        else -> language
    }
    return languageToShow
}

fun translateEN(language: String): String {
    val languageToShow = when (language) {
        ENGLISH_EN -> ENGLISH_TR
        ENGLISH_TR -> ENGLISH_EN
        else -> language
    }
    return languageToShow
}

fun createAlertDialog(
    context: Context,
    view: View,
): AlertDialog {
    val dialog = AlertDialog.Builder(context)
        .setView(view)
        .create()
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    return dialog
}

