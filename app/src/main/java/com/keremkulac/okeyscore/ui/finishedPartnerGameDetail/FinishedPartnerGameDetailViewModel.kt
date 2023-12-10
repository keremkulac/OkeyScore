package com.keremkulac.okeyscore.ui.finishedPartnerGameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedPartnerGameDetailViewModel @Inject constructor(
    private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {

    private val _finishedPartnerGameGame = MutableLiveData<FinishedPartnerGame?>()
    val finishedPartnerGameGame: LiveData<FinishedPartnerGame?>
        get() = _finishedPartnerGameGame

    fun getFinishedGame(id: Int){
        viewModelScope.launch {
            _finishedPartnerGameGame.postValue(okeyScoreRepository.getFinishedPartnerGame(id))
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

}