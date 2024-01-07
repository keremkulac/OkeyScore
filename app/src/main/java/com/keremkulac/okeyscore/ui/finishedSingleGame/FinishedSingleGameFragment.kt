package com.keremkulac.okeyscore.ui.finishedSingleGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameBinding
import com.keremkulac.okeyscore.ui.finishedGameView.FinishedGameViewFragmentDirections
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

    private fun clickFinishedGame(){
        finishedSingleGameAdapter.clickListener={
            val action = FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToFinishedSingleGameDetailFragment(it.id)
            findNavController().navigate(action)
        }
    }

}