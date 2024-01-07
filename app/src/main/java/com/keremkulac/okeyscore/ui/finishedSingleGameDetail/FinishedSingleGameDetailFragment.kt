package com.keremkulac.okeyscore.ui.finishedSingleGameDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedSingleGame
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameDetailFragment @Inject constructor(
    private val finishedSingleGameDetailAdapter: FinishedSingleGameDetailAdapter): Fragment(R.layout.fragment_finished_single_game_detail) {

    private lateinit var binding : FragmentFinishedSingleGameDetailBinding
    private val viewModel by viewModels<FinishedSingleGameDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedSingleGameDetailBinding.bind(view)
        getAndSetFinishedGame()
    }

    private fun setRecyclerView(finishedSingleGame: FinishedSingleGame?){
        finishedSingleGame?.let {
            binding.player1Name.setText(finishedSingleGame.player1!!.name)
            binding.player2Name.setText(finishedSingleGame.player2!!.name)
            binding.player3Name.setText(finishedSingleGame.player3!!.name)
            binding.player4Name.setText(finishedSingleGame.player4!!.name)
            binding.gameDate.text = finishedSingleGame.gameInfo.date
            binding.gameDetail.text = finishedSingleGame.gameInfo.gameInfo
            finishedSingleGameDetailAdapter.finishedSingleGame = finishedSingleGame
            binding.roundRecyclerView.adapter = finishedSingleGameDetailAdapter
            binding.roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun getAndSetFinishedGame(){
        viewModel.getFinishedSingleGame(arguments!!.getInt("finishedGameID"))
        viewModel.finishedSingleGame.observe(viewLifecycleOwner){
            it?.let {
                setRecyclerView(it)
                finishedSingleGameDetailAdapter.numberOfGames = viewModel.findNumberOfGames(it)
            }
        }
    }

}