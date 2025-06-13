package com.keremkulac.okeyscore.presentation.ui.chooseGame

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentChooseGameBinding
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.observeValidationMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGameFragment :
    BaseFragment<FragmentChooseGameBinding>(FragmentChooseGameBinding::inflate) {
    private val viewModel: ChooseGameViewModel by viewModels()
    private var selectedGame: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNavigationVisible()
        setupClickListeners()
        onBackPressCancel()
        observeValidationMessage(viewModel.validationMessage)
    }


    private fun setupClickListeners() {
        binding.cardViewSingleGame.setOnClickListener {
            selectGame(getString(R.string.single_game))
        }

        binding.cardViewPartnerGame.setOnClickListener {
            selectGame(getString(R.string.partner_game))
        }

        binding.btnStart.setOnClickListener {
            if (viewModel.checkSelectedGame(selectedGame,getString(R.string.warning_check_selected_game))){
                startSelectedFragment()
            }
        }
    }

    private fun selectGame(selectedGame: String) {
        this.selectedGame = selectedGame
        updateSelection(selectedGame)
        when (selectedGame) {
            getString(R.string.single_game) -> animateCardSelection(binding.cardViewSingleGame)
            getString(R.string.partner_game) -> animateCardSelection(binding.cardViewPartnerGame)
        }
    }

    private fun updateSelection(selectedInstrument: String) {
        binding.singleGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)
        binding.partnerGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)

        when (selectedInstrument) {
            getString(R.string.single_game) -> binding.singleGameIndicator.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator)

            getString(R.string.partner_game) -> binding.partnerGameIndicator.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator)
        }
    }

    private fun animateCardSelection(cardView: CardView) {
        cardView.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                cardView.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun startSelectedFragment() {
        when (selectedGame) {
            getString(R.string.single_game) -> findNavController().navigate(
                ChooseGameFragmentDirections.actionChooseGameFragmentToSaveSingleGameFragment()
            )

            getString(R.string.partner_game) -> findNavController().navigate(
                ChooseGameFragmentDirections.actionChooseGameFragmentToSavePartnerGameFragment()
            )
        }
    }

    private fun onBackPressCancel() {
        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }

    private fun setBottomNavigationVisible() {
        val bottomNavigation = requireActivity().findViewById<View>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE
    }

}