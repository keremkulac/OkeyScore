package com.keremkulac.okeyscore.ui

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
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


     fun setTotal( list : List<EditText>,totalEditText : EditText){
        for (editText in list) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var total = 0
                    for((i, editText) in list.withIndex()){
                        if(i>0){
                            val inputValue = editText.text.toString()
                            if (inputValue.isNotEmpty() && inputValue != "-") {
                                total += inputValue.toInt()
                            }
                        }
                    }
                    totalEditText.setText(total.toString())

                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
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
