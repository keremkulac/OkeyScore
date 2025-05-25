package com.keremkulac.okeyscore.util

import android.content.Context
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
}


