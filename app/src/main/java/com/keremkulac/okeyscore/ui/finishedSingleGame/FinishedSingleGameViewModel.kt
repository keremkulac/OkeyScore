package com.keremkulac.okeyscore.ui.finishedSingleGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedSingleGameViewModel @Inject constructor(val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {
    private val _allFinishedSingleGames = MutableLiveData<ArrayList<FinishedSingleGame?>>()
    val allFinishedSingleGames: LiveData<ArrayList<FinishedSingleGame?>>
        get() = _allFinishedSingleGames

    init {
        getAllFinishedSingleGame()
    }

    private fun getAllFinishedSingleGame(){
        viewModelScope.launch {
            _allFinishedSingleGames.postValue(ArrayList(okeyScoreRepository.getAllFinishedSingleGames()))
        }
    }
}