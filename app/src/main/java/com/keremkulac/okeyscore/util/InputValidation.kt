package com.keremkulac.okeyscore.util

import android.widget.EditText
import javax.inject.Inject

class InputValidation @Inject constructor() {

    fun isAllUsernamesFilled(
        playerNames: List<String>,
        errorMessage: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        val allFilled = playerNames.all { it.isNotBlank() }
        if (!allFilled) {
            validationMessage(errorMessage)
        }
        return allFilled
    }

    fun checkSamePlayerNames(
        playerNames: List<String>,
        errorMessage: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        val distinctNames = playerNames.map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        val hasDuplicates = distinctNames.size != playerNames.count { it.trim().isNotEmpty() }
        if (hasDuplicates) {
            validationMessage(errorMessage)
            return false
        }
        return true
    }

    fun isAllRoundFieldsFilled(
        roundScores: List<EditText>,
        errorMessageTemplate: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        for (editText in roundScores) {
            if (editText.text.toString().trim().isEmpty()) {
                validationMessage(errorMessageTemplate)
                return false
            }
        }
        return true
    }

    fun teamAndPlayerNamesValidation(
        teamName: String,
        player1Name: String,
        player2Name: String,
        errorTeamName: String,
        errorPlayer1Name: String,
        errorPlayer2Name: String,
        errorSameNames: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        when {
            teamName.trim().isEmpty() -> {
                validationMessage(errorTeamName)
                return false
            }

            player1Name.trim().isEmpty() -> {
                validationMessage(errorPlayer1Name)
                return false
            }

            player2Name.trim().isEmpty() -> {
                validationMessage(errorPlayer2Name)
                return false
            }

            player1Name.trim().equals(player2Name.trim(), ignoreCase = true) -> {
                validationMessage(errorSameNames)
                return false
            }

            else -> return true
        }
    }

    fun checkSelectedGame(
        selectedGame: String,
        errorMessage: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        if (selectedGame.isEmpty()) {
            validationMessage(errorMessage)
            return false
        }
        return true
    }

    fun checkPenaltyPlayer(
        penalizedPlayer: String?,
        errorMessagePenalizedPlayer: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        when {
            penalizedPlayer.isNullOrEmpty() -> {
                validationMessage(errorMessagePenalizedPlayer)
                return false
            }

        }
        return true
    }

    fun checkPenaltyValue(
        penaltyValue: String?,
        errorMessagePenaltyValue: String,
        validationMessage: (String) -> Unit
    ): Boolean {
        when {
            penaltyValue.isNullOrEmpty() -> {
                validationMessage(errorMessagePenaltyValue)
                return false
            }
        }
        return true
    }

}
