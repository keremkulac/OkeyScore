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

    private val _selectedLanguageCode = MutableLiveData<String?>()
    val selectedLanguageCode: LiveData<String?> get() = _selectedLanguageCode


    private val _selectedLanguageName = MutableLiveData<String?>()
    val selectedLanguageName: LiveData<String?> get() = _selectedLanguageName

    init {
        getNightModeSharedPreferencesValue()
        getLanguageCodeSharedPreferencesValue()
        getLanguageNameSharedPreferencesValue()
    }

    fun getNightModeSharedPreferencesValue() {
        _isNightModeActive.value = sharedPrefHelper.getNightModeSharedPreferencesValue()
    }

    fun setNightModeSharedPreferencesValue(value: Boolean) {
        sharedPrefHelper.setNightModeSharedPreferencesValue(value)
        _isNightModeActive.value = value
    }

    private fun getLanguageCodeSharedPreferencesValue() {
        _selectedLanguageCode.value = sharedPrefHelper.getLanguageCodeSharedPreferencesValue()
    }

    fun setLanguageCodeSharedPreferencesValue(value: String) {
        sharedPrefHelper.setLanguageCodeSharedPreferencesValue(value)
        _selectedLanguageCode.value = value
    }

    private fun getLanguageNameSharedPreferencesValue() {
        _selectedLanguageName.value = sharedPrefHelper.getLanguageNameSharedPreferencesValue()
    }

    fun setLanguageNameSharedPreferencesValue(value: String) {
        sharedPrefHelper.setLanguageNameSharedPreferencesValue(value)
        _selectedLanguageName.value = value
    }

}