package com.keremkulac.okeyscore.util

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import java.util.Locale

fun updateResources(activity: Activity,locale: Locale) {
    val resources = activity.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun updateTheme(isNightModeActive : Boolean,darkModeSwitch : SwitchCompat){
    if(!isNightModeActive){
        darkModeSwitch.isChecked = false
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }else{
        darkModeSwitch.isChecked = true
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}