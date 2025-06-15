package com.keremkulac.okeyscore.presentation.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.IS_ONBORDING_COMPLATED

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(
    FragmentOnboardingBinding::inflate
) {

    private val viewModel: OnboardingViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            IS_ONBORDING_COMPLATED,
            AppCompatActivity.MODE_PRIVATE
        )
        checkIsOnboardingCompleted()
        setupOnboarding()
        onBackPressCancel()
    }

    private fun checkIsOnboardingCompleted() {
        viewModel.checkIsOnboardingCompleted(sharedPreferences)
        viewModel.isOnboarding.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToChooseGameFragment())
                sharedPreferences.edit { putBoolean(IS_ONBORDING_COMPLATED, true) }
            }
        }
    }

    private fun setupOnboarding() {
        val onboardingItems = getOnboardingItems()
        val adapter = OnboardingAdapter(onboardingItems)
        with(binding) {
            viewPager.adapter = adapter
            dotsIndicator.setViewPager2(viewPager)
            updateButtonVisibility(viewPager.currentItem)
            nextButton.setOnClickListener {
                if (viewPager.currentItem < onboardingItems.lastIndex) {
                    viewPager.currentItem++
                } else {
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToChooseGameFragment()
                    )
                    sharedPreferences.edit { putBoolean(IS_ONBORDING_COMPLATED, true) }
                }
                updateButtonVisibility(viewPager.currentItem)
            }

            previousButton.setOnClickListener {
                if (viewPager.currentItem > 0) {
                    viewPager.currentItem--
                }
                updateButtonVisibility(viewPager.currentItem)
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateButtonVisibility(position)
                }
            })
        }

    }

    private fun updateButtonVisibility(position: Int) {
        with(binding) {
            previousButton.visibility = if (position == 0) View.GONE else View.VISIBLE
            if (position == viewPager.adapter?.itemCount?.minus(1)) {
                nextButton.text = getString(R.string.onboarding_next_finish_button_text)
            } else {
                nextButton.text = getString(R.string.onboarding_next_button_text)
            }
        }
    }

    private fun getOnboardingItems(): List<OnboardingItem> {
        return listOf(
            OnboardingItem(
                R.drawable.illustrations_onboarding_1,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_description_1)
            ),
            OnboardingItem(
                R.drawable.illustrations_onboarding_2,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_description_2)
            ),
            OnboardingItem(
                R.drawable.illustrations_onboarding_3,
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_description_3)
            ),
            OnboardingItem(
                R.drawable.illustrations_onboarding_4,
                getString(R.string.onboarding_title_4),
                getString(R.string.onboarding_description_4)
            )
        )
    }

    private fun onBackPressCancel() {
        val onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
    }

}