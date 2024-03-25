package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
        goToFinishedGameViewFragment()
    }

    private fun goToFinishedGameViewFragment(){
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action = FinishedSingleGameDetailFragmentDirections.actionFinishedSingleGameDetailFragmentToFinishedGameViewFragment("single")
            findNavController().navigate(action)
        }
    }

    private fun createPlayerNameTextViewList() : List<TextView>{
        return listOf(binding.player1Name,binding.player2Name,binding.player3Name,binding.player4Name)
    }

    private fun setRecyclerView(finishedSingleGame: FinishedSingleGame){
            binding.player1Name.text = finishedSingleGame.player1!!.name
            binding.player2Name.text = finishedSingleGame.player2!!.name
            binding.player3Name.text = finishedSingleGame.player3!!.name
            binding.player4Name.text = finishedSingleGame.player4!!.name
            for(textView in createPlayerNameTextViewList()){
                if(textView.text ==  viewModel.sortByMin(finishedSingleGame)[0].name){
                    textView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                        ContextCompat.getDrawable(requireContext(),R.drawable.ic_trophy),null)
                }
            }
            binding.player1TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player1.totalScore)
            binding.player2TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player2.totalScore)
            binding.player3TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player3.totalScore)
            binding.player4TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player4.totalScore)
            binding.gameDate.text = finishedSingleGame.gameInfo.date
            binding.gameDetail.text = finishedSingleGame.gameInfo.gameInfo
            finishedSingleGameDetailAdapter.finishedSingleGame = finishedSingleGame
            binding.roundRecyclerView.adapter = finishedSingleGameDetailAdapter
            binding.roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            setScoreDifferences(finishedSingleGame)
    }

    private fun getAndSetFinishedGame(){
        viewModel.getFinishedSingleGame(requireArguments().getInt("finishedGameID"))
        viewModel.finishedSingleGame.observe(viewLifecycleOwner){
            it?.let {
                setRecyclerView(it)
                finishedSingleGameDetailAdapter.numberOfGames = viewModel.findNumberOfGames(it)
            }
        }
    }

    private fun setScoreDifferences(finishedSingleGame: FinishedSingleGame){
        var isClicked = true
        binding.scoreDifferencesTextView.text = viewModel.scoreDifferences(finishedSingleGame,requireContext())
        binding.showScoreDifferencesTextView.setOnClickListener {
            if(isClicked){
                binding.scoreDifferencesTextView.visibility = View.VISIBLE
                binding.showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_close_detail),null)
                isClicked = false
            }else{
                binding.scoreDifferencesTextView.visibility = View.GONE
                binding.showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,
                    ContextCompat.getDrawable(requireContext(),R.drawable.ic_show_detail),null)
                isClicked = true
            }
        }
    }

}