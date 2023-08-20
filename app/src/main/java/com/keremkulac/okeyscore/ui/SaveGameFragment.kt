package com.keremkulac.okeyscore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keremkulac.okeyscore.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveGameFragment : Fragment(){
    private val viewModel by viewModels<SaveGameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_save_game, container, false)
    }

    private fun save(){


      //  viewModel.save(continuing,finished)


    }

}