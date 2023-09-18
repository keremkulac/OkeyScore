package com.keremkulac.okeyscore.ui.finishedGameDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedGameDetailFragment : Fragment(R.layout.fragment_finished_game_detail) {
    private lateinit var binding : FragmentFinishedGameDetailBinding
    private val viewModel by viewModels<FinishedGameDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedGameDetailBinding.bind(view)
        goToFinishedGameFragment()
        getAndSetFinishedGames()
    }

    private fun team1EditTexts(): List<EditText> {
        return listOf(
            binding.team1Name,
            binding.team1Score1,
            binding.team1Score2,
            binding.team1Score3,
            binding.team1Score4,
            binding.team1Score5,
            binding.team1Score6,
            binding.team1Score7,
            binding.team1Score8,
            binding.team1Score9,
            binding.team1Score10,
            binding.team1Score11,
            binding.team1TotalScore
        )
    }
    private fun team2EditTexts(): List<EditText> {
        return listOf(
            binding.team2Name,
            binding.team2Score1,
            binding.team2Score2,
            binding.team2Score3,
            binding.team2Score4,
            binding.team2Score5,
            binding.team2Score6,
            binding.team2Score7,
            binding.team2Score8,
            binding.team2Score9,
            binding.team2Score10,
            binding.team2Score11,
            binding.team2TotalScore
        )
    }



    private fun goToFinishedGameFragment(){
        binding.goToFinishedGameFragment.setOnClickListener {
            findNavController().navigate(R.id.action_finishedGameDetailFragment_to_finishedGameFragment)
        }
    }

    private fun getAndSetFinishedGames(){
        viewModel.getFinishedGame(arguments!!.getInt("finishedGameID"))
        viewModel.finishedGame.observe(viewLifecycleOwner){
            viewModel.setTeamInformations(team1EditTexts(),team2EditTexts(),it!!)
        }
    }
}