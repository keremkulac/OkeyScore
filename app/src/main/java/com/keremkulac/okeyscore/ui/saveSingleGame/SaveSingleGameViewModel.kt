package com.keremkulac.okeyscore.ui.saveSingleGame

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Info
import com.keremkulac.okeyscore.model.Player
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SaveSingleGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {

    fun insertSingleGame(
        allPlayerScoreEditTextList : ArrayList<ArrayList<EditText>>,
        playerNames: List<EditText>,navController: NavController,
        context: Context){

        viewModelScope.launch {

            okeyScoreRepository.insertFinishedSingleGame(createFinishedSingleGame(playerNames,allPlayerScoreEditTextList))
            val action = SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToFinishedGameViewFragment("single")
            navController.navigate(action)
            context.toast("Kayıt başarılı.")
        }
    }

    fun createFinishedSingleGame(
        playerNames: List<EditText>,
        allPlayerScoreEditTextList: ArrayList<ArrayList<EditText>>
    ): FinishedSingleGame {
        val players = createPlayers(playerNames, allPlayerScoreEditTextList)
        return FinishedSingleGame(
            0,
            players[0],
            players[1],
            players[2],
            players[3],
            createInfo(players)
        )
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
            if(playerScoreEditText.text.toString() != ""){
                totalScore += playerScoreEditText.text.toString().toInt()
            }
        }
        return totalScore
    }

     fun checkList(editTextList : List<EditText>) : Boolean{
        var isEmpty = false
        for (editText in editTextList) {
            if (editText.text.toString().isEmpty()) {
                isEmpty = true
            }
        }
        return isEmpty
    }

   private fun createPlayers(playerNames: List<EditText>, playerScoreLists: List<List<EditText>>): List<Player> {
        val players = mutableListOf<Player>()
        for (i in playerNames.indices) {
            val playerName = playerNames[i].text.toString()
            val playerScores = createPlayerScoreList(playerScoreLists[i])
            val totalScore = calculateTotalScore(playerScoreLists[i]).toString()
            val player = Player(0, playerName, playerScores, totalScore)
            players.add(player)
        }
        return players
    }

    fun setTotalScore(team1ScoreList : List<EditText>,totalScoreTextView: TextView){
        for ((i, scoreListItem) in team1ScoreList.withIndex()) {
            scoreListItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    totalScoreTextView.text = "${calculateTotalScore(team1ScoreList)}"
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun createInfo(player: List<Player>): Info {

        val minScorePlayer = player.minBy { it.totalScore }
        return Info("Oyunu ${minScorePlayer.name} adlı oyuncu toplam ${minScorePlayer.totalScore} skor ile kazanmıştır", getCurrentDate())
    }
    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }

    fun areAllEditTextsFilled(allPlayerScoreEditTextList: ArrayList<ArrayList<EditText>>,saveGameButton : View): Boolean {
        for (editTextList in allPlayerScoreEditTextList) {
            for (editText in editTextList) {
                if (editText.text.isNullOrEmpty()) {
                    saveGameButton.isEnabled = false
                    return true
                }else{
                    saveGameButton.isEnabled = true
                }
            }
        }
        return false
    }

}