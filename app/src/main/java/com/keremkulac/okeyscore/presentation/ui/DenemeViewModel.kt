package com.keremkulac.okeyscore.presentation.ui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Info
import com.keremkulac.okeyscore.model.Player
import com.keremkulac.okeyscore.presentation.ui.saveSingleGame.SaveSingleGameFragmentDirections
import com.keremkulac.okeyscore.util.InputValidation
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DenemeViewModel @Inject constructor(
    private val okeyScoreRepositoryImp: OkeyScoreRepositoryImp,
    private val inputValidation: InputValidation
) :
    ViewModel() {

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun saveFinishedGame(finishedSingleGame: FinishedSingleGame) {
        viewModelScope.launch {
            okeyScoreRepositoryImp.insertFinishedSingleGame(finishedSingleGame)
        }
    }


    fun checkPlayerNames(playerNames: List<String>): Boolean {
        return inputValidation.isAllUsernamesFilled(playerNames) { message ->
            _validationMessage.value = message
        }
    }

    fun sameNamesCheck(playerNames: List<String>): Boolean {
        return inputValidation.checkSamePlayerNames(playerNames) { message ->
            _validationMessage.value = message
        }
    }

    fun createInfo(player: List<Player>, context: Context): Info {

        val minScorePlayer = player.minBy { it.totalScore.toInt() }
        return Info(
            context.getString(R.string.winning_player_info)
                .format(minScorePlayer.name, minScorePlayer.totalScore), getCurrentDate()
        )
    }

    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }

    fun areAllEditTextsFilled(
        allPlayerScoreEditTextList: List<List<EditText>>,
        saveGameButton: View
    ): Boolean {
        for (editTextList in allPlayerScoreEditTextList) {
            for (editText in editTextList) {
                if (editText.text.isNullOrEmpty()) {
                    saveGameButton.isEnabled = false
                    return true
                } else {
                    saveGameButton.isEnabled = true
                }
            }
        }
        return false
    }
}