package com.keremkulac.okeyscore.presentation.ui.onboarding

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.okeyscore.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor()  : ViewModel() {

    private val _isOnboarding = MutableLiveData<Boolean>()
    val isOnboarding : LiveData<Boolean>
        get() = _isOnboarding


    fun getOnboardingImagesList() : List<Int>{
        return listOf(
            R.drawable.onboarding_1,
            R.drawable.onboarding_2,
            R.drawable.onboarding_3
        )
    }

    fun checkIsOnboardingCompleted(sharedPreferences : SharedPreferences) {
        _isOnboarding.value = sharedPreferences.getBoolean("isOnboardingCompleted", false)
    }

}