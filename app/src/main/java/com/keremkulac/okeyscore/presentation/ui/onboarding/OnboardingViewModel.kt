package com.keremkulac.okeyscore.presentation.ui.onboarding

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.okeyscore.util.IS_ONBORDING_COMPLATED
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    private val _isOnboarding = MutableLiveData<Boolean>()
    val isOnboarding: LiveData<Boolean>
        get() = _isOnboarding

    fun checkIsOnboardingCompleted(sharedPreferences: SharedPreferences) {
        _isOnboarding.value = sharedPreferences.getBoolean(IS_ONBORDING_COMPLATED, false)
    }

}