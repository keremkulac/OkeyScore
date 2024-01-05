package com.keremkulac.okeyscore.ui.saveSingleGame

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSaveSingleGameBinding
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveSingleGameFragment : Fragment(R.layout.fragment_save_single_game) {
    private val viewModel by viewModels<SaveSingleGameViewModel>()
    private lateinit var binding : FragmentSaveSingleGameBinding
    private var player1ScoreEditTextList = ArrayList<EditText>()
    private var player2ScoreEditTextList = ArrayList<EditText>()
    private var player3ScoreEditTextList = ArrayList<EditText>()
    private var player4ScoreEditTextList = ArrayList<EditText>()
    private var allPlayerScoreEditTextList = ArrayList<ArrayList<EditText>>()
    var lineCount = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveSingleGameBinding.bind(view)
        checkPlayerNames()
        saveGame()
        newRound(layoutInflater)
    }

    private fun playerNames(): List<EditText> {
        return listOf(
            binding.player1NameEntry,
            binding.player2NameEntry,
            binding.player3NameEntry,
            binding.player4NameEntry
        )
    }

    private fun getAllPlayerTotalScoreEditTextList(): List<EditText> {
        return listOf(
            binding.player1TotalScore,
            binding.player2TotalScore,
            binding.player3TotalScore,
            binding.player4TotalScore
        )
    }

    private fun checkPlayerNames(){
        binding.confirmNames.setOnClickListener {
            if(viewModel.checkList(playerNames())){
                requireContext().toast("Lütfen tüm oyuncu isimlerini giriniz")
            }else{
                binding.nameEntryLayout.visibility = View.GONE
                binding.confirmNames.visibility = View.GONE
                binding.newRound.visibility = View.VISIBLE
                binding.saveGame.visibility = View.VISIBLE
                binding.scoreLayout.visibility = View.VISIBLE
                binding.playerLayout.visibility = View.VISIBLE
                binding.seperator.visibility = View.VISIBLE
                binding.roundScoreTitle.visibility = View.VISIBLE
                binding.playerNameTitle.visibility = View.VISIBLE
                binding.saveGame.isEnabled = false
                calculate()
                setPlayerNames()
            }
        }
    }

    private fun newRound(inflater : LayoutInflater){
        var newLine = createNewLine(inflater)
        checkRoundScores(newLine)
        binding.newRound.setOnClickListener {
            if (viewModel.checkList(newLine)){
                requireContext().toast("Lütfen $lineCount. turdaki tüm alanları doldurun.")
            }else{
                lineCount++
                newLine = createNewLine(inflater)
                checkRoundScores(newLine)
                calculate()
                binding.saveGame.isEnabled = false
            }
        }
    }

    private fun checkRoundScores(newLine : List<EditText>){
        for(i in newLine.indices){
            editTextWatcher(newLine[i])
        }
    }

    private fun createNewLine(inflater : LayoutInflater) : List<EditText>{
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.single_game_round_item, null)
        parentLayout.addView(includedLayout)
        createAllPlayerScoreEditTextList()
        val editTextIds = listOf(R.id.player1Score, R.id.player2Score, R.id.player3Score, R.id.player4Score)
        val hintIds = listOf(R.id.player1ScoreHint, R.id.player2ScoreHint, R.id.player3ScoreHint, R.id.player4ScoreHint)
        val editTextList = mutableListOf<EditText>()
        editTextIds.forEachIndexed{index, id ->
            val editText = includedLayout.findViewById<EditText>(id)
            allPlayerScoreEditTextList[index].add(editText)
            editTextList.add(editText)
        }
        setEditText(hintIds,includedLayout)
        return editTextList
    }

    private fun setPlayerNames(){
        for (i in playerNames().indices){
            val id= resources.getIdentifier("player${i+1}Name","id",requireContext().packageName)
            requireActivity().findViewById<TextInputLayout>(id).hint = playerNames()[i].text.toString()
        }
    }

    private fun createAllPlayerScoreEditTextList(){
        allPlayerScoreEditTextList.add(player1ScoreEditTextList)
        allPlayerScoreEditTextList.add(player2ScoreEditTextList)
        allPlayerScoreEditTextList.add(player3ScoreEditTextList)
        allPlayerScoreEditTextList.add(player4ScoreEditTextList)
    }

    private fun editTextWatcher(editText: EditText) : Boolean{
        var empty = false
        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                empty = viewModel.areAllEditTextsFilled(allPlayerScoreEditTextList,binding.saveGame)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        return empty
    }

    private fun calculate(){
        var size = allPlayerScoreEditTextList.size
        if(size > 4){
            size = 4
            for (i in 0 until size){
                viewModel.setTotalScore(allPlayerScoreEditTextList[i],getAllPlayerTotalScoreEditTextList()[i])
            }
        }else{
            for (i in 0 until size){
                viewModel.setTotalScore(allPlayerScoreEditTextList[i],getAllPlayerTotalScoreEditTextList()[i])
            }
        }
   }
    private fun setEditText(hintIds: List<Int>,parentLayout : View){
        for(id in hintIds){
            val textInputLayout = parentLayout.findViewById<TextInputLayout>(id)
            textInputLayout.hint ="Tur $lineCount"
        }
    }

   private fun saveGame(){
        binding.saveGame.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext(),R.style.AlertDialogStyle)
            alertDialogBuilder.setTitle("İşlem Onayı")
            alertDialogBuilder.setMessage("Oyunu kayıt etmek istiyor musunuz?")
            alertDialogBuilder.setPositiveButton("Evet") { dialog, which ->
                viewModel.insertSingleGame(allPlayerScoreEditTextList,playerNames(),findNavController(),requireContext())
            }
            alertDialogBuilder.setNegativeButton("Hayır",null)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}