package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class FinishedPartnerGameDetailFragment : Fragment(R.layout.fragment_finished_partner_game_detail) {

    private lateinit var binding: FragmentFinishedPartnerGameDetailBinding
    private val viewModel: FinishedPartnerGameDetailViewModel by viewModels()
    @Inject
    lateinit var finishedPartnerGameDetailAdapter: FinishedPartnerGameDetailAdapter
    private lateinit var expandableLayoutManager: ExpandableLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedPartnerGameDetailBinding.bind(view)
        expandableLayoutManager = ExpandableLayoutManager()
        goToFinishedGameViewFragment()
        getAndSetFinishedGames()
    }

    private fun goToFinishedGameViewFragment() {
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action =
                FinishedPartnerGameDetailFragmentDirections.actionFinishedPartnerGameDetailFragmentToFinishedGameViewFragment(
                    "partner"
                )
            findNavController().navigate(action)
        }
    }

    private fun getAndSetFinishedGames() {
        viewModel.getFinishedGame(requireArguments().getInt("finishedGameID"))
        viewModel.finishedPartnerGameGame.observe(viewLifecycleOwner) {
            it?.let {
                setRecyclerView(it)
                finishedPartnerGameDetailAdapter.numberOfGames = viewModel.findNumberOfGames(it)
            }
        }
    }


    private fun setRecyclerView(finishedPartnerGame: FinishedPartnerGame) {
        binding.apply {
            team1Name.text = (finishedPartnerGame.team1Name)
            team2Name.text = (finishedPartnerGame.team2Name)
            team1TotalScore.text = finishedPartnerGame.team1TotalScore.toString()
            team2TotalScore.text = finishedPartnerGame.team2TotalScore.toString()
            finishedPartnerGameDetailAdapter.finishedPartnerGame = finishedPartnerGame
            roundRecyclerView.adapter = finishedPartnerGameDetailAdapter
            roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            finishedPartnerGameDetailAdapter.clickListener = { scoreContainer, icon ->
                expandableLayoutManager.toggleLayout(scoreContainer, icon)
            }
            totalScorePlayer1Name.text = finishedPartnerGame.team1Player1?.name ?: ""
            totalScorePlayer2Name.text = finishedPartnerGame.team1Player2?.name ?: ""
            totalScorePlayer3Name.text = finishedPartnerGame.team2Player1?.name ?: ""
            totalScorePlayer4Name.text = finishedPartnerGame.team2Player2?.name ?: ""
            player1TotalScore.text = finishedPartnerGame.team1Player1?.totalScore ?: "0"
            player2TotalScore.text = finishedPartnerGame.team1Player2?.totalScore ?: "0"
            player3TotalScore.text = finishedPartnerGame.team2Player1?.totalScore ?: "0"
            player4TotalScore.text = finishedPartnerGame.team2Player2?.totalScore ?: "0"
            setScoreDifferences(finishedPartnerGame)
            gameDate.text = finishedPartnerGame.gameInfo.date
            val infoItems = finishedPartnerGame.gameInfo.gameInfo.split(" ")
            val pattern = Pattern.compile("Kazanan takım: (.+?)\\. Skor: (\\d+)")
            val matcher = pattern.matcher(finishedPartnerGame.gameInfo.gameInfo)
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

    private fun setScoreDifferences(finishedPartnerGame: FinishedPartnerGame) {
        var isClicked = true
        binding.scoreDifferencesTextView.text =
            viewModel.scoreDifferences(finishedPartnerGame, requireContext())
        binding.showScoreDifferencesTextView.setOnClickListener {
            if (isClicked) {
                binding.scoreDifferencesTextView.visibility = View.VISIBLE
                binding.showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_close_detail), null
                )
                isClicked = false
            } else {
                binding.scoreDifferencesTextView.visibility = View.GONE
                binding.showScoreDifferencesTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null, null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_show_detail), null
                )
                isClicked = true
            }
        }
    }

}
