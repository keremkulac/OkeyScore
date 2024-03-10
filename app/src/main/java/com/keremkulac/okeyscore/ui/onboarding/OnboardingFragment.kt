package com.keremkulac.okeyscore.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding : FragmentOnboardingBinding
    private lateinit var sharedPreferences : SharedPreferences
    private var isOnboardingCompleted : Boolean?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        sharedPreferences = requireActivity().getSharedPreferences("isOnboardingCompleted", AppCompatActivity.MODE_PRIVATE)
        checkIsOnboardingCompleted()
        setOnboardingAdapter()
    }

    private fun checkIsOnboardingCompleted(){
        isOnboardingCompleted = sharedPreferences.getBoolean("isOnboardingCompleted", false)
        isOnboardingCompleted?.let {
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
            val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
            onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                this.isEnabled = false
            }
        }
    }

    private fun setOnboardingAdapter(){
        val images = listOf(
            R.drawable.onboarding_1,
            R.drawable.onboarding_2,
            R.drawable.onboarding_3
        )
        val adapter = OnboardingAdapter(requireContext(),images, binding.viewPager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.setSelectedTabIndicator(null)
        skipOnboarding(adapter)
    }

}