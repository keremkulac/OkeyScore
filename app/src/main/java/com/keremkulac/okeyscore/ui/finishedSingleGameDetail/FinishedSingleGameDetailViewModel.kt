package com.keremkulac.okeyscore.ui.finishedSingleGameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedSingleGameDetailViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel(){


    private val _finishedSingleGame = MutableLiveData<FinishedSingleGame?>()
    val finishedSingleGame: LiveData<FinishedSingleGame?>
        get() = _finishedSingleGame

    fun getFinishedSingleGame(id: Int){
        viewModelScope.launch {
            _finishedSingleGame.postValue(okeyScoreRepository.getFinishedSingleGame(id))
        }
    }

    fun findNumberOfGames(finishedSingleGame: FinishedSingleGame) : Int{
        var numberOfGames = 0
        for(i in 0 until  finishedSingleGame.player1!!.allScores!!.size){
            if (finishedSingleGame.player1.allScores!![i]!! != "" && finishedSingleGame.player2!!.allScores!![i]!! != ""  && finishedSingleGame.player3!!.allScores!![i]!! != "" && finishedSingleGame.player4!!.allScores!![i]!! != "") {
                numberOfGames++
            }
        }
        return numberOfGames
    }
}