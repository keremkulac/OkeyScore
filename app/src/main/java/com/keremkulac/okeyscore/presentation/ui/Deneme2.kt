package com.keremkulac.okeyscore.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentDeneme2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Deneme2 : Fragment(R.layout.fragment_deneme2) {
    private lateinit var binding : FragmentDeneme2Binding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDeneme2Binding.bind(view)
    }
}