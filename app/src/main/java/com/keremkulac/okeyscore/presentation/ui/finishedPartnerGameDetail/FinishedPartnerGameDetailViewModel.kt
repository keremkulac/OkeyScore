package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedPartnerGameDetailViewModel @Inject constructor(
    private val okeyScoreRepositoryImp: OkeyScoreRepositoryImp) : ViewModel() {

    private val _finishedPartnerGame = MutableLiveData<FinishedPartnerGame?>()
    val finishedPartnerGameGame: LiveData<FinishedPartnerGame?>
        get() = _finishedPartnerGame

    fun getFinishedGame(id: Int){
        viewModelScope.launch {
            _finishedPartnerGame.postValue(okeyScoreRepositoryImp.getFinishedPartnerGame(id))
        }
    }

    fun findNumberOfGames(finishedPartnerGame: FinishedPartnerGame) : Int{
        var numberOfGames = 0
        for(i in 0 until  finishedPartnerGame.team1!!.allScores!!.size){
            if (finishedPartnerGame.team1.allScores!![i]!! != "" && finishedPartnerGame.team2!!.allScores!![i]!! != "") {
                numberOfGames++
            }
        }
        return numberOfGames
    }

    fun sortByMin(finishedPartnerGame: FinishedPartnerGame) : List<Player>{
        val players = listOf(
            finishedPartnerGame.team1!!,
            finishedPartnerGame.team2!!)
        return players.sortedBy { it.totalScore.toInt() }
    }

    fun scoreDifferences(finishedPartnerGame: FinishedPartnerGame,context: Context) : String{
        var scoreDifference = 0
        val result = if(finishedPartnerGame.team1!!.totalScore > finishedPartnerGame.team2!!.totalScore){
            scoreDifference =  finishedPartnerGame.team1.totalScore.toInt() - finishedPartnerGame.team2.totalScore.toInt()
            context.getString(R.string.score_difference).format(finishedPartnerGame.team2.name,finishedPartnerGame.team1.name,scoreDifference)
            }else{
            scoreDifference =  finishedPartnerGame.team2.totalScore.toInt() - finishedPartnerGame.team1.totalScore.toInt()
            context.getString(R.string.score_difference).format(finishedPartnerGame.team1.name,finishedPartnerGame.team2.name,scoreDifference)

        }
        return result
    }
}