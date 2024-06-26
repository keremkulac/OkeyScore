package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.domain.use_case.delete_finished_partner_game.DeleteFinishedPartnerGameUseCase
import com.keremkulac.okeyscore.domain.use_case.get_all_finished_partner_games.GetAllFinishedPartnerGamesUseCase
import com.keremkulac.okeyscore.domain.use_case.insert_finished_partner_game.InsertFinishedPartnerGameUseCase
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedPartnerGameViewModel
@Inject constructor(
    private val getAllFinishedPartnerGamesUseCase: GetAllFinishedPartnerGamesUseCase,
    private val deleteFinishedPartnerGameUseCase: DeleteFinishedPartnerGameUseCase,
    private val insertFinishedPartnerGameUseCase: InsertFinishedPartnerGameUseCase) : ViewModel() {
    private val _allFinishedGamesPartnerGame = MutableLiveData<List<FinishedPartnerGame>>()
    val finishedPartnerGame: LiveData<List<FinishedPartnerGame>>
        get() = _allFinishedGamesPartnerGame

    private val _filteredList = MutableLiveData<ArrayList<FinishedPartnerGame>>()
    val filteredList: LiveData<ArrayList<FinishedPartnerGame>>
        get() = _filteredList
    init {
        getFinishedGames()
    }
   private fun getFinishedGames(){
        viewModelScope.launch {
            _allFinishedGamesPartnerGame.postValue(getAllFinishedPartnerGamesUseCase.invoke())
        }
    }

    fun search(newText : String?,adapter: FinishedPartnerGameAdapter){
        val query = newText?.lowercase()
        if (query.isNullOrEmpty()) {
            _allFinishedGamesPartnerGame.value?.let { adapter.updateData(it) }
        } else {
            val filteredList = _allFinishedGamesPartnerGame.value?.filter { finishedPartnerGame ->
                finishedPartnerGame.team1!!.name.lowercase().contains(query) || finishedPartnerGame.team2!!.name.lowercase().contains(query) || finishedPartnerGame.gameInfo.date.lowercase().contains(query)
            }
            _filteredList.postValue(filteredList?.let { ArrayList(it) })
            filteredList?.let { ArrayList(it) }?.let { adapter.updateData(it) }
        }
    }

    fun deleteFinishedGame(finishedPartnerGame: FinishedPartnerGame){
        viewModelScope.launch {
            deleteFinishedPartnerGameUseCase.invoke(finishedPartnerGame)
        }
    }

    fun saveFinishedGame(finishedPartnerGame: FinishedPartnerGame){
        viewModelScope.launch {
            insertFinishedPartnerGameUseCase.invoke(finishedPartnerGame)
        }
    }

}