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
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Info
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveSingleGameFragment : Fragment() {
    private val viewModel by viewModels<SaveSingleGameViewModel>()
    private lateinit var binding : FragmentSaveSingleGameBinding
    private var player1ScoreEditTextList = ArrayList<EditText>()
    private var player2ScoreEditTextList = ArrayList<EditText>()
    private var player3ScoreEditTextList = ArrayList<EditText>()
    private var player4ScoreEditTextList = ArrayList<EditText>()

    var lineCount = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentSaveSingleGameBinding.inflate(inflater)
        checkPlayerNames()
        saveGame()
        newRound(layoutInflater)
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
                binding.nameLayout.visibility = View.GONE
                binding.confirmNames.visibility = View.GONE
                binding.newRound.visibility = View.VISIBLE
                binding.saveGame.visibility = View.VISIBLE
                binding.scoreLayout.visibility = View.VISIBLE
            }
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
            }
        }
    }


    private fun createNewLine(inflater : LayoutInflater) : List<EditText>{
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.single_game_round_item, null)
        parentLayout.addView(includedLayout)
        val player1EditText = includedLayout.findViewById<EditText>(R.id.player1Score)
        val player2EditText = includedLayout.findViewById<EditText>(R.id.player2Score)
        val player3EditText = includedLayout.findViewById<EditText>(R.id.player3Score)
        val player4EditText = includedLayout.findViewById<EditText>(R.id.player4Score)
        player1ScoreEditTextList.add(player1EditText)
        player2ScoreEditTextList.add(player2EditText)
        player3ScoreEditTextList.add(player3EditText)
        player4ScoreEditTextList.add(player4EditText)

        val list = listOf<EditText>(
            player1EditText,
            player2EditText,
            player3EditText,
            player4EditText)
        setEditText(list)
        return list
    }

    private fun setEditText(editTextList: List<EditText>){
        for(item in editTextList){
            item.hint = "Tur $lineCount"
            item.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }
   private fun saveGame(){
        binding.saveGame.setOnClickListener {
            Log.d("TAG","Kayıt başarılı")
            val scoreLists = listOf<List<EditText>>(player1ScoreEditTextList,player2ScoreEditTextList,player3ScoreEditTextList,player4ScoreEditTextList)
            val players = viewModel.createPlayers(playerNames(),scoreLists)
            val info = Info("info", "date")
            val finishedSingleGame = FinishedSingleGame(0,players[0],players[1],players[2],players[3],info)
            viewModel.insertGame(finishedSingleGame)

        }

    }
}