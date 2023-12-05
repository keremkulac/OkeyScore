package com.keremkulac.okeyscore.ui.saveSingleGame

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveSingleGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {

    fun insertGame(finishedSingleGame: FinishedSingleGame){
        viewModelScope.launch {
            okeyScoreRepository.insertFinishedSingleGame(finishedSingleGame)
        }
    }

    fun createPlayerScoreList(playerScoreEditTextList: List<EditText>) : ArrayList<String>{
        val scoreList = ArrayList<String>()
        for(playerScoreEditText in playerScoreEditTextList){
            scoreList.add(playerScoreEditText.text.toString())
        }
        return scoreList
    }

    fun calculateTotalScore(playerScoreEditTextList: List<EditText>) : Int{
        var totalScore = 0
        for(playerScoreEditText in playerScoreEditTextList){
            totalScore += playerScoreEditText.text.toString().toInt()
        }
        return totalScore
    }

     fun checkList(editTextList : List<EditText>) : Boolean{
        var isEmpty = false
        for (editText in editTextList) {
            Log.d("TAG",editText.text.toString())

            if (editText.text.toString().isEmpty()) {
                isEmpty = true
            }
        }
        return isEmpty
    }

    fun createPlayers(playerNames: List<EditText>, playerScoreLists: List<List<EditText>>): List<Player> {
        val players = mutableListOf<Player>()

        for (i in 0 until playerNames.size) {
            val playerName = playerNames[i].text.toString()
            val playerScores = createPlayerScoreList(playerScoreLists[i])
            val totalScore = calculateTotalScore(playerScoreLists[i]).toString()
            val player = Player(0, playerName, playerScores, totalScore)
            players.add(player)
        }
        return players
    }

}