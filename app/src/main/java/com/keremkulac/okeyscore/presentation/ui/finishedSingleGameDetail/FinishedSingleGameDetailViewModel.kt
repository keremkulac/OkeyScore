package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.domain.use_case.get_finished_single_game.GetFinishedSingleGameUseCase
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedSingleGameDetailViewModel
@Inject constructor(
    private val getFinishedSingleGameUseCase: GetFinishedSingleGameUseCase
) : ViewModel(){


    private val _finishedSingleGame = MutableLiveData<FinishedSingleGame?>()
    val finishedSingleGame: LiveData<FinishedSingleGame?>
        get() = _finishedSingleGame

    fun getFinishedSingleGame(id: Int){
        viewModelScope.launch {
            _finishedSingleGame.postValue(getFinishedSingleGameUseCase.invoke(id))
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


     fun sortByMin(finishedSingleGame: FinishedSingleGame) : List<Player>{
        val players = listOf(
            finishedSingleGame.player1!!,
            finishedSingleGame.player2!!,
            finishedSingleGame.player3!!,
            finishedSingleGame.player4!!)
        return players.sortedBy { it.totalScore.toInt() }
    }

    fun scoreDifferences(finishedSingleGame: FinishedSingleGame,context: Context) : String {
        val minScorePlayer = sortByMin(finishedSingleGame)[0]
        var result = ""
        for(player in sortByMin(finishedSingleGame)){
            if(minScorePlayer.totalScore.toInt() != player.totalScore.toInt()){
                val scoreDifference =  player.totalScore.toInt() - minScorePlayer.totalScore.toInt()
                result += context.getString(R.string.score_difference).format(minScorePlayer.name,player.name,scoreDifference)+"\n"
            }
        }
        return result
    }
}


