package com.keremkulac.okeyscore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.Continuing
import com.keremkulac.okeyscore.model.Finished
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel(){

    fun save(continuing: Continuing, finished: Finished){
        viewModelScope.launch{
            okeyScoreRepository.insertContinuingGame(continuing)
            okeyScoreRepository.insertFinishedGame(finished)
        }
    }
}
