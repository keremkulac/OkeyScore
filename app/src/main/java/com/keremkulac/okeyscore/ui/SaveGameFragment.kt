package com.keremkulac.okeyscore.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.keremkulac.okeyscore.databinding.FragmentSaveGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveGameFragment : Fragment(){
    private val viewModel by viewModels<SaveGameViewModel>()
    private lateinit var binding : FragmentSaveGameBinding
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSaveGameBinding.inflate(inflater)
        saveToRoomDb()
        getFromRoomDb()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("ContinuingSharedPref",
            Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTotalScores()
    }

   private  fun setTotalScores(){
        viewModel.setTotal(redTeamEditTexts(),binding.redTeamTotalScore)
        viewModel.setTotal(blueTeamEditTexts(),binding.blueTeamTotalScore)
    }

    private fun getFromRoomDb(){
        viewModel.finishedGame.observe(requireActivity()){
            it?.let {
               //     Log.d("TAG",it.continuingRedTeam.toString())
            }
        }
    }



    private fun redTeamEditTexts(): List<EditText> {
        return listOf(
            binding.redTeamName,
            binding.redTeamScore1,
            binding.redTeamScore2,
            binding.redTeamScore3,
            binding.redTeamScore4,
            binding.redTeamScore5,
            binding.redTeamScore6,
            binding.redTeamScore7,
            binding.redTeamScore8,
            binding.redTeamScore9,
            binding.redTeamScore10,
            binding.redTeamScore11,
        )
    }
    private fun blueTeamEditTexts(): List<EditText> {
        return listOf(
            binding.blueTeamName,
            binding.blueTeamScore1,
            binding.blueTeamScore2,
            binding.blueTeamScore3,
            binding.blueTeamScore4,
            binding.blueTeamScore5,
            binding.blueTeamScore6,
            binding.blueTeamScore7,
            binding.blueTeamScore8,
            binding.blueTeamScore9,
            binding.blueTeamScore10,
            binding.blueTeamScore11,
        )
    }

    private fun saveToRoomDb(){
        binding.save.setOnClickListener {
            viewModel.insertFinishedGame(
                binding.redTeamName.text.toString(),
                binding.blueTeamName.text.toString(),
                viewModel.getRedTeamScores(redTeamEditTexts()),
                viewModel.getBlueTeamScores(blueTeamEditTexts()),
                binding.redTeamTotalScore.text.toString().toInt(),
                binding.blueTeamTotalScore.text.toString().toInt(),requireContext())
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        //    findNavController().navigate(R.id.saveGameFragment)23
        }
    }

   private fun saveToSharedPref(){
        val continuingRedTeamList = viewModel.getRedTeamScores(redTeamEditTexts()).joinToString(separator = ",")
        val continuingBlueTeamList = viewModel.getBlueTeamScores(blueTeamEditTexts()).joinToString(separator = ",")
        val editor = sharedPreferences.edit()
        editor.putString("continuingRedTeamList", continuingRedTeamList)
        editor.putString("continuingBlueTeamList", continuingBlueTeamList)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToSharedPref()
    }

    override fun onPause() {
        super.onPause()
        saveToSharedPref()

    }

    override fun onStop() {
        super.onStop()
        saveToSharedPref()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSharedPrefAndSetTeamScores("continuingRedTeamList",redTeamEditTexts(),sharedPreferences)
        viewModel.getSharedPrefAndSetTeamScores("continuingBlueTeamList",blueTeamEditTexts(),sharedPreferences)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSharedPrefAndSetTeamScores("continuingRedTeamList",redTeamEditTexts(),sharedPreferences)
        viewModel.getSharedPrefAndSetTeamScores("continuingBlueTeamList",blueTeamEditTexts(),sharedPreferences)
    }

}