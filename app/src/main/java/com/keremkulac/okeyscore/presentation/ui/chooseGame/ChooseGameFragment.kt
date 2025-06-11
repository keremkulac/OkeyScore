package com.keremkulac.okeyscore.presentation.ui.chooseGame

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentChooseGameBinding
import com.keremkulac.okeyscore.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGameFragment :
    BaseFragment<FragmentChooseGameBinding>(FragmentChooseGameBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToSaveSingleGameFragment()
        goToSavePartnerGameFragment()
        setBottomNavigationVisible()
        goToSettingsFragment()
        onBackPressCancel()
    }

    private fun goToSaveSingleGameFragment() {
        binding.goToSaveSingleGameFragment.setOnClickListener {
            findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSaveSingleGameFragment())
        }
    }

    private fun goToSavePartnerGameFragment() {
        binding.goToSavePartnerGameFragment.setOnClickListener {
            findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSavePartnerGameFragment())
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

    private fun goToSettingsFragment() {
        binding.settings.setOnClickListener {
            findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSettingsFragment())
        }
    }
}