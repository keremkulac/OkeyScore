package com.keremkulac.okeyscore.ui.finishedGame

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
class FinishedGameViewModel
@Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) : ViewModel() {
    private val _allFinishedGames = MutableLiveData<ArrayList<Finished?>>()
    val finishedGame: LiveData<ArrayList<Finished?>>
        get() = _allFinishedGames
    init {
        getFinishedGames()
    }
   private fun getFinishedGames(){
        viewModelScope.launch {
            _allFinishedGames.postValue(ArrayList(okeyScoreRepository.getAllFinishedGames()))

        }
    }

    fun search(newText : String?,adapter: FinishedGameAdapter){
        val query = newText?.toLowerCase()
        if (query.isNullOrEmpty()) {
            _allFinishedGames.value?.let { adapter.updateData(it) }
        } else {
            val filteredList = _allFinishedGames.value?.filter { item ->
                item!!.team1Name.toLowerCase().contains(query)
                item!!.team2Name.toLowerCase().contains(query)
                item!!.date.toLowerCase().contains(query)

            }
            adapter.updateData(ArrayList(filteredList))
        }
    }

}