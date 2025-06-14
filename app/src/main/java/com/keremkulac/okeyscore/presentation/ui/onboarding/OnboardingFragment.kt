package com.keremkulac.okeyscore.presentation.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.IS_ONBORDING_COMPLATED

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(
    FragmentOnboardingBinding::inflate) {

    private val viewModel: OnboardingViewModel by viewModels()
    private lateinit var sharedPreferences : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(IS_ONBORDING_COMPLATED, AppCompatActivity.MODE_PRIVATE)
        checkIsOnboardingCompleted()
        setOnboardingAdapter()
        viewModel.checkIsOnboardingCompleted(sharedPreferences)
    }

    private fun checkIsOnboardingCompleted(){
        viewModel.isOnboarding.observe(viewLifecycleOwner){
            if (it) {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToChooseGameFragment())
                sharedPreferences.edit { putBoolean(IS_ONBORDING_COMPLATED, true) }
            }
        }
    }

    private fun skipOnboarding(adapter: OnboardingAdapter){
        adapter.clickListener={
            sharedPreferences.edit { putBoolean(IS_ONBORDING_COMPLATED, true) }
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToChooseGameFragment())
        }
    }

    private fun setOnboardingAdapter(){
        val adapter = OnboardingAdapter(requireContext(),viewModel.getOnboardingImagesList(), binding.viewPager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.setSelectedTabIndicator(null)
        skipOnboarding(adapter)
    }

}