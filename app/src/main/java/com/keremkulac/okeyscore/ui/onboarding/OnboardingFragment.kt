package com.keremkulac.okeyscore.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding : FragmentOnboardingBinding
    private val viewModel by viewModels<OnboardingViewModel>()
    private lateinit var sharedPreferences : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        sharedPreferences = requireActivity().getSharedPreferences("isOnboardingCompleted", AppCompatActivity.MODE_PRIVATE)
        checkIsOnboardingCompleted()
        setOnboardingAdapter()
        viewModel.checkIsOnboardingCompleted(sharedPreferences)
    }

    private fun checkIsOnboardingCompleted(){
        viewModel.isOnboarding.observe(viewLifecycleOwner){
            if (it) {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToChooseGameFragment())
                sharedPreferences.edit().putBoolean("isOnboardingCompleted",true ).apply()
            }
        }
    }

    private fun skipOnboarding(adapter: OnboardingAdapter){
        adapter.clickListener={
            sharedPreferences.edit().putBoolean("isOnboardingCompleted",true ).apply()
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