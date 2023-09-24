package com.keremkulac.okeyscore.ui.finishedGameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.Finished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedGameDetailViewModel @Inject constructor(
    private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {

    private val _finishedGame = MutableLiveData<Finished?>()
    val finishedGame: LiveData<Finished?>
        get() = _finishedGame

    fun getFinishedGame(id: Int){
        viewModelScope.launch {
            _finishedGame.postValue(okeyScoreRepository.getFinishedGame(id))
        }
    }

    fun findNumberOfGames(finished: Finished) : Int{
        var numberOfGames = 0
        for(i in 0 until  finished.team1AllScores!!.size){
            if (finished.team1AllScores[i]!! != "" && finished.team2AllScores!![i]!! != "") {
                numberOfGames++
            }
        }
        return numberOfGames
    }

}