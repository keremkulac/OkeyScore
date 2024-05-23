package com.keremkulac.okeyscore.presentation.ui.saveSingleGame

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
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
@Inject constructor(private val okeyScoreRepositoryImp: OkeyScoreRepositoryImp) : ViewModel() {

    fun insertSingleGame(
        allPlayerScoreEditTextList : List<List<EditText>>,
        allPlayerPenaltyTextViewList: List<List<TextView>>,
        playerNames: List<EditText>,
        totalScoreHasMap : HashMap<String,TextView>,
        navController: NavController,
        context: Context){
        viewModelScope.launch {
            val finishedSingleGame = createFinishedSingleGame(playerNames,allPlayerScoreEditTextList,allPlayerPenaltyTextViewList,totalScoreHasMap,context)
            okeyScoreRepositoryImp.insertFinishedSingleGame(finishedSingleGame)
            navController.navigate(
                SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToChooseGameFragment()
            )
            context.toast(context.getString(R.string.registration_successful), R.drawable.ic_successful)
        }
    }

    private fun createFinishedSingleGame(
        playerNames: List<EditText>,
        allPlayerScoreEditTextList: List<List<EditText>>,
        allPlayerPenaltyTextViewList : List<List<TextView>>,
        totalScoreHasMap : HashMap<String,TextView>,
        context: Context
    ): FinishedSingleGame {
        val players = createPlayers(context,playerNames, allPlayerScoreEditTextList,allPlayerPenaltyTextViewList,totalScoreHasMap)
        return FinishedSingleGame(
            0,
            players[0],
            players[1],
            players[2],
            players[3],
            createInfo(players,context)
        )
    }
   private fun createPlayerScoreList(playerScoreEditTextList: List<EditText>) : ArrayList<String>{
        val scoreList = ArrayList<String>()
        for(playerScoreEditText in playerScoreEditTextList){
            scoreList.add(playerScoreEditText.text.toString())
        }
        return scoreList
    }

    private fun createPlayerPenaltyList(context: Context,playerPenalties: List<TextView>) : ArrayList<String>{
        val penaltyList = ArrayList<String>()
        for(playerPenaltiesTextView in playerPenalties){
            if(playerPenaltiesTextView.text.toString() == ""){
                penaltyList.add("")
            }else{
                penaltyList.add(playerPenaltiesTextView.text.split(context.getString(R.string.penalty_text))[1].trim())
            }
        }
        return penaltyList
    }
    fun calculateTotalScore(teamScoreEditTextList: List<EditText>) : Int{
        var totalScore = 0
        for(teamScoreEditText in teamScoreEditTextList){
            if(teamScoreEditText.text.toString() != ""){
                if(teamScoreEditText.text.contains("-")){
                    if(teamScoreEditText.text.count() > 1){
                        val score = teamScoreEditText.text.toString().split("-")
                        totalScore -= score[1].toInt()
                    }
                }else{
                    totalScore += teamScoreEditText.text.toString().toInt()
                }
            }
        }
        return totalScore
    }

    fun calculatePenalties(context: Context,penaltyList: List<TextView>) : Int{
        var totalScore = 0
        for(penaltyTextView in penaltyList){
            val penaltyText = penaltyTextView.text
            if(penaltyText != ""){
                totalScore+= penaltyText.split(context.getString(R.string.penalty_text))[1].trimStart().toInt()
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
    fun sameNamesCheck(editTextList: List<EditText>): Boolean {
        val seenTexts = mutableSetOf<String>()
        for (editText in editTextList) {
            val text = editText.text.toString()
            if (text in seenTexts) {
                return true
            } else {
                seenTexts.add(text)
            }
        }
        return false
    }

   private fun createPlayers(context: Context,playerNames: List<EditText>, playerScoreLists: List<List<EditText>>,playerPenaltyLists : List<List<TextView>>,totalScoreHasMap: HashMap<String, TextView>): List<Player> {
        val players = mutableListOf<Player>()
        for (i in playerNames.indices) {
            val playerName = playerNames[i].text.toString()
            val playerScores = createPlayerScoreList(playerScoreLists[i])
            val totalScore = totalScoreHasMap[playerName]!!.text.toString()
            val playerPenalties = createPlayerPenaltyList(context,playerPenaltyLists[i])
            val player = Player(0, playerName, playerScores, totalScore, playerPenalties)
            players.add(player)
        }
        return players
    }

    fun setTotalScore(context: Context,playerScoreList : List<EditText>,totalScoreTextView: TextView,penaltyList: List<TextView>){
        for (scoreListItem in  playerScoreList){
            scoreListItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val total = calculateTotalScore(playerScoreList)+calculatePenalties(context,penaltyList)
                    totalScoreTextView.text = total.toString()
                }
                override fun afterTextChanged(s: Editable?) {
                }
            })
        }

    }

    private fun createInfo(player: List<Player>,context: Context): Info {

        val minScorePlayer = player.minBy { it.totalScore.toInt() }
        return Info(context.getString(R.string.winning_player_info).format(minScorePlayer.name,minScorePlayer.totalScore), getCurrentDate())
    }
    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }

    fun areAllEditTextsFilled(allPlayerScoreEditTextList: List<List<EditText>>,saveGameButton : View): Boolean {
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