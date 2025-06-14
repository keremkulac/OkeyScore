package com.keremkulac.okeyscore.util

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

fun updateResources(activity: Activity,locale: Locale) {
    Locale.setDefault(locale)
    val resources = activity.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun updateTheme(isNightModeActive : Boolean){
    if(!isNightModeActive){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }else{
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}

fun translateTR(language : String) : String {
    val languageToShow = when (language) {
        TURKISH_TR -> TURKISH_EN
        TURKISH_EN -> TURKISH_TR
        else ->language
    }
    return languageToShow
}

fun translateEN(language : String) : String {
    val languageToShow = when (language) {
        ENGLISH_EN -> ENGLISH_TR
        ENGLISH_TR -> ENGLISH_EN
        else ->language
    }
    return languageToShow
}

fun createAlertDialog(context: Context, view: View, titleResId: Int, positiveButtonText: String, onPositiveClick: () -> Unit): AlertDialog {
    return AlertDialog.Builder(context)
        .setView(view)
        .setTitle(context.getString(titleResId))
        .setPositiveButton(positiveButtonText) { _, _ -> onPositiveClick() }
        .create()
}

