package com.keremkulac.okeyscore.ui.finishedGameView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameViewBinding
import com.keremkulac.okeyscore.ui.finishedPartnerGame.FinishedPartnerGameAdapter
import com.keremkulac.okeyscore.ui.finishedPartnerGame.FinishedPartnerGameFragment
import com.keremkulac.okeyscore.ui.finishedSingleGame.FinishedSingleGameAdapter
import com.keremkulac.okeyscore.ui.finishedSingleGame.FinishedSingleGameFragment


class FinishedGameViewFragment : Fragment() {
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var binding : FragmentFinishedGameViewBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishedGameViewBinding.inflate(inflater)
        setViewPager()
        setViewPagerPage()
        return binding.root
    }

    private fun setViewPager(){
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager,lifecycle)
        adapter.addFragment(FinishedSingleGameFragment(FinishedSingleGameAdapter()))
        adapter.addFragment(FinishedPartnerGameFragment(FinishedPartnerGameAdapter()))
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text = resources.getStringArray(R.array.tablayoutTitleArray)[position]
        }.attach()
    }


    private fun setViewPagerPage(){
        val argument = requireArguments().getString("gameType")
        if(argument != null){
            if(argument == "single"){
                binding.viewPager.setCurrentItem(0, true)

            }else{
                binding.viewPager.setCurrentItem(1, true)
            }
        }
    }
}