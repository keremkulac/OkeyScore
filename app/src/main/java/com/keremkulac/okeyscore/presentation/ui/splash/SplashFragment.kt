package com.keremkulac.okeyscore.presentation.ui.splash

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSplashBinding
import com.keremkulac.okeyscore.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimations()
        scheduleNavigation()
    }

    private fun startAnimations() {
        animateLogo()
        animateLoadingText()
    }

    private fun animateLogo() {
        binding.logoImage.apply {
            alpha = 0f
            scaleX = 0.3f
            scaleY = 0.3f
            rotation = -180f
            animate()
                .alpha(1f)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .rotation(0f)
                .setDuration(1200)
                .setInterpolator(OvershootInterpolator())
                .withEndAction {
                    animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .setInterpolator(BounceInterpolator())
                        .start()
                }
                .start()
        }
    }

    private fun animateLoadingText() {
        binding.loadingText.alpha = 0f
        lifecycleScope.launch {
            delay(1200)
            binding.loadingText.alpha = 1f
            startPulsingText()
            animateLoadingDots()
        }
    }

    private fun startPulsingText() {
        ObjectAnimator.ofFloat(binding.loadingText, "alpha", 1f, 0.3f, 1f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun animateLoadingDots() {
        viewLifecycleOwner.lifecycleScope.launch {
            var dotCount = 0
            while (true) {
                val dots = ".".repeat(dotCount % 4)
                binding.loadingText.text = getString(R.string.splash_logo_text).format(dots)
                dotCount++
                delay(500)
            }
        }
    }

    private fun scheduleNavigation() {
        lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToOnboardingFragment()
            )
        }
    }

}