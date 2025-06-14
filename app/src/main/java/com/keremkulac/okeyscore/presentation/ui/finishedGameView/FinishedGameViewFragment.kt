package com.keremkulac.okeyscore.presentation.ui.finishedGameView

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameViewBinding
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame.FinishedPartnerGameFragment
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGame.FinishedSingleGameFragment
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.GAME_TYPE_SINGLE

class FinishedGameViewFragment :
    BaseFragment<FragmentFinishedGameViewBinding>(FragmentFinishedGameViewBinding::inflate) {
    private lateinit var adapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        setViewPagerPage()
    }

    private fun setViewPager() {
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        adapter.addFragment(FinishedSingleGameFragment())
        adapter.addFragment(FinishedPartnerGameFragment())
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tablayoutTitleArray)[position]
        }.attach()
    }


    private fun setViewPagerPage() {
        val argument = requireArguments().getString("gameType")
        if (argument != null) {
            if (argument == GAME_TYPE_SINGLE) {
                binding.viewPager.setCurrentItem(0, true)

            } else {
                binding.viewPager.setCurrentItem(1, true)
            }
        }
    }
}