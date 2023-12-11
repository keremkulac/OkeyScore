package com.keremkulac.okeyscore.ui.savePartnerGame

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSavePartnerGameBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SavePartnerGameFragment : Fragment(R.layout.fragment_save_partner_game)  {
    private val viewModel by viewModels<SavePartnerGameViewModel>()
    private lateinit var binding : FragmentSavePartnerGameBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavePartnerGameBinding.bind(view)
        setTotalScores()
        goToSaveFragment()
        setTeamInfo()
        saveToRoomDb()
    }
   private  fun setTotalScores(){
       viewModel.setTotal(team1EditTexts(),team2EditTexts(),binding.differenceText)
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
            binding.team1TotalScore
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
            binding.team2TotalScore
        )
    }
    private fun setVisibility(){
        binding.layout.visibility = View.GONE
        binding.scoreContainer.visibility = View.VISIBLE
        binding.arrowButton.setImageResource(R.drawable.ic_arrow_right)
        binding.differenceText.visibility = View.VISIBLE
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
                    viewModel.getTeamScoreInformations(team1EditTexts()),
                    viewModel.getTeamScoreInformations(team2EditTexts()),
                    team1EditTexts(),
                    team2EditTexts(),
                findNavController())
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
       viewModel.saveDataToSharedPreferences("continuingTeam1InformationList",continuingTeam1InformationList)
       viewModel.saveDataToSharedPreferences("continuingTeam2InformationList",continuingTeam2InformationList)
    }

    private fun setSharedPrefInformations(){
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam1InformationList",team1EditTexts())
        viewModel.getSharedPrefAndSetTeamScores("continuingTeam2InformationList",team2EditTexts())
        viewModel.isEmpty.observe(viewLifecycleOwner){
            if(it){
                setVisibility()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        saveToSharedPref()
    }

    override fun onStart() {
        super.onStart()
        setSharedPrefInformations()
    }

}