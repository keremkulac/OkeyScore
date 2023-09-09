package com.keremkulac.okeyscore.ui.finishedGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameBinding
import com.keremkulac.okeyscore.model.Finished
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class FinishedGameFragment : Fragment() {

    private val viewModel by viewModels<FinishedGameViewModel>()
    private lateinit var binding : FragmentFinishedGameBinding
    private lateinit var finishedGameAdapter: FinishedGameAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishedGameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllFinishedGame()
        goToSaveGameFragment()
        searchClick()
    }
    private fun observeAllFinishedGame(){
        viewModel.finishedGame.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.recordNotFound.visibility = View.INVISIBLE
                setRecyclerView(it)

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

    private fun setRecyclerView(finishedGames : ArrayList<Finished?>){
        finishedGameAdapter = FinishedGameAdapter(finishedGames)
        binding.finishedGameRecyclerView.adapter =  finishedGameAdapter
        binding.finishedGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(true)
    }

    private fun goToSaveGameFragment(){
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_finishedGameFragment_to_saveGameFragment)
        }
    }
}