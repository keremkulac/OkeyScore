package com.keremkulac.okeyscore.ui.finishedGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FinishedGameFragment @Inject constructor(
    private val finishedGameAdapter: FinishedGameAdapter
)  : Fragment(R.layout.fragment_finished_game) {


    private val viewModel by viewModels<FinishedGameViewModel>()
    private lateinit var binding : FragmentFinishedGameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedGameBinding.bind(view)
        observeAllFinishedGame()
        setRecyclerView()
        goToSaveGameFragment()
        searchClick()
    }

    private fun setRecyclerView(){
        binding.finishedGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame(){
        viewModel.finishedGame.observe(viewLifecycleOwner){finishedList->
            if(finishedList.isNotEmpty()){
                binding.recordNotFound.visibility = View.GONE
                finishedGameAdapter.finishedList = finishedList
                binding.finishedGameRecyclerView.adapter =  finishedGameAdapter

            }else{
                binding.recordNotFound.visibility = View.VISIBLE
            }
        }
    }
    private fun searchClick(){
        binding.searchView.setOnSearchClickListener {
            binding.gameHistoryText.visibility = View.GONE
            search()
        }
        binding.searchView.setOnCloseListener {
            binding.gameHistoryText.visibility = View.VISIBLE
            false
        }
    }

    private fun search(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText,finishedGameAdapter)
                return true
            }
        })
    }


    private fun goToSaveGameFragment(){
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_finishedGameFragment_to_saveGameFragment)
        }
    }

}