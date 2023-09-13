package com.keremkulac.okeyscore.ui.saveGame

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
        setTeamInfo()
    }
   private  fun setTotalScores(){
       viewModel.setTotal(team1EditTexts(),team2EditTexts(),
           binding.team1TotalScore,binding.team2TotalScore, binding.differenceText)
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
    private fun setTeamInfo(){
        binding.arrowButton.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(binding.layout, AutoTransition())
                }
                binding.scoreContainer.visibility = View.GONE
                binding.layout.visibility = View.VISIBLE
                binding.arrowButton.setImageResource(R.drawable.ic_arrow_down)
                binding.differenceText.visibility = View.GONE
        }

        binding.saveTeamNames.setOnClickListener {
            if(binding.team1Name.text.toString() == "" || binding.team2Name.text.toString() == ""){
                Toast.makeText(requireContext(),"Lütfen takım isimlerini giriniz",Toast.LENGTH_SHORT).show()
            }else{
                if(binding.layout.visibility == View.VISIBLE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(binding.layout, AutoTransition())
                    }
                    binding.layout.visibility = View.GONE
                    binding.scoreContainer.visibility = View.VISIBLE
                    binding.arrowButton.setImageResource(R.drawable.ic_arrow_right)
                    binding.differenceText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun saveToRoomDb(){
        binding.saveGame.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext(),R.style.AlertDialogStyle)
            alertDialogBuilder.setTitle("İşlem Onayı")
            alertDialogBuilder.setMessage("Oyunu kayıt etmek istiyor musunuz?")
            alertDialogBuilder.setPositiveButton("Evet") { dialog, which ->
                viewModel.insertFinishedGame(
                    binding.team1Name.text.toString(),
                    binding.team2Name.text.toString(),
                    viewModel.getTeamScoreInformations(team1EditTexts()),
                    viewModel.getTeamScoreInformations(team2EditTexts()),
                    team1EditTexts(),
                    team2EditTexts(),
                    viewModel.getTeamScoreDifference(team1EditTexts(),team2EditTexts(),binding.team1Name.text.toString(),binding.team2Name.text.toString()),sharedPreferences)
                findNavController().navigate(R.id.action_saveGameFragment_to_finishedGameFragment)
            }
            alertDialogBuilder.setNegativeButton("Hayır") { dialog, which -> }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun goToSaveFragment(){
        binding.goToSaveFragment.setOnClickListener{
            findNavController().navigate(R.id.action_saveGameFragment_to_finishedGameFragment)
        }
    }

   private fun saveToSharedPref(){
        val continuingTeam1InformationList = viewModel.getTeamScoreInformations(team1EditTexts()).joinToString(separator = ",")
        val continuingTeam2InformationList = viewModel.getTeamScoreInformations(team2EditTexts()).joinToString(separator = ",")
        val editor = sharedPreferences.edit()
       editor.putString("continuingTeam1InformationList", continuingTeam1InformationList)
       editor.putString("team1Name", binding.team1Name.text.toString())
       editor.putString("continuingTeam2InformationList", continuingTeam2InformationList)
       editor.putString("team2Name", binding.team2Name.text.toString())
       editor.apply()
    }

    private fun setSharedPrefInformations(){
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam1InformationList","team1Name",team1EditTexts(),sharedPreferences
            ,binding.layout,binding.scoreContainer,binding.arrowButton,binding.differenceText)
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam2InformationList","team2Name",team2EditTexts(),sharedPreferences,
            binding.layout,binding.scoreContainer,binding.arrowButton,binding.differenceText)
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
        setSharedPrefInformations()
    }

    override fun onResume() {
        super.onResume()
        setSharedPrefInformations()
    }

}