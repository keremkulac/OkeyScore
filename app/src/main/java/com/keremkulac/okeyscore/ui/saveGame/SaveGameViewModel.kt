package com.keremkulac.okeyscore.ui.saveGame

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
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
                             redTeamTotalScore : String, blueTeamTotalScore : String,context: Context,sharedPreferences: SharedPreferences){
        viewModelScope.launch{
            if(team1Name.isBlank() || team2Name.isBlank()){
                Toast.makeText(context,"Lütfen takım isimlerini giriniz",Toast.LENGTH_SHORT).show()
                _isTrue.postValue(false)
            }else{
                if(redTeamTotalScore.isNotBlank() && blueTeamTotalScore.isNotBlank()){
                    _isTrue.postValue(true)

                    val finished = Finished( 0,team1Name,team2Name,finishedTeam1,finishedTeam2,
                        splitTotal(redTeamTotalScore),splitTotal(blueTeamTotalScore),getCurrentDate())
                    okeyScoreRepository.insertFinishedGame(finished)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                }else{
                    _isTrue.postValue(false)

                }
            }
        }
    }

    private fun splitTotal(totalScore: String): String {
        return totalScore.split("Toplam: ")[1]
    }

    private fun setBackgroundColor(team1View: View ,team2View: View,context: Context,
                                   team1TotalScore: Int,team2TotalScore: Int){
        if(team1TotalScore > team2TotalScore){
            team1View.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            team2View.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }else if(team1TotalScore < team2TotalScore){
            team2View.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            team1View.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }else{
            team2View.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            team1View.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

     fun setTotal(team1ScoreList : List<EditText>, team2ScoreList: List<EditText>,
                  team1TotalScoreEditText : EditText, team2TotalScoreEditText : EditText,
                  team1View: View, team2View: View,context: Context){
         val list =team1ScoreList+team2ScoreList
         for((i, scoreListItem) in list.withIndex()){
             if (i !=0 && i != 12){
                 scoreListItem.addTextChangedListener(object : TextWatcher {
                     override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                     @SuppressLint("SetTextI18n")
                     override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                         team1TotalScoreEditText.setText("Toplam: ${calculateTotalScore(team1ScoreList)}")
                         team2TotalScoreEditText.setText("Toplam: ${calculateTotalScore(team2ScoreList)}")
                         setBackgroundColor(team1View,team2View,context,
                             calculateTotalScore(team1ScoreList), calculateTotalScore(team2ScoreList))
                     }
                     override fun afterTextChanged(s: Editable?) {}
                 })
             }
        }
    }

    private fun calculateTotalScore(scoreList: List<EditText>) : Int{
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

    fun getTeam1Scores(team1EditTexts : List<EditText>): List<String?> {
        val team1Scores = ArrayList<String?>()
        for(editText in team1EditTexts){
            team1Scores.add(editText.text.toString())
        }
        return team1Scores
    }

    fun getTeam2Score(team2EditTexts : List<EditText>): List<String?> {
        val team2Scores = ArrayList<String?>()
        for(editText in team2EditTexts){
            team2Scores.add(editText.text.toString())
        }
        return team2Scores
    }

     fun getSharedPrefAndSetTeamScores(keyName : String,editTextList : List<EditText>,sharedPreferences : SharedPreferences) {
         val getContinuingTeamScores = sharedPreferences.getString(keyName, "")
         val continuingList = getContinuingTeamScores?.split(",")
         if (continuingList != null) {
             if (getContinuingTeamScores != "") {
                 for ((i, editText) in editTextList.withIndex()) {
                     editText.setText(continuingList[i])
                 }
             }
         }
     }

    private fun getCurrentDate(): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
    }
}
