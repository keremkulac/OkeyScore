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
import com.keremkulac.okeyscore.util.VIEWPAGER_GAME_TYPE

class FinishedGameViewFragment :
    BaseFragment<FragmentFinishedGameViewBinding>(FragmentFinishedGameViewBinding::inflate) {
    private lateinit var adapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        setViewPagerPage()
    }

    private fun setViewPager() = with(binding) {
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        adapter.addFragment(FinishedSingleGameFragment())
        adapter.addFragment(FinishedPartnerGameFragment())
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tablayoutTitleArray)[position]
        }.attach()
    }


    private fun setViewPagerPage() = with(binding.viewPager) {
        val argument = requireArguments().getString(VIEWPAGER_GAME_TYPE)
        if (argument != null) {
            if (argument == GAME_TYPE_SINGLE) {
                setCurrentItem(0, true)
            } else {
                setCurrentItem(1, true)
            }
        }
    }
}