package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.domain.use_case.get_finished_partner_game.GetFinishedPartnerGameUseCase
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedPartnerGameDetailViewModel @Inject constructor(
    private val finishedPartnerGameUseCase: GetFinishedPartnerGameUseCase
) : ViewModel() {

    private val _finishedPartnerGame = MutableLiveData<FinishedPartnerGame?>()
    val finishedPartnerGameGame: LiveData<FinishedPartnerGame?>
        get() = _finishedPartnerGame

    fun getFinishedGame(id: Int) {
        viewModelScope.launch {
            _finishedPartnerGame.postValue(finishedPartnerGameUseCase.invoke(id))
        }
    }

    fun findNumberOfGames(finishedPartnerGame: FinishedPartnerGame): Int {
        var numberOfGames = 0
        for (i in 0 until finishedPartnerGame.team1Player1!!.allScores!!.size) {
            if (finishedPartnerGame.team1Player1.allScores!![i]!! != "" && finishedPartnerGame.team2Player1!!.allScores!![i]!! != "") {
                numberOfGames++
            }
        }
        return numberOfGames
    }


    fun scoreDifferences(finishedPartnerGame: FinishedPartnerGame, context: Context): String {
        val result =
            if (finishedPartnerGame.team1TotalScore > finishedPartnerGame.team2TotalScore) {
                context.getString(R.string.score_difference).format(
                    finishedPartnerGame.team1Name,
                    finishedPartnerGame.team2Name,
                    finishedPartnerGame.team1TotalScore - finishedPartnerGame.team2TotalScore
                )
            } else {
                context.getString(R.string.score_difference).format(
                    finishedPartnerGame.team2Name,
                    finishedPartnerGame.team1Name,
                    finishedPartnerGame.team2TotalScore - finishedPartnerGame.team1TotalScore
                )
            }
        return result
    }
}