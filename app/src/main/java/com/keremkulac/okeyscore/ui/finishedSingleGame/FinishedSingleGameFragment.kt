package com.keremkulac.okeyscore.ui.finishedSingleGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedSingleGameFragment : Fragment() {

    private lateinit var binding : FragmentFinishedSingleGameBinding
    private val viewModel by viewModels<FinishedSingleGameViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishedSingleGameBinding.inflate(inflater)
        setRecyclerView()
        observeAllFinishedGame()
        return binding.root
    }

    private fun setRecyclerView(){
        binding.finishedGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame(){
        viewModel.allFinishedSingleGames.observe(viewLifecycleOwner){ finishedSingleList->
            if(finishedSingleList.isNotEmpty()){
                binding.recordNotFound.visibility = View.GONE
                val finishedSingleGameAdapter = FinishedSingleGameAdapter()
                finishedSingleGameAdapter.finishedSingleGameLists = ArrayList(finishedSingleList)
                binding.finishedGameRecyclerView.adapter =  finishedSingleGameAdapter

            }else{
                binding.recordNotFound.visibility = View.VISIBLE
            }
        }
    }

}