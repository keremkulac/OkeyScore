package com.keremkulac.okeyscore.ui.saveGame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
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
        goToSaveFragment()
    }

   private  fun setTotalScores(){
       viewModel.setTotal(team1EditTexts(),team2EditTexts(),
           binding.team1TotalScore,binding.team2TotalScore,
           binding.team1View,binding.team2View,requireContext())
    }


    private fun team1EditTexts(): List<EditText> {
        return listOf(
            binding.team1Name,
            binding.team1Score1,
            binding.team1Score2,
            binding.team1Score3,
            binding.team1Score4,
            binding.team1Score5,
            binding.team1Score6,
            binding.team1Score7,
            binding.team1Score8,
            binding.team1Score9,
            binding.team1Score10,
            binding.team1Score11,
        )
    }
    private fun team2EditTexts(): List<EditText> {
        return listOf(
            binding.team2Name,
            binding.team2Score1,
            binding.team2Score2,
            binding.team2Score3,
            binding.team2Score4,
            binding.team2Score5,
            binding.team2Score6,
            binding.team2Score7,
            binding.team2Score8,
            binding.team2Score9,
            binding.team2Score10,
            binding.team2Score11,
        )
    }


    private fun saveToRoomDb(){
        binding.saveGame.setOnClickListener {
            viewModel.insertFinishedGame(
                binding.team1Name.text.toString(),
                binding.team2Name.text.toString(),
                viewModel.getTeam1Scores(team1EditTexts()),
                viewModel.getTeam2Score(team2EditTexts()),
                binding.team1TotalScore.text.toString(),
                binding.team2TotalScore.text.toString(),requireContext(),sharedPreferences)
            viewModel.isTrue.observe(viewLifecycleOwner){
                if (it){
                   findNavController().navigate(R.id.action_saveGameFragment_to_finishedGameFragment)
                }
            }
        }
    }

    private fun goToSaveFragment(){
        binding.goToSaveFragment.setOnClickListener{
            findNavController().navigate(R.id.action_saveGameFragment_to_finishedGameFragment)

        }
    }

   private fun saveToSharedPref(){
        val continuingRedTeamList = viewModel.getTeam1Scores(team1EditTexts()).joinToString(separator = ",")
        val continuingBlueTeamList = viewModel.getTeam2Score(team2EditTexts()).joinToString(separator = ",")
        val editor = sharedPreferences.edit()
        editor.putString("continuingTeam1List", continuingRedTeamList)
        editor.putString("continuingTeam2List", continuingBlueTeamList)
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
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam1List",team1EditTexts(),sharedPreferences)
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam2List",team2EditTexts(),sharedPreferences)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam1List",team1EditTexts(),sharedPreferences)
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam2List",team2EditTexts(),sharedPreferences)
    }

}