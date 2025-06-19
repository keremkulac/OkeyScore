package com.keremkulac.okeyscore.presentation.ui.saveSingleGame

import android.content.Context
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Info
import com.keremkulac.okeyscore.model.Player
import com.keremkulac.okeyscore.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SaveSingleGameViewModel @Inject constructor(
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

    fun checkPlayerNames(
        playerNames: List<String>,
        errorMessage: String,
    ): Boolean {
        return inputValidation.isAllUsernamesFilled(playerNames, errorMessage) { message ->
            _validationMessage.value = message
        }
    }

    fun sameNamesCheck(
        playerNames: List<String>,
        errorMessage: String
    ): Boolean {
        return inputValidation.checkSamePlayerNames(playerNames, errorMessage) { message ->
            _validationMessage.value = message
        }
    }

    fun checkAllRoundScoreFilled(
        roundScores: List<EditText>,
        errorMessageTemplate: String
    ): Boolean {
        return inputValidation.isAllRoundFieldsFilled(
            roundScores,
            errorMessageTemplate
        ) { message ->
            _validationMessage.value = message
        }
    }

    fun checkPenaltyPlayer(
        penalizedPlayer: String?,
        errorMessagePenalizedPlayer: String,
    ): Boolean {
        return inputValidation.checkPenaltyPlayer(
            penalizedPlayer,
            errorMessagePenalizedPlayer,
        ) { message ->
            _validationMessage.value = message
        }
    }

    fun checkPenaltyValue(
        penaltyValue: String?,
        errorMessagePenaltyValue: String
    ): Boolean {
        return inputValidation.checkPenaltyValue(
            penaltyValue,
            errorMessagePenaltyValue
        ) { message ->
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

}