package com.keremkulac.okeyscore.ui.finishedPartnerGameDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameDetailBinding
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FinishedGameDetailFragment @Inject constructor(
    private val finishedPartnerGameDetailAdapter : FinishedPartnerGameDetailAdapter
) : Fragment(R.layout.fragment_finished_partner_game_detail) {

    private lateinit var binding : FragmentFinishedPartnerGameDetailBinding
    private val viewModel by viewModels<FinishedPartnerGameDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedPartnerGameDetailBinding.bind(view)
        goToFinishedGameFragment()
        getAndSetFinishedGames()
    }

    private fun goToFinishedGameFragment(){
        binding.goToFinishedGameFragment.setOnClickListener {
          //  findNavController().navigate(R.id.action_finishedGameDetailFragment_to_finishedGameFragment)
        }
    }

    private fun getAndSetFinishedGames(){
        viewModel.getFinishedGame(arguments!!.getInt("finishedGameID"))
        viewModel.finishedPartnerGameGame.observe(viewLifecycleOwner){
            it?.let {
                setRecyclerView(it)
                finishedPartnerGameDetailAdapter.numberOfGames = viewModel.findNumberOfGames(it)
            }
        }
    }
    private fun setRecyclerView(finishedPartnerGame: FinishedPartnerGame?){
        binding.team1Name.setText(finishedPartnerGame!!.team1!!.name)
        binding.team2Name.setText(finishedPartnerGame.team2!!.name)
        binding.gameDate.text = finishedPartnerGame.gameInfo.date
        binding.gameDetail.text = finishedPartnerGame.gameInfo.gameInfo
        finishedPartnerGameDetailAdapter.finishedPartnerGame = finishedPartnerGame
        binding.roundRecyclerView.adapter = finishedPartnerGameDetailAdapter
        binding.roundRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}