package com.keremkulac.okeyscore.ui.saveGame

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.Finished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class SaveGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel(){
    private val _isTrue = MutableLiveData<Boolean>()
    val isTrue: LiveData<Boolean>
        get() = _isTrue

    fun insertFinishedGame(  team1Name : String, team2Name : String,
                             finishedTeam1: List<String?>?, finishedTeam2: List<String?>?,
                             team1TotalScore : String, team2TotalScore : String,gameInfo : String,
                             sharedPreferences: SharedPreferences){
        viewModelScope.launch{
            if(team1TotalScore.isNotBlank() && team2TotalScore.isNotBlank()){
                clearSharedPreferences(sharedPreferences)
                val finished = Finished( 0,team1Name,team2Name,finishedTeam1,finishedTeam2,
                    splitTotal(team1TotalScore),splitTotal(team2TotalScore),gameInfo,getCurrentDate())
                check(finished,sharedPreferences)
            }
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

    private suspend fun check(finished: Finished,sharedPreferences: SharedPreferences){
        val result = runCatching {
            val affectedRows = okeyScoreRepository.insertFinishedGame(finished)
            affectedRows
        }

        if (result.isSuccess) {
            val insertedRowCount = result.getOrNull()
            if (insertedRowCount != null ) {
                _isTrue.postValue(true)
                clearSharedPreferences(sharedPreferences)
            } else {
                _isTrue.postValue(false)
            }
        } else {
            val exception = result.exceptionOrNull()
            println("Hata meydana geldi: ${exception?.message}")
        }


    }

    fun clearSharedPreferences(sharedPreferences: SharedPreferences){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun splitTotal(totalScore: String): String {
        return totalScore.split("Toplam: ")[1]
    }

    @SuppressLint("SetTextI18n")
    private fun setDifferenceText(team1TotalScore: Int, team2TotalScore: Int, differenceText : TextView
                                  , team1Name: String, team2Name: String){
            if(team1TotalScore > team2TotalScore){
                differenceText.text = "Fark : ${team1TotalScore-team2TotalScore}. ${team2Name} takımı önde"
            }else if(team1TotalScore < team2TotalScore){
                differenceText.text = "Fark : ${team2TotalScore-team1TotalScore}. ${team1Name} takımı önde"
            }else{
                differenceText.text = "Fark : Skorlar eşit"
            }
    }

     fun setTotal(team1ScoreList : List<EditText>, team2ScoreList: List<EditText>,
                  team1TotalScoreEditText : EditText, team2TotalScoreEditText : EditText, differenceText : TextView){
         val list =team1ScoreList+team2ScoreList
         for((i, scoreListItem) in list.withIndex()){
             if (i !=0 && i != 12){
                 scoreListItem.addTextChangedListener(object : TextWatcher {
                     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                     }
                     @SuppressLint("SetTextI18n")
                     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                         team1TotalScoreEditText.setText("Toplam: ${calculateTotalScore(team1ScoreList)}")
                         team2TotalScoreEditText.setText("Toplam: ${calculateTotalScore(team2ScoreList)}")
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
            if(i>0){
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
            if (i != 0){
                informations.add(editText.text.toString())
            }
        }
        return informations
    }

     fun getSharedPrefAndSetTeamScores(keyName : String,teamName : String,editTextList : List<EditText>,
                                       sharedPreferences : SharedPreferences,
                                       layout: LinearLayout,scoreContainer : LinearLayout,
                                       arrowButton : ImageButton,differenceText : TextView) {
         val getContinuingTeamScores = sharedPreferences.getString(keyName, "")
         val continuingTeamName = sharedPreferences.getString(teamName, "")
         val continuingList = getContinuingTeamScores?.split(",")
         if (continuingList != null) {
             if (getContinuingTeamScores != "") {
                 layout.visibility = View.GONE
                 scoreContainer.visibility = View.VISIBLE
                 arrowButton.setImageResource(R.drawable.ic_arrow_right)
                 differenceText.visibility = View.VISIBLE
                 for ((i, editText) in editTextList.withIndex()) {
                     if(i == 0){
                         editText.setText(continuingTeamName)
                     }else{
                         editText.setText(continuingList[i-1])
                     }

                 }
             }
         }
     }

    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }
}
