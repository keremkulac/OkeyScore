package com.keremkulac.okeyscore.ui.finishedGameDetail

import android.widget.EditText
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
class FinishedGameDetailViewModel @Inject constructor(
    private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {

    private val _finishedGame = MutableLiveData<Finished?>()
    val finishedGame: LiveData<Finished?>
        get() = _finishedGame

    fun getFinishedGame(id: Int){
        viewModelScope.launch {
            _finishedGame.postValue(okeyScoreRepository.getFinishedGame(id))
        }
    }

    fun setTeamInformations(team1EditTextList : List<EditText>,team2EditTextList : List<EditText>,finished: Finished){
        set(team1EditTextList,finished,"team1",finished.finishedTeam1)
        set(team2EditTextList,finished,"team2",finished.finishedTeam2)
    }

    private fun set(editTextList : List<EditText>,finished: Finished,whichTeam : String,scores : List<String?>?){
        for ((i, editText) in editTextList.withIndex()) {
            if(i == 0){
                if(whichTeam == "team1"){
                    editText.setText(finished.team1Name)
                }else{
                    editText.setText(finished.team2Name)
                }
            }else if(i == 12){
                if(whichTeam == "team1"){
                    editText.setText(finished.team1TotalScore)
                }else{
                    editText.setText(finished.team2TotalScore)
                }
            } else{
                editText.setText(scores?.get(i-1))
            }
        }
    }

}