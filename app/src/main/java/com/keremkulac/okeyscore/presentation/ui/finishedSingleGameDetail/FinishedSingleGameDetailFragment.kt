package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameDetailFragment : Fragment(R.layout.fragment_finished_single_game_detail) {

    private lateinit var binding : FragmentFinishedSingleGameDetailBinding
    private val viewModel by viewModels<FinishedSingleGameDetailViewModel>()
    private lateinit var expandableLayoutManager : ExpandableLayoutManager
    @Inject
    lateinit var finishedSingleGameDetailAdapter: FinishedSingleGameDetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedSingleGameDetailBinding.bind(view)
        expandableLayoutManager = ExpandableLayoutManager()
        getAndSetFinishedGame()
        goToFinishedGameViewFragment()
        totalScoresCardViewToggle()
    }

    private fun goToFinishedGameViewFragment(){
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action = FinishedSingleGameDetailFragmentDirections.actionFinishedSingleGameDetailFragmentToFinishedGameViewFragment("single")
            findNavController().navigate(action)
        }
    }


    private fun setRecyclerView(finishedSingleGame: FinishedSingleGame) {
        binding.apply {
            player1TotalScore.text = finishedSingleGame.player1?.totalScore ?: ""
            finishedSingleGameDetailAdapter.finishedSingleGame = finishedSingleGame
            roundRecyclerView.adapter = finishedSingleGameDetailAdapter
            roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            finishedSingleGameDetailAdapter.clickListener = { scoreContainer, icon ->
                expandableLayoutManager.toggleLayout(scoreContainer, icon)
            }
            totalScorePlayer1Name.text = finishedSingleGame.player1?.name ?: ""
            totalScorePlayer2Name.text = finishedSingleGame.player2?.name ?: ""
            totalScorePlayer3Name.text = finishedSingleGame.player3?.name ?: ""
            totalScorePlayer4Name.text = finishedSingleGame.player4?.name ?: ""
            player1TotalScore.text = finishedSingleGame.player1?.totalScore ?: "0"
            player2TotalScore.text = finishedSingleGame.player2?.totalScore ?: "0"
            player3TotalScore.text = finishedSingleGame.player3?.totalScore ?: "0"
            player4TotalScore.text = finishedSingleGame.player4?.totalScore ?: "0"

            setScoreDifferences(finishedSingleGame)
            gameDate.text = finishedSingleGame.gameInfo.date
            val infoItems = finishedSingleGame.gameInfo.gameInfo.split(" ")
            val pattern = Pattern.compile("Kazanan takım: (.+?)\\. Skor: (\\d+)")
            val matcher = pattern.matcher(finishedSingleGame.gameInfo.gameInfo)
            if (matcher.find()) {
                gameDetail.text =
                    requireContext().getString(R.string.winning_team_info_text)
                        .format(matcher.group(1), matcher.group(2))
            } else {
                gameDetail.text =
                    requireContext().getString(R.string.winning_team_info_text)
                        .format(infoItems[0], infoItems[1])
            }
        }

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

    private fun totalScoresCardViewToggle() {
        val totalScoresExpandableLayoutManager = ExpandableLayoutManager()
        binding.totalScoresCardView.setOnClickListener {
            totalScoresExpandableLayoutManager.toggleLayout(
                binding.totalScoreContainer,
                binding.totalScoresIcon
            )
        }
    }

}