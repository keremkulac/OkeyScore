package com.keremkulac.okeyscore.presentation.ui.chooseGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.okeyscore.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseGameViewModel @Inject constructor(
    private val inputValidation: InputValidation
) : ViewModel() {
    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage


    fun checkSelectedGame(
        selectedGame: String,
        errorMessage: String
    ): Boolean {
        return inputValidation.checkSelectedGame(selectedGame, errorMessage) { message ->
            _validationMessage.value = message
        }
    }
}