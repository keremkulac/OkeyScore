package com.keremkulac.okeyscore.ui.saveSingleGame

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSaveSingleGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveSingleGameFragment : Fragment() {
    private val viewModel by viewModels<SaveSingleGameViewModel>()
    private lateinit var binding : FragmentSaveSingleGameBinding
    private var player1ScoreEditTextList = ArrayList<EditText>()
    private var player2ScoreEditTextList = ArrayList<EditText>()
    private var player3ScoreEditTextList = ArrayList<EditText>()
    private var player4ScoreEditTextList = ArrayList<EditText>()
    private var totalScoreEditTextList = ArrayList<EditText>()
    private var allPlayerScoreEditTextList = ArrayList<ArrayList<EditText>>()

    var lineCount = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentSaveSingleGameBinding.inflate(inflater)
        checkPlayerNames()
        saveGame()
        newRound(layoutInflater)
        fillTotalScoreEditTextList()
        return binding.root
    }

    private fun playerNames(): List<EditText> {
        return listOf(
            binding.player1Name,
            binding.player2Name,
            binding.player3Name,
            binding.player4Name
        )
    }
    private fun checkPlayerNames(){
        binding.confirmNames.setOnClickListener {
            val isEmpty = viewModel.checkList(playerNames())
            if(isEmpty){
                Log.d("TAG","Lütfen isimleri giriniz")
            }else{
                binding.nameEntryLayout.visibility = View.GONE
                binding.confirmNames.visibility = View.GONE
                binding.newRound.visibility = View.VISIBLE
                binding.saveGame.visibility = View.VISIBLE
                binding.scoreLayout.visibility = View.VISIBLE
                binding.namesLayout.visibility = View.VISIBLE
                binding.totalScoresLayout.visibility = View.VISIBLE
                setTotalScoresEditTextView()
                calculate()
                setPlayerNames()
            }
        }

    }

    private fun setPlayerNames(){
        binding.names.player1Score.text = playerNames()[0].text
        binding.names.player2Score.text = playerNames()[1].text
        binding.names.player3Score.text = playerNames()[2].text
        binding.names.player4Score.text = playerNames()[3].text
    }

    private fun fillTotalScoreEditTextList(){
        totalScoreEditTextList.add(binding.totalScores.player1Score)
        totalScoreEditTextList.add(binding.totalScores.player2Score)
        totalScoreEditTextList.add(binding.totalScores.player3Score)
        totalScoreEditTextList.add(binding.totalScores.player4Score)
    }
    private fun setTotalScoresEditTextView(){
        for (totalScoreEditText in totalScoreEditTextList){
            totalScoreEditText.hint = "Toplam"
            totalScoreEditText.isEnabled = false
        }
    }
    private fun newRound(inflater : LayoutInflater){
        var newLine = createNewLine(inflater)
        binding.newRound.setOnClickListener {
            val isEmpty = viewModel.checkList(newLine)
            if (isEmpty){
              Log.d("TAG","BOŞ")
            }else{
                Log.d("TAG","boş değil")
                lineCount++
                newLine = createNewLine(inflater)
                calculate()
            }
        }
    }

    val editTextIds = listOf(R.id.player1Score, R.id.player2Score, R.id.player3Score, R.id.player4Score)

    private fun createNewLine(inflater : LayoutInflater) : List<EditText>{
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.single_game_round_item, null)
        parentLayout.addView(includedLayout)
        createAllPlayerScoreEditTextList()
        val editTextList = mutableListOf<EditText>()
        editTextIds.forEachIndexed{index, id ->
            val editText = includedLayout.findViewById<EditText>(id)
            allPlayerScoreEditTextList[index].add(editText)
            editTextList.add(editText)
        }

        setEditText(editTextList)
        return editTextList
    }

    private fun createAllPlayerScoreEditTextList(){
        allPlayerScoreEditTextList.add(player1ScoreEditTextList)
        allPlayerScoreEditTextList.add(player2ScoreEditTextList)
        allPlayerScoreEditTextList.add(player3ScoreEditTextList)
        allPlayerScoreEditTextList.add(player4ScoreEditTextList)
    }

    private fun calculate(){
       viewModel.setTotal(player1ScoreEditTextList,totalScoreEditTextList[0])
       viewModel.setTotal(player2ScoreEditTextList,totalScoreEditTextList[1])
       viewModel.setTotal(player3ScoreEditTextList,totalScoreEditTextList[2])
       viewModel.setTotal(player4ScoreEditTextList,totalScoreEditTextList[3])
   }
    private fun setEditText(editTextList: List<EditText>){
        for(editText in editTextList){
            editText.hint = "Tur $lineCount"
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }
   private fun saveGame(){
        binding.saveGame.setOnClickListener {
            viewModel.insertSingleGame(player1ScoreEditTextList,player2ScoreEditTextList,player3ScoreEditTextList,player4ScoreEditTextList,playerNames())
        }
    }
}