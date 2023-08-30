package com.keremkulac.okeyscore.ui.finishedGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun setRecyclerView(finishedGames : List<Finished?>){
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