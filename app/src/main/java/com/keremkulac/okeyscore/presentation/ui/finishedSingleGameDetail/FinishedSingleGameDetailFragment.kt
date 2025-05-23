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
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameDetailFragment : Fragment(R.layout.fragment_finished_single_game_detail) {

    private lateinit var binding : FragmentFinishedSingleGameDetailBinding
    private val viewModel by viewModels<FinishedSingleGameDetailViewModel>()
    @Inject
    lateinit var finishedSingleGameDetailAdapter: FinishedSingleGameDetailAdapter
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
                    when (textView.text) {
                        binding.player1Name.text.toString() -> binding.player1Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                        binding.player2Name.text.toString() -> binding.player2Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                        binding.player3Name.text.toString() -> binding.player3Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                        binding.player4Name.text.toString() -> binding.player4Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                    }
                }
            }
            binding.player1TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player1.totalScore)
            binding.player2TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player2.totalScore)
            binding.player3TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player3.totalScore)
            binding.player4TotalScore.text = getString(R.string.total_score_text,finishedSingleGame.player4.totalScore)
            binding.gameDate.text = finishedSingleGame.gameInfo.date
            val infoItems = finishedSingleGame.gameInfo.gameInfo.split(" ")
            val pattern = Pattern.compile("Kazanan oyuncu: (.+?)\\. Skor: (\\d+)")
            val matcher = pattern.matcher(finishedSingleGame.gameInfo.gameInfo)
            if (matcher.find()) {
                binding.gameDetail.text = requireContext().getString(R.string.winning_player_info_text).format(matcher.group(1),matcher.group(2))
            } else {
                binding.gameDetail.text = requireContext().getString(R.string.winning_player_info_text).format(infoItems[0],infoItems[1])
            }
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