package com.keremkulac.okeyscore.ui.finishedPartnerGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedPartnerGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {
    private val _allFinishedGamesPartnerGame = MutableLiveData<ArrayList<FinishedPartnerGame?>>()
    val finishedPartnerGameGame: LiveData<ArrayList<FinishedPartnerGame?>>
        get() = _allFinishedGamesPartnerGame
    init {
        getFinishedGames()
    }
   private fun getFinishedGames(){
        viewModelScope.launch {
            _allFinishedGamesPartnerGame.postValue(ArrayList(okeyScoreRepository.getAllFinishedGames()))
        }
    }

    fun search(newText : String?,adapter: FinishedPartnerGameAdapter){
        val query = newText?.lowercase()
        if (query.isNullOrEmpty()) {
            _allFinishedGamesPartnerGame.value?.let { adapter.updateData(it) }
        } else {
            /*
            val filteredList = _allFinishedGames.value?.filter { item ->
                item!!.team1Name.lowercase().contains(query) || item.team2Name.lowercase().contains(query) || item.date.lowercase().contains(query)
            }
            adapter.updateData(ArrayList(filteredList))

             */
        }
    }

    fun deleteFinishedGame(finishedPartnerGame: FinishedPartnerGame?){
        viewModelScope.launch {
            okeyScoreRepository.deleteFinishedGame(finishedPartnerGame)
        }
    }

    fun saveFinishedGame(finishedPartnerGame: FinishedPartnerGame){
        viewModelScope.launch {
            okeyScoreRepository.insertFinishedGame(finishedPartnerGame)
        }
    }

}