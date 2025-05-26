package com.keremkulac.okeyscore.util

import android.content.Context
import android.widget.EditText
import com.keremkulac.okeyscore.R
import javax.inject.Inject

class InputValidation @Inject constructor(private val context: Context) {

    fun isAllUsernamesFilled(
        playerNames: List<String>,
        validationMessage: (String) -> Unit
    ): Boolean {
        val allFilled = playerNames.all { it.isNotBlank() }
        if (!allFilled) {
            validationMessage(context.getString(R.string.validation_message_fill_all_player_names))
        }
        return allFilled
    }

    fun checkSamePlayerNames(
        playerNames: List<String>,
        validationMessage: (String) -> Unit
    ): Boolean {
        val distinctNames = playerNames.map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        val hasDuplicates = distinctNames.size != playerNames.count { it.trim().isNotEmpty() }
        if (hasDuplicates) {
            validationMessage(context.getString(R.string.validation_message_check_same_player_names))
            return false
        }
        return true
    }

    fun isAllRoundFieldsFilled(
        roundScores: List<EditText>,
        lineCount: Int,
        validationMessage: (String) -> Unit
    ): Boolean {
        for (editText in roundScores) {
            if (editText.text.toString().trim().isEmpty()) {
                validationMessage(
                    context.getString(R.string.warning_check_all_rounds).format(lineCount)
                )
                return false
            }
        }
        return true
    }
}


