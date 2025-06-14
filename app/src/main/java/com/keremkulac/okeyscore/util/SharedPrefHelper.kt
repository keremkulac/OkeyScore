package com.keremkulac.okeyscore.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class SharedPrefHelper(context: Context) {
   private var onboardingSharedPreferences = context.getSharedPreferences(IS_ONBORDING_COMPLATED, MODE_PRIVATE)
   private var nightModeSharedPreferences = context.getSharedPreferences(IS_NIGHT_MODE_ACTIVE, MODE_PRIVATE)
   private var languageCodeSharedPreferences = context.getSharedPreferences(SELECTED_LANGUAGE_CODE, MODE_PRIVATE)
   private var languageNameSharedPreferences = context.getSharedPreferences(SELECTED_LANGUAGE_NAME, MODE_PRIVATE)

    fun getOnBoardingSharedPreferencesValue(): Boolean {
        return onboardingSharedPreferences.getBoolean(IS_ONBORDING_COMPLATED, false)
    }

    fun setOnBoardingSharedPreferencesValue(value: Boolean) {
        onboardingSharedPreferences.edit { putBoolean(IS_ONBORDING_COMPLATED, value) }
    }

    fun getNightModeSharedPreferencesValue(): Boolean {
        return nightModeSharedPreferences.getBoolean(IS_NIGHT_MODE_ACTIVE, false)
    }

    fun setNightModeSharedPreferencesValue(value: Boolean) {
        nightModeSharedPreferences.edit { putBoolean(IS_NIGHT_MODE_ACTIVE, value) }
    }

    fun getLanguageCodeSharedPreferencesValue(): String {
        return languageCodeSharedPreferences.getString(SELECTED_LANGUAGE_CODE, TR_CODE)!!
    }

    fun setLanguageCodeSharedPreferencesValue(value: String) {
        languageCodeSharedPreferences.edit { putString(SELECTED_LANGUAGE_CODE, value) }
    }

    fun getLanguageNameSharedPreferencesValue(): String {
        return languageNameSharedPreferences.getString(SELECTED_LANGUAGE_NAME, TURKISH_TR)!!
    }

    fun setLanguageNameSharedPreferencesValue(value: String) {
        languageNameSharedPreferences.edit { putString(SELECTED_LANGUAGE_NAME, value) }
    }
}