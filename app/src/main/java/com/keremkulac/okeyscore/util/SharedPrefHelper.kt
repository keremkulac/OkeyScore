package com.keremkulac.okeyscore.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPrefHelper(context: Context) {
   private var onboardingSharedPreferences = context.getSharedPreferences("isOnboardingCompleted", MODE_PRIVATE)
   private var nightModeSharedPreferences = context.getSharedPreferences("isNightModeActive", MODE_PRIVATE)
   private var languageSharedPreferences = context.getSharedPreferences("selectedLanguage", MODE_PRIVATE)

    fun getOnBoardingSharedPreferencesValue(): Boolean {
        return onboardingSharedPreferences.getBoolean("isOnboardingCompleted", false)
    }

    fun setOnBoardingSharedPreferencesValue(value: Boolean) {
        onboardingSharedPreferences.edit().putBoolean("isOnboardingCompleted", value).apply()
    }

    fun getNightModeSharedPreferencesValue(): Boolean {
        return nightModeSharedPreferences.getBoolean("isNightModeActive", false)
    }

    fun setNightModeSharedPreferencesValue(value: Boolean) {
        nightModeSharedPreferences.edit().putBoolean("isNightModeActive", value).apply()
    }

    fun getLanguageSharedPreferencesValue(): String? {
        return languageSharedPreferences.getString("selectedLanguage", "Türkçe")
    }

    fun setLanguageSharedPreferencesValue(value: String) {
        languageSharedPreferences.edit().putString("selectedLanguage", value).apply()
    }
}