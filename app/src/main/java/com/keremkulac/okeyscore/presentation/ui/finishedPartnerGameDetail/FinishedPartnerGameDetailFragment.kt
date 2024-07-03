package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject


@AndroidEntryPoint
class FinishedPartnerGameDetailFragment : Fragment(R.layout.fragment_finished_partner_game_detail) {

    private lateinit var binding : FragmentFinishedPartnerGameDetailBinding
    private val viewModel by viewModels<FinishedPartnerGameDetailViewModel>()
    @Inject
    lateinit var finishedPartnerGameDetailAdapter : FinishedPartnerGameDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedPartnerGameDetailBinding.bind(view)
        goToFinishedGameViewFragment()
        getAndSetFinishedGames()
    }

    private fun goToFinishedGameViewFragment(){
        binding.goToFinishedGameViewFragment.setOnClickListener {
            val action = FinishedPartnerGameDetailFragmentDirections.actionFinishedPartnerGameDetailFragmentToFinishedGameViewFragment("partner")
            findNavController().navigate(action)
        }
    }

    private fun getAndSetFinishedGames(){
        viewModel.getFinishedGame(requireArguments().getInt("finishedGameID"))
        viewModel.finishedPartnerGameGame.observe(viewLifecycleOwner){
            it?.let {
                setRecyclerView(it)
                finishedPartnerGameDetailAdapter.numberOfGames = viewModel.findNumberOfGames(it)
            }
        }
    }

    private fun createTeamNameTextViewList() : List<TextView>{
        return listOf(binding.team1Name,binding.team2Name)
    }
    private fun setRecyclerView(finishedPartnerGame: FinishedPartnerGame){
            binding.team1Name.text = (finishedPartnerGame.team1!!.name)
            binding.team2Name.text = (finishedPartnerGame.team2!!.name)
            for (textView in createTeamNameTextViewList()){
                if(textView.text == viewModel.sortByMin(finishedPartnerGame)[0].name){
                    when (textView.text) {
                        binding.team1Name.text.toString() -> binding.team1Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                        binding.team2Name.text.toString() -> binding.team2Indicator.setBackgroundColor(requireContext().getColor(R.color.game_winner_indicator_color))
                    }
                }
            binding.team1TotalScore.text = getString(R.string.total_score_text,finishedPartnerGame.team1.totalScore)
            binding.team2TotalScore.text = getString(R.string.total_score_text,finishedPartnerGame.team2.totalScore)
            binding.gameDate.text = finishedPartnerGame.gameInfo.date
            val infoItems = finishedPartnerGame.gameInfo.gameInfo.split(" ")
            val pattern = Pattern.compile("Kazanan takım: (.+?)\\. Skor: (\\d+)")
            val matcher = pattern.matcher(finishedPartnerGame.gameInfo.gameInfo)
            if (matcher.find()) {
                binding.gameDetail.text = requireContext().getString(R.string.winning_team_info_text).format(matcher.group(1),matcher.group(2))
            }else{
                binding.gameDetail.text = requireContext().getString(R.string.winning_team_info_text).format(infoItems[0],infoItems[1])
            }
            finishedPartnerGameDetailAdapter.finishedPartnerGame = finishedPartnerGame
            binding.roundRecyclerView.adapter = finishedPartnerGameDetailAdapter
            binding.roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            setScoreDifferences(finishedPartnerGame)
        }
    }

    private fun setScoreDifferences(finishedPartnerGame: FinishedPartnerGame){
        var isClicked = true
        binding.scoreDifferencesTextView.text = viewModel.scoreDifferences(finishedPartnerGame,requireContext())
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