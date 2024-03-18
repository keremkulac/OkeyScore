package com.keremkulac.okeyscore.ui.savePartnerGame

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSavePartnerGameBinding
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SavePartnerGameFragment : Fragment(R.layout.fragment_save_partner_game)  {
    private val viewModel by viewModels<SavePartnerGameViewModel>()
    private lateinit var binding : FragmentSavePartnerGameBinding
    private var team1ScoreEditTextList = ArrayList<EditText>()
    private var team2ScoreEditTextList = ArrayList<EditText>()
    private var allTeamScoreEditTextList = ArrayList<ArrayList<EditText>>()
    private var lineCount = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavePartnerGameBinding.bind(view)
        checkTeamNames()
        saveToRoomDb()
        newRound(layoutInflater)
        goToChooseGameFragment()
    }

    private fun playerNames(): List<EditText> {
        return listOf(
            binding.team1NameEntry,
            binding.team2NameEntry
        )
    }

    private fun checkTeamNames(){
        binding.confirmNames.setOnClickListener {
            if(viewModel.checkList(playerNames())){
                requireContext().toast(requireContext().getString(R.string.warning_enter_all_team_names), R.drawable.ic_warning)
            }else{
                binding.team1NameEntryHint.visibility = View.GONE
                binding.team2NameEntryHint.visibility = View.GONE
                binding.confirmNames.visibility = View.GONE
                binding.newRound.visibility = View.VISIBLE
                binding.saveGame.visibility = View.VISIBLE
                binding.team1Name.visibility = View.VISIBLE
                binding.team2Name.visibility = View.VISIBLE
                binding.scoreLayout.visibility = View.VISIBLE
                binding.roundScoreTitle.visibility = View.VISIBLE
                binding.playerNameTitle.visibility = View.VISIBLE
                binding.scoreColumnDivider.visibility = View.VISIBLE
                binding.saveGame.isEnabled = false
                calculate()
                setPlayerNames()
            }
        }
    }

    private fun setPlayerNames(){
        for (i in playerNames().indices){
            val id= resources.getIdentifier("team${i+1}Name","id",requireContext().packageName)
            requireActivity().findViewById<TextInputLayout>(id).hint = playerNames()[i].text.toString()
        }
    }

    private fun checkRoundScores(newLine : List<EditText>){
        for(i in newLine.indices){
            editTextWatcher(newLine[i])
        }
    }

    private fun newRound(inflater : LayoutInflater){
        var newLine = createNewLine(inflater)
        checkRoundScores(newLine)
        binding.newRound.setOnClickListener {
            if (viewModel.checkList(newLine)){
                requireContext().toast(resources.getString(R.string.warning_check_all_rounds).format(lineCount), R.drawable.ic_warning)
            }else{
                lineCount++
                newLine = createNewLine(inflater)
                checkRoundScores(newLine)
                calculate()
                binding.saveGame.isEnabled = false
            }
        }
    }

    private fun createNewLine(inflater : LayoutInflater) : List<EditText>{
        val nullParent : ViewGroup? = null
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.partner_game_round_item, nullParent)
        parentLayout.addView(includedLayout)
        if(lineCount % 2 != 0){
            includedLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.line_color_dark))
        }
        createAllTeamScoreEditTextList()
        val editTextIds = listOf(R.id.team1Score, R.id.team2Score)
        val hintIds = listOf(R.id.team1ScoreHint, R.id.team2ScoreHint)
        val editTextList = mutableListOf<EditText>()
        editTextIds.forEachIndexed{index, id ->
            val editText = includedLayout.findViewById<EditText>(id)
            allTeamScoreEditTextList[index].add(editText)
            editTextList.add(editText)
        }
        setEditText(hintIds,includedLayout)
        return editTextList
    }

    private fun createAllTeamScoreEditTextList(){
        allTeamScoreEditTextList.add(team1ScoreEditTextList)
        allTeamScoreEditTextList.add(team2ScoreEditTextList)
    }

    private fun setEditText(hintIds: List<Int>,parentLayout : View){
        for(id in hintIds){
            val textInputLayout = parentLayout.findViewById<TextInputLayout>(id)
            textInputLayout.hint =requireContext().getString(R.string.round_count).format(lineCount)

        }
    }
    private fun saveToRoomDb(){
        binding.saveGame.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext(),R.style.AlertDialogStyle)
            alertDialogBuilder.setTitle(requireContext().getString(R.string.confirmation_title))
            alertDialogBuilder.setMessage(requireContext().getString(R.string.confirmation_message))
            alertDialogBuilder.setPositiveButton(requireContext().getString(R.string.confirmation_yes)) { _, _ ->
                viewModel.insertFinishedGame(allTeamScoreEditTextList,playerNames(),findNavController(),requireContext())
            }
            alertDialogBuilder.setNegativeButton(requireContext().getString(R.string.confirmation_no)) { _, _ -> }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun getAllTeamTotalScoreEditTextList(): List<EditText> {
        return listOf(
            binding.team1TotalScore,
            binding.team2TotalScore
        )
    }


    private fun calculate(){

        var size = allTeamScoreEditTextList.size
        if(size > 2){
            size = 2
            for (i in 0 until size){
                viewModel.setTotalScore(allTeamScoreEditTextList[i],getAllTeamTotalScoreEditTextList()[i])
            }
        }else{
            for (i in 0 until size){
                viewModel.setTotalScore(allTeamScoreEditTextList[i],getAllTeamTotalScoreEditTextList()[i])
            }
        }
    }

    private fun editTextWatcher(editText: EditText) : Boolean{
        var empty = false
        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                empty = viewModel.areAllEditTextsFilled(allTeamScoreEditTextList,binding.saveGame)

            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        editText.setOnFocusChangeListener { _,hasFocus ->
            if(hasFocus){
                bottomNavigation.visibility = View.GONE
            }
        }
        return empty
    }

    private fun goToChooseGameFragment(){
        binding.goToChooseGameFragment.setOnClickListener {
            findNavController().navigate(SavePartnerGameFragmentDirections.actionSavePartnerGameFragmentToChooseGameFragment())
        }
    }

}