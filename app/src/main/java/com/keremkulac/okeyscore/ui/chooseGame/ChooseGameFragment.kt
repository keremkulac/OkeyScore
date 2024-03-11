package com.keremkulac.okeyscore.ui.chooseGame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentChooseGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGameFragment : Fragment(R.layout.fragment_choose_game) {

    private lateinit var binding : FragmentChooseGameBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChooseGameBinding.bind(view)
        goToSaveSingleGameFragment()
        goToSavePartnerGameFragment()
        setBottomNavigationVisible()
        onBackPressCancel()
    }

    private fun goToSaveSingleGameFragment(){
        binding.goToSaveSingleGameFragment.setOnClickListener {
            findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSaveSingleGameFragment())
        }
    }

    private fun goToSavePartnerGameFragment(){
        binding.goToSavePartnerGameFragment.setOnClickListener {
            findNavController().navigate(ChooseGameFragmentDirections.actionChooseGameFragmentToSavePartnerGameFragment())
        }
    }

    private fun onBackPressCancel(){
        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }

    private fun setBottomNavigationVisible(){
        val bottomNavigation = requireActivity().findViewById<View>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE
    }
}