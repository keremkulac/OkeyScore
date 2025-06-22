package com.keremkulac.okeyscore.presentation.ui.chooseGame

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.MainActivity
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
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeValidationMessage(viewModel.validationMessage)
        onBackPressCancel()
    }

    private fun setupListeners() = with(binding) {
        cardViewSingleGame.setOnClickListener {
            handleGameSelection(GameType.SINGLE)
        }
        cardViewPartnerGame.setOnClickListener {
            handleGameSelection(GameType.PARTNER)
        }
        cancelCardViewSingleGame.setOnClickListener {
            clearGameSelection()
        }
        cancelCardViewPartnerGame.setOnClickListener {
            clearGameSelection()
        }
        start.setOnClickListener {
            if (viewModel.checkSelectedGame(
                    selectedGame,
                    getString(R.string.warning_check_selected_game)
                )
            ) {
                navigateToSelectedGame()
                (requireActivity() as MainActivity).hideBottomNav()
            }
        }
    }

    private fun handleGameSelection(gameType: GameType) {
        (requireActivity() as MainActivity).hideBottomNav()
        selectedGame = getString(gameType.labelRes)
        updateIndicators(gameType)
        animateCardSelection(if (gameType == GameType.SINGLE) binding.cardViewSingleGame else binding.cardViewPartnerGame)
        with(binding) {
            choiceText.text = getString(gameType.labelRes)
            cancelCardViewSingleGame.visibility =
                if (gameType == GameType.SINGLE) View.VISIBLE else View.INVISIBLE
            cancelCardViewPartnerGame.visibility =
                if (gameType == GameType.PARTNER) View.VISIBLE else View.INVISIBLE
            choiceLayout.visibility = View.VISIBLE
        }
    }

    private fun updateIndicators(gameType: GameType) = with(binding) {
        singleGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)
        partnerGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)

        val indicator =
            if (gameType == GameType.SINGLE) singleGameIndicator else partnerGameIndicator
        indicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator)
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

    private fun navigateToSelectedGame() {
        when (selectedGame) {
            getString(R.string.single_game) -> {
                findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSaveSingleGameFragment())
            }

            getString(R.string.partner_game) -> {
                findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSavePartnerGameFragment())
            }
        }
    }

    private fun clearGameSelection() = with(binding) {
        selectedGame = ""
        singleGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)
        partnerGameIndicator.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selection_indicator_inactive)
        cancelCardViewSingleGame.visibility = View.INVISIBLE
        cancelCardViewPartnerGame.visibility = View.INVISIBLE
        choiceLayout.visibility = View.GONE
        (requireActivity() as MainActivity).showBottomNav()
    }

    private fun onBackPressCancel() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
    }

    private enum class GameType(val labelRes: Int) {
        SINGLE(R.string.single_game),
        PARTNER(R.string.partner_game)
    }
}
