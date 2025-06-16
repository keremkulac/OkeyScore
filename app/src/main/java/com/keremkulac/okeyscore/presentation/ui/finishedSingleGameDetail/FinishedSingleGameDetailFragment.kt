package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import com.keremkulac.okeyscore.util.FINISHED_GAME_ID
import com.keremkulac.okeyscore.util.GAME_TYPE_SINGLE
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameDetailFragment :
    BaseFragment<FragmentFinishedSingleGameDetailBinding>(FragmentFinishedSingleGameDetailBinding::inflate) {

    private val viewModel: FinishedSingleGameDetailViewModel by viewModels()
    private lateinit var expandableLayoutManager: ExpandableLayoutManager

    @Inject
    lateinit var finishedSingleGameDetailAdapter: FinishedSingleGameDetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandableLayoutManager = ExpandableLayoutManager()
        getAndSetFinishedGame()
        goToFinishedGameViewFragment()
    }

    private fun goToFinishedGameViewFragment() {
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action =
                FinishedSingleGameDetailFragmentDirections.actionFinishedSingleGameDetailFragmentToFinishedGameViewFragment(
                    GAME_TYPE_SINGLE
                )
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView(finishedSingleGame: FinishedSingleGame) = with(binding) {
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
                requireContext().getString(R.string.winning_team_info_text2)
                    .format(matcher.group(1), matcher.group(2))
        } else {
            gameDetail.text =
                requireContext().getString(R.string.winning_team_info_text2)
                    .format(infoItems[0], infoItems[1])
        }

    }

    private fun getAndSetFinishedGame() = with(viewModel) {
        getFinishedSingleGame(requireArguments().getInt(FINISHED_GAME_ID))
        finishedSingleGame.observe(viewLifecycleOwner) {
            it?.let {
                setRecyclerView(it)
                finishedSingleGameDetailAdapter.numberOfGames = findNumberOfGames(it)
            }
        }
    }

    private fun setScoreDifferences(finishedSingleGame: FinishedSingleGame) = with(binding) {
        var isClicked = true
        scoreDifferencesTextView.text =
            viewModel.scoreDifferences(finishedSingleGame, requireContext())
        showScoreDifferencesTextView.setOnClickListener {
            if (isClicked) {
                scoreDifferencesTextView.visibility = View.VISIBLE
                showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_detail), null
                )
                isClicked = false
            } else {
                scoreDifferencesTextView.visibility = View.GONE
                showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_show_detail), null
                )
                isClicked = true
            }
        }
    }

}