package com.keremkulac.okeyscore.presentation.ui.finishedSingleGame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.domain.use_case.delete_finished_single_game.DeleteFinishedSingleGameUseCase
import com.keremkulac.okeyscore.domain.use_case.get_all_finished_single_games.GetAllFinishedSingleGamesUseCase
import com.keremkulac.okeyscore.domain.use_case.insert_finished_single_game.InsertFinishedSingleGameUseCase
import com.keremkulac.okeyscore.model.FinishedSingleGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedSingleGameViewModel @Inject constructor(
    private val getAllFinishedSingleGamesUseCase: GetAllFinishedSingleGamesUseCase,
    private val deleteFinishedSingleGameUseCase: DeleteFinishedSingleGameUseCase,
    private val insertFinishedSingleGameUseCase: InsertFinishedSingleGameUseCase
) : ViewModel() {
    private val _allFinishedSingleGames = MutableLiveData<ArrayList<FinishedSingleGame>>()
    val allFinishedSingleGames: LiveData<ArrayList<FinishedSingleGame>>
        get() = _allFinishedSingleGames

    private val _filteredList = MutableLiveData<ArrayList<FinishedSingleGame>>()
    val filteredList: LiveData<ArrayList<FinishedSingleGame>>
        get() = _filteredList
    init {
        getAllFinishedSingleGame()
    }

    private fun getAllFinishedSingleGame(){
        viewModelScope.launch {
            _allFinishedSingleGames.postValue(ArrayList(getAllFinishedSingleGamesUseCase.invoke()))
        }
    }

    fun deleteFinishedGame(finishedSingleGame : FinishedSingleGame){
        viewModelScope.launch {
            deleteFinishedSingleGameUseCase.invoke(finishedSingleGame)
        }
    }


    fun search(newText : String?,adapter: FinishedSingleGameAdapter){
        val query = newText?.lowercase()
        if (query.isNullOrEmpty()) {
            _allFinishedSingleGames.value?.let { adapter.updateData(it) }
        } else {
            val filteredList = _allFinishedSingleGames.value?.filter { finishedSingleGame ->
                finishedSingleGame.player1!!.name.lowercase().contains(query) ||
                        finishedSingleGame.player2!!.name.lowercase().contains(query) ||
                        finishedSingleGame.player3!!.name.lowercase().contains(query) ||
                        finishedSingleGame.player4!!.name.lowercase().contains(query) ||
                        finishedSingleGame.gameInfo.date.lowercase().contains(query)
            }
            _filteredList.postValue(filteredList?.let { ArrayList(it) })
            filteredList?.let { ArrayList(it) }?.let { adapter.updateData(it) }
        }
    }

    fun saveSingleGame(finishedSingleGame: FinishedSingleGame){
        viewModelScope.launch {
            insertFinishedSingleGameUseCase.invoke(finishedSingleGame)
        }
    }
}