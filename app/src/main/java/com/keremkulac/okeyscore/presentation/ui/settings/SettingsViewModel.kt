package com.keremkulac.okeyscore.presentation.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.okeyscore.util.SharedPrefHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val sharedPrefHelper: SharedPrefHelper) : ViewModel(){
    private val _isNightModeActive = MutableLiveData<Boolean?>()
    val isNightModeActive: LiveData<Boolean?> get() = _isNightModeActive

    private val _selectedLanguage = MutableLiveData<String?>()
    val selectedLanguage: LiveData<String?> get() = _selectedLanguage

    init {
        getNightModeSharedPreferencesValue()
        getLanguageSharedPreferencesValue()
    }

    fun getNightModeSharedPreferencesValue() {
        _isNightModeActive.value = sharedPrefHelper.getNightModeSharedPreferencesValue()
    }

    fun setNightModeSharedPreferencesValue(value: Boolean) {
        sharedPrefHelper.setNightModeSharedPreferencesValue(value)
        _isNightModeActive.value = value
    }

    private fun getLanguageSharedPreferencesValue() {
        _selectedLanguage.value = sharedPrefHelper.getLanguageSharedPreferencesValue()
    }

    fun setLanguageSharedPreferencesValue(value: String) {
        sharedPrefHelper.setLanguageSharedPreferencesValue(value)
        _selectedLanguage.value = value
    }


}