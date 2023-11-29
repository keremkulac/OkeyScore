package com.keremkulac.okeyscore.ui.savePartnerGame

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.util.SharedPreferencesManager
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.Info
import com.keremkulac.okeyscore.model.Player
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
    fun insertFinishedGame(finishedTeam1: List<String?>?, finishedTeam2: List<String?>?,
                             team1Information : List<EditText>, team2Information: List<EditText>){
        viewModelScope.launch{
            if(team1Information.isNotEmpty() && team2Information.isNotEmpty()){
                val gameInfo = Info(getTeamScoreDifference(team1Information,team2Information,  team1Information[0].text.toString(),  team2Information[0].text.toString()),getCurrentDate())
                val player1 = Player(
                    0,
                    team1Information[0].text.toString(),
                    finishedTeam1,
                    calculateTotalScore(team1Information).toString())

                val player2 = Player(
                    0,
                    team2Information[0].text.toString(),
                    finishedTeam2,
                    calculateTotalScore(team2Information).toString())
                    val finishedPartnerGame = FinishedPartnerGame(0,player1,player2,gameInfo)
                check(finishedPartnerGame)
            }
        }
    }

    private suspend fun check(finishedPartnerGame: FinishedPartnerGame){
        val result = runCatching {
            val affectedRows = okeyScoreRepository.insertFinishedGame(finishedPartnerGame)
            affectedRows
        }
        if (result.isSuccess) {
            val insertedRowCount = result.getOrNull()
            if (insertedRowCount != null ) {
                sharedPreferencesManager.clearStoredData() }
        } else {
            val exception = result.exceptionOrNull()
            println("Hata meydana geldi: ${exception?.message}")
        }
    }

     fun getTeamScoreDifference(team1ScoreList: List<EditText>, team2ScoreList: List<EditText>,team1Name: String,team2Name: String) : String{
        val team1TotalScore = calculateTotalScore(team1ScoreList)
        val team2TotalScore = calculateTotalScore(team2ScoreList)
        if(team1TotalScore > team2TotalScore)
            return "${team2Name} takımı oyunu ${team1TotalScore-team2TotalScore} fark ile kazanmıştır"
        else if(team2TotalScore > team1TotalScore)
            return "${team1Name} takımı oyunu ${team2TotalScore-team1TotalScore} fark ile kazanmıştır"
        return "Oyun eşit skor ile bitmiştir"
    }

    @SuppressLint("SetTextI18n")
    private fun setDifferenceText(team1TotalScore: Int, team2TotalScore: Int, differenceText : TextView, team1Name: String, team2Name: String){
            if(team1TotalScore > team2TotalScore){
                differenceText.text = "Fark : ${team1TotalScore-team2TotalScore}. \n${team2Name} takımı önde"
            }else if(team1TotalScore < team2TotalScore){
                differenceText.text = "Fark : ${team2TotalScore-team1TotalScore}. \n${team1Name} takımı önde"
            }else{
                differenceText.text = "Fark : Skorlar eşit"
            }
    }

     fun setTotal(team1ScoreList : List<EditText>, team2ScoreList: List<EditText>, differenceText : TextView){
         val list =team1ScoreList+team2ScoreList
         for((i, scoreListItem) in list.withIndex()){
             if (i !=0 && i != 12 && i !=13 && i != 25){
                 scoreListItem.addTextChangedListener(object : TextWatcher {
                     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                     @SuppressLint("SetTextI18n")
                     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                         team1ScoreList[12].setText("${calculateTotalScore(team1ScoreList)}")
                         team2ScoreList[12].setText("${calculateTotalScore(team2ScoreList)}")
                         setDifferenceText(calculateTotalScore(team1ScoreList), calculateTotalScore(team2ScoreList),differenceText,team1ScoreList[0].text.toString(),team2ScoreList[0].text.toString())
                     }
                     override fun afterTextChanged(s: Editable?) {}
                 })
             }
        }
    }

     fun calculateTotalScore(scoreList: List<EditText>) : Int{
        var total = 0
        for((i, editText) in scoreList.withIndex()){
            if (i > 0 && i < 12){
                val inputValue = editText.text.toString()
                if (inputValue.isNotEmpty() && inputValue != "-") {
                    total += inputValue.toInt()
                }
            }
        }
        return total
    }

    fun getTeamScoreInformations(teamInformationEditTexts : List<EditText>): List<String?> {
        val informations = ArrayList<String?>()
        for((i, editText) in teamInformationEditTexts.withIndex()){
            if(i != 0 && i != 12){
                informations.add(editText.text.toString())
            }
        }
        return informations
    }

     fun getSharedPrefAndSetTeamScores(keyName : String,editTextList : List<EditText>){
         val getContinuingTeamScores = getDataFromSharedPreferences(keyName)
         val continuingList = getContinuingTeamScores!!.split(",")
         if (continuingList[0] != "") {
             _isEmpty.postValue(true)
             for ((i, editText) in editTextList.withIndex()) {
                 if(i == 0){
                     editText.setText(continuingList[i])
                 }else if(i == 12){
                     editText.setText(continuingList[i])
                 }else{
                     editText.setText(continuingList[i])
                 }
             }
         }
     }

    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }

   private fun getDataFromSharedPreferences(key : String): String {
        return sharedPreferencesManager.getStoredData(key,"")
    }

    fun saveDataToSharedPreferences(key : String,data: String) {
        sharedPreferencesManager.saveData(key, data)
    }
}
