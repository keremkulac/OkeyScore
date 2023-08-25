package com.keremkulac.okeyscore.ui

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
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
import javax.inject.Inject

@HiltViewModel
class SaveGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel(){
    private val _finishedGame = MutableLiveData<Finished?>()
    val finishedGame: LiveData<Finished?>
        get() = _finishedGame

    init {
        getContinuingGame()
    }
    fun insertFinishedGame(  redTeamName : String, blueTeamName : String,
                             finishedRedTeam: List<String?>?, finishedBlueTeam: List<String?>?,
                             redTeamTotalScore : Int, blueTeamTotalScore : Int,context: Context){
        viewModelScope.launch{
            if(redTeamName.isBlank() || blueTeamName.isBlank()){
                Toast.makeText(context,"Lütfen takım isimlerini giriniz",Toast.LENGTH_SHORT).show()
            }else{
                val finished = Finished( 0,redTeamName,blueTeamName,finishedRedTeam,finishedBlueTeam,redTeamTotalScore,blueTeamTotalScore)
                okeyScoreRepository.insertFinishedGame(finished)
            }

        }
    }

    private fun getContinuingGame() = viewModelScope.launch {
            val result = okeyScoreRepository.getFinishedGame(1)
        _finishedGame.postValue(result)
    }

    fun setBackgroundColor(blueTeamLinearLayout : LinearLayout,redTeamLinearLayout : LinearLayout,context: Context
    ,red: Int,blue: Int){

        if(red > blue){
            blueTeamLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            redTeamLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }else{
            blueTeamLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
            redTeamLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }

    }

     fun setTotal( redTeamScoreList : List<EditText>,blueTeamScoreList: List<EditText>,
                   totalScoreRedTeamEditText : EditText, totalScoreBlueTeamEditText : EditText,
                    blueTeamLinearLayout: LinearLayout,redTeamLinearLayout: LinearLayout,context: Context){
         val list =redTeamScoreList+blueTeamScoreList
        for (scoreListItem in list) {
            scoreListItem.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    totalScoreRedTeamEditText.setText(calculateTotalScore(redTeamScoreList).toString())
                    totalScoreBlueTeamEditText.setText(calculateTotalScore(blueTeamScoreList).toString())
                    setBackgroundColor(blueTeamLinearLayout,redTeamLinearLayout,context,
                        calculateTotalScore(redTeamScoreList), calculateTotalScore(blueTeamScoreList))
                }
                override fun afterTextChanged(s: Editable?) {}
            })
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

    fun getRedTeamScores(redTeamEditTexts : List<EditText>): List<String?> {
        val redTeamScores = ArrayList<String?>()
        for(editText in redTeamEditTexts){
            redTeamScores.add(editText.text.toString())
        }
        return redTeamScores
    }

    fun getBlueTeamScores(blueTeamEditTexts : List<EditText>): List<String?> {
        val blueTeamScores = ArrayList<String?>()
        for(editText in blueTeamEditTexts){
            blueTeamScores.add(editText.text.toString())
        }
        return blueTeamScores
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
}
