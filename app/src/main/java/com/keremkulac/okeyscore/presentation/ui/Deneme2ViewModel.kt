package com.keremkulac.okeyscore.presentation.ui


import android.content.Context
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.model.FinishedPartnerGame
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
class Deneme2ViewModel @Inject constructor(
    private val okeyScoreRepositoryImp: OkeyScoreRepositoryImp,
    private val inputValidation: InputValidation
) :
    ViewModel() {

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun savePartnerGame(finishedPartnerGame: FinishedPartnerGame) {
        viewModelScope.launch {
            okeyScoreRepositoryImp.insertFinishedPartnerGame(finishedPartnerGame)
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

    fun checkAllRoundScoreFilled(roundScores: List<EditText>, lineCount: Int): Boolean {
        return inputValidation.isAllRoundFieldsFilled(roundScores, lineCount) { message ->
            _validationMessage.value = message
        }
    }

    fun checkTeamAndPlayerNames(
        teamName: String,
        player1Name: String,
        player2Name: String
    ): Boolean {
        return inputValidation.teamAndPlayerNamesValidation(
            teamName,
            player1Name,
            player2Name
        ) { message ->
            _validationMessage.value = message
        }
    }

    fun createInfo(players: List<Player>, teamNames: List<String>, context: Context): Info {

        val team1Score = players[0].totalScore.toInt() + players[1].totalScore.toInt()
        val team2Score = players[2].totalScore.toInt() + players[3].totalScore.toInt()
        var infoText = ""
        val (winningTeamName, winningScore) = when {
            team1Score < team2Score -> teamNames[0] to team1Score
            team1Score > team2Score -> teamNames[1] to team2Score
            else -> null to null
        }

        if (winningTeamName != null && winningScore != null) {
            infoText =
                context.getString(R.string.winning_team_info).format(winningTeamName, winningScore)
        }

        return Info(infoText, getCurrentDate())
    }

    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }

}