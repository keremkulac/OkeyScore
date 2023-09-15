package com.keremkulac.okeyscore.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ContinuingSharedPref", Context.MODE_PRIVATE)

    fun getStoredData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun clearStoredData(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}