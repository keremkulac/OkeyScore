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
    val finishedPartnerGame: LiveData<ArrayList<FinishedPartnerGame?>>
        get() = _allFinishedGamesPartnerGame

    private val _filteredList = MutableLiveData<ArrayList<FinishedPartnerGame?>>()
    val filteredList: LiveData<ArrayList<FinishedPartnerGame?>>
        get() = _filteredList
    init {
        getFinishedGames()
    }
   private fun getFinishedGames(){
        viewModelScope.launch {
            _allFinishedGamesPartnerGame.postValue(ArrayList(okeyScoreRepository.getAllFinishedPartnerGames()))
        }
    }

    fun search(newText : String?,adapter: FinishedPartnerGameAdapter){
        val query = newText?.lowercase()
        if (query.isNullOrEmpty()) {
            _allFinishedGamesPartnerGame.value?.let { adapter.updateData(it) }
        } else {
            val filteredList = _allFinishedGamesPartnerGame.value?.filter { finishedPartnerGame ->
                finishedPartnerGame!!.team1!!.name.lowercase().contains(query) || finishedPartnerGame.team2!!.name.lowercase().contains(query) || finishedPartnerGame.gameInfo.date.lowercase().contains(query)
            }
            _filteredList.postValue(ArrayList(filteredList))

            adapter.updateData(ArrayList(filteredList))
        }
    }

    fun deleteFinishedGame(finishedPartnerGame: FinishedPartnerGame?){
        viewModelScope.launch {
            okeyScoreRepository.deleteFinishedPartnerGame(finishedPartnerGame)
        }
    }

    fun saveFinishedGame(finishedPartnerGame: FinishedPartnerGame){
        viewModelScope.launch {
            okeyScoreRepository.insertFinishedPartnerGame(finishedPartnerGame)
        }
    }

}