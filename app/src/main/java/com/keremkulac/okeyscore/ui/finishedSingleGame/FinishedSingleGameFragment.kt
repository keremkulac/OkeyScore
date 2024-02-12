package com.keremkulac.okeyscore.ui.finishedSingleGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameBinding
import com.keremkulac.okeyscore.ui.finishedGameView.FinishedGameViewFragmentDirections
import com.keremkulac.okeyscore.util.SwipeGesture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameFragment @Inject constructor(
    private val finishedSingleGameAdapter: FinishedSingleGameAdapter) : Fragment(R.layout.fragment_finished_single_game) {

    private lateinit var binding : FragmentFinishedSingleGameBinding
    private val viewModel by viewModels<FinishedSingleGameViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedSingleGameBinding.bind(view)
        setRecyclerView()
        observeAllFinishedGame()
        clickFinishedGame()
        search()
        deleteItemDatabase()
    }


    private fun setRecyclerView(){
        binding.finishedGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame(){
        viewModel.allFinishedSingleGames.observe(viewLifecycleOwner){ finishedSingleList->
            if(finishedSingleList.isNotEmpty()){
                binding.recordNotFound.visibility = View.GONE
                finishedSingleGameAdapter.finishedSingleGameLists = ArrayList(finishedSingleList)
                binding.finishedGameRecyclerView.adapter =  finishedSingleGameAdapter

            }else{
                binding.recordNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun observeFilteredList(){
        viewModel.filteredList.observe(viewLifecycleOwner){filteredList->
            if(filteredList.isNotEmpty()){
                binding.recordNotFound.visibility = View.GONE
            }else{
                binding.recordNotFound.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteItemDatabase(){
        val swipeGesture = object  : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val itemToDelete = finishedSingleGameAdapter.finishedSingleGameLists[position]
                viewModel.deleteFinishedGame(itemToDelete)
                val action = FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentSelf("single")
                findNavController().navigate(action)
                Snackbar.make(binding.finishedGameRecyclerView,"Silindi", Snackbar.LENGTH_LONG).setAction(
                    "Geri al"
                ) {
                    viewModel.saveSingleGame(itemToDelete)
                    findNavController().navigate(action)
                }
                    .setBackgroundTint(requireContext().getColor(R.color.snackbar_background_color))
                    .setTextColor(requireContext().getColor(R.color.snackbar_text_color))
                    .setActionTextColor(requireContext().getColor(R.color.snackbar_text_color))
                    .show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.finishedGameRecyclerView)
    }


    private fun search(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText,finishedSingleGameAdapter)
                observeFilteredList()
                if(newText.isNullOrEmpty()){
                    observeAllFinishedGame()
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            observeAllFinishedGame()
            false
        }

        binding.searchView.clearAnimation()
    }

    private fun clickFinishedGame(){
        finishedSingleGameAdapter.clickListener={
            findNavController().navigate(
                FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToFinishedSingleGameDetailFragment(it.id)
            )
        }
    }

}