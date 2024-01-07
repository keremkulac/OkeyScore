package com.keremkulac.okeyscore.ui.finishedGameView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
    private var fabVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishedGameViewBinding.inflate(inflater)
        saveGameFabClick()
        setViewPager()
        setViewPagerPage()
        goToSaveSingleGame()
        goToSavePartnerGame()
        return binding.root
    }

    private fun saveGameFabClick(){
        fabVisible = false
        binding.fab.setOnClickListener {
            if(!fabVisible){
                binding.fabSingle.show()
                binding.fabPartner.show()
                binding.fabPartnerText.visibility = View.VISIBLE
                binding.fabSingleText.visibility = View.VISIBLE
                binding.fabSingle.visibility = View.VISIBLE
                binding.fabPartner.visibility = View.VISIBLE
                binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close))
                fabVisible = true
            }else{
                binding.fabSingle.hide()
                binding.fabPartner.hide()
                binding.fabPartnerText.visibility = View.GONE
                binding.fabSingleText.visibility = View.GONE
                binding.fabSingle.visibility = View.GONE
                binding.fabPartner.visibility = View.GONE
                binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
                fabVisible = false
            }
        }
    }

    private fun goToSaveSingleGame(){
        binding.fabSingle.setOnClickListener {
            findNavController().navigate(R.id.action_finishedGameViewFragment_to_saveSingleGameFragment)
        }
    }
    private fun goToSavePartnerGame(){
        binding.fabPartner.setOnClickListener {
            findNavController().navigate(R.id.action_finishedGameViewFragment_to_savePartnerGameFragment)
        }
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
        val argument = arguments?.getString("gameType")
        if(argument != null){
            if(argument == "single"){
                binding.viewPager.setCurrentItem(0, true)

            }else{
                binding.viewPager.setCurrentItem(1, true)
            }
        }
    }
}