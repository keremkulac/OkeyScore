package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.util.BannerAdManager
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import com.keremkulac.okeyscore.util.FINISHED_GAME_ID
import com.keremkulac.okeyscore.util.GAME_TYPE_PARTNER
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class FinishedPartnerGameDetailFragment
    :
    BaseFragment<FragmentFinishedPartnerGameDetailBinding>(FragmentFinishedPartnerGameDetailBinding::inflate) {

    private val viewModel: FinishedPartnerGameDetailViewModel by viewModels()

    @Inject
    lateinit var finishedPartnerGameDetailAdapter: FinishedPartnerGameDetailAdapter
    private lateinit var expandableLayoutManager: ExpandableLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdView()
        expandableLayoutManager = ExpandableLayoutManager()
        goToFinishedGameViewFragment()
        getAndSetFinishedGames()
        clickTeam1Layout()
        clickTeam2Layout()
    }

    private fun goToFinishedGameViewFragment() {
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action =
                FinishedPartnerGameDetailFragmentDirections.actionFinishedPartnerGameDetailFragmentToFinishedGameViewFragment(
                    GAME_TYPE_PARTNER
                )
            findNavController().navigate(action)
        }
    }

    private fun setAdView() {
        BannerAdManager.loadBannerAd(binding.adView)
    }

    private fun getAndSetFinishedGames() = with(viewModel) {
        getFinishedGame(requireArguments().getInt(FINISHED_GAME_ID))
        finishedPartnerGameGame.observe(viewLifecycleOwner) {
            it?.let {
                setRecyclerView(it)
                finishedPartnerGameDetailAdapter.numberOfGames = findNumberOfGames(it)
            }
        }
    }

    private fun setRecyclerView(finishedPartnerGame: FinishedPartnerGame) = with(binding) {
        team1Name.text = (finishedPartnerGame.team1Name)
        team2Name.text = (finishedPartnerGame.team2Name)
        team1TotalScore.text =
            getString(R.string.team_total_score_text).format(finishedPartnerGame.team1TotalScore)
        team2TotalScore.text =
            getString(R.string.team_total_score_text).format(finishedPartnerGame.team2TotalScore)
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
                requireContext().getString(R.string.winning_team_info_text2)
                    .format(matcher.group(1), matcher.group(2))
        } else {
            gameDetail.text =
                requireContext().getString(R.string.winning_team_info_text2)
                    .format(infoItems[0], infoItems[1])
        }
    }

    private fun setScoreDifferences(finishedPartnerGame: FinishedPartnerGame) = with(binding) {
        var isClicked = true
        scoreDifferencesTextView.text =
            viewModel.scoreDifferences(finishedPartnerGame, requireContext())
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

    private fun clickTeam1Layout() = with(binding) {
        val expandableLayoutManager = ExpandableLayoutManager()
        team1MainLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(team1Layout, team1MainIcon)
        }
    }

    private fun clickTeam2Layout() = with(binding) {
        val expandableLayoutManager = ExpandableLayoutManager()
        team2MainLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(team2Layout, team2MainIcon)
        }
    }

}
