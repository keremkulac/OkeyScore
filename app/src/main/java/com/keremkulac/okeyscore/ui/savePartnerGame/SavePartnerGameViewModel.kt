package com.keremkulac.okeyscore.ui.savePartnerGame

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.keremkulac.okeyscore.util.SharedPreferencesManager
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
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
class SavePartnerGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository,
                    private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel(){
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    fun insertFinishedGame(
        allPlayerScoreEditTextList : ArrayList<ArrayList<EditText>>,
        playerNames: List<EditText>,navController: NavController,
        context: Context
    ){

        viewModelScope.launch {
            okeyScoreRepository.insertFinishedPartnerGame(createFinishedPartnerGame(playerNames,allPlayerScoreEditTextList))
            val action = SavePartnerGameFragmentDirections.actionSavePartnerGameFragmentToFinishedGameViewFragment("partner")
            navController.navigate(action)
            context.toast("Kayıt başarılı.")
        }
    }

    fun createFinishedPartnerGame(playerNames: List<EditText>,
        allPlayerScoreEditTextList: ArrayList<ArrayList<EditText>>
    ): FinishedPartnerGame {
        val players = createPlayers(playerNames, allPlayerScoreEditTextList)
        return FinishedPartnerGame(
            0,
            players[0],
            players[1],
            createInfo(players)
        )
    }

     fun createInfo(player: List<Player>): Info {
        val minScorePlayer = player.minBy { it.totalScore }
        return Info("Oyunu ${minScorePlayer.name} adlı takım toplam ${minScorePlayer.totalScore} skor ile kazanmıştır", getCurrentDate())
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

    fun calculateTotalScore(teamScoreEditTextList: List<EditText>) : Int{
        var totalScore = 0
        for(teamScoreEditText in teamScoreEditTextList){
            if(teamScoreEditText.text.toString() != ""){
                totalScore += teamScoreEditText.text.toString().toInt()
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

    fun createPlayerScoreList(playerScoreEditTextList: List<EditText>) : ArrayList<String>{
        val scoreList = ArrayList<String>()
        for(playerScoreEditText in playerScoreEditTextList){
            scoreList.add(playerScoreEditText.text.toString())
        }
        return scoreList
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
