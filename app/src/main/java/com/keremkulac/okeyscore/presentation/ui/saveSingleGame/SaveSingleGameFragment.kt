package com.keremkulac.okeyscore.presentation.ui.saveSingleGame

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.divider.MaterialDivider
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSaveSingleGameBinding
import com.keremkulac.okeyscore.util.CustomDialog
import com.keremkulac.okeyscore.util.SINGLE_PLAYER_SIZE
import com.keremkulac.okeyscore.util.createAlertDialog
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class SaveSingleGameFragment : Fragment(R.layout.fragment_save_single_game) {
    private val viewModel by viewModels<SaveSingleGameViewModel>()
    private lateinit var binding : FragmentSaveSingleGameBinding
    private val allPlayerScoreEditTextList: List<MutableList<EditText>> = List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val allPlayerPenaltyTextViewList: List<MutableList<TextView>> = List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val dividerList : ArrayList<MaterialDivider> = ArrayList()
    private val penaltyHashMap = HashMap<String,List<TextView>>()
    private val totalScoreHasMap = HashMap<String,TextView>()

    private var lineCount = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveSingleGameBinding.bind(view)
        handleOnBackPressed()
        checkPlayerNames()
        saveGame()
        newRound(layoutInflater)
        goToChooseGameFragment()
        penalty()
    }

    private fun playerNames(): List<EditText> {
        return listOf(
            binding.player1NameEntry,
            binding.player2NameEntry,
            binding.player3NameEntry,
            binding.player4NameEntry
        )
    }

    private fun getAllPlayerTotalScoreEditTextList(): List<TextView> {
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
                requireContext().toast(requireContext().getString(R.string.warning_enter_all_player_names), R.drawable.ic_warning)
            }else{

                if(viewModel.sameNamesCheck(playerNames())){
                    requireContext().toast(requireContext().getString(R.string.enter_different_names), R.drawable.ic_warning)
                }else{
                    binding.cardView.visibility = View.GONE
                    binding.newRound.visibility = View.VISIBLE
                    binding.saveGame.visibility = View.VISIBLE
                    binding.playerNamesAndScoresLayout.visibility = View.VISIBLE
                    binding.scoreLayout.visibility = View.VISIBLE
                    binding.roundScoreTitle.visibility = View.VISIBLE
                    binding.playerNameTitle.visibility = View.VISIBLE
                    binding.penalty.visibility = View.VISIBLE
                    binding.saveGame.isEnabled = false
                    calculate()
                    setPlayerNames()
                    createTotalScoreHashMap()
                    createPenaltyHashMap(allPlayerPenaltyTextViewList as List<List<TextView>>)
                }
            }
        }
    }

    private fun newRound(inflater : LayoutInflater){
        var newLine = createNewLine(inflater)
        createAllPlayerScoreEditTextList(newLine)
        checkRoundScores(newLine)
        checkDividerList()
        binding.newRound.setOnClickListener {
            if (viewModel.checkList(newLine)){
                requireContext().toast(requireContext().getString(R.string.warning_check_all_rounds).format(lineCount), R.drawable.ic_warning)
            }else{
                lineCount++
                newLine = createNewLine(inflater)
                createAllPlayerScoreEditTextList(newLine)
                checkRoundScores(newLine)
                calculate()
                checkDividerList()
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
        val nullParent : ViewGroup? = null
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.single_game_round_item, nullParent)
        parentLayout.addView(includedLayout)
        val penaltyTextViewIds = listOf(R.id.player1Penalty,R.id.player2Penalty,R.id.player3Penalty,R.id.player4Penalty)
        val editTextIds = listOf(R.id.player1Score, R.id.player2Score, R.id.player3Score, R.id.player4Score)
        val count = includedLayout.findViewById<TextView>(R.id.roundCount)
        count.text = lineCount.toString()
        val editTextList = mutableListOf<EditText>()
        val textViewList = mutableListOf<TextView>()
        for (id in editTextIds){
            val editText = includedLayout.findViewById<EditText>(id)
            editTextList.add(editText)
        }
        for(id in penaltyTextViewIds){
            val textView = includedLayout.findViewById<TextView>(id)
            textViewList.add(textView)
        }
        dividerList.add(includedLayout.findViewById(R.id.horizontalDivider))
        createAllPlayersPenaltyTextViewList(textViewList)
        return editTextList
    }

    private fun createPenaltyHashMap(allPlayerPenaltyTextViewList : List<List<TextView>>){
        for((i, list) in allPlayerPenaltyTextViewList.withIndex()){
            penaltyHashMap[playerNames()[i].text.toString()] = list
        }
    }

    private fun createTotalScoreHashMap(){
        for((i, listItem) in getAllPlayerTotalScoreEditTextList().withIndex()){
                totalScoreHasMap[playerNames()[i].text.toString()] = listItem
            }
    }

    private fun setPlayerNames(){
        for (i in playerNames().indices){
            val id= resources.getIdentifier("player${i+1}Name","id",requireContext().packageName)
            requireActivity().findViewById<TextView>(id).text = playerNames()[i].text.toString()

        }
    }

    private fun createAllPlayerScoreEditTextList(list: List<EditText>){
        for(i in list.indices){
            allPlayerScoreEditTextList[i].add(list[i])
        }
    }

    private fun createAllPlayersPenaltyTextViewList(list: List<TextView>){
        for(i in list.indices){
            allPlayerPenaltyTextViewList[i].add(list[i])
        }
    }

    private fun editTextWatcher(editText: EditText) : Boolean{
        var empty = false
        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                empty = viewModel.areAllEditTextsFilled(allPlayerScoreEditTextList as List<List<EditText>>,binding.saveGame)
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

    private fun calculate(){
        var size = allPlayerScoreEditTextList.size
        if(size > SINGLE_PLAYER_SIZE){
            size = SINGLE_PLAYER_SIZE
            for (i in 0 until size){
                viewModel.setTotalScore(requireContext(),allPlayerScoreEditTextList[i],getAllPlayerTotalScoreEditTextList()[i],allPlayerPenaltyTextViewList[i])
            }
        }else{
            for (i in 0 until size){
                viewModel.setTotalScore(requireContext(),allPlayerScoreEditTextList[i],getAllPlayerTotalScoreEditTextList()[i],allPlayerPenaltyTextViewList[i])
            }
        }
   }

    private fun checkDividerList(){
        if(lineCount == 1){
            dividerList.last().visibility = View.GONE
        }else{
            dividerList.last().visibility = View.GONE
            dividerList[dividerList.size-2].visibility = View.VISIBLE
        }
    }


    private fun penalty() {
        binding.penalty.setOnClickListener {
            val rootNull = null
            val inflater = LayoutInflater.from(requireContext())
            val partnerAddPenaltyView = inflater.inflate(R.layout.single_players_add_penalty, rootNull)
            val penaltyView = inflater.inflate(R.layout.add_penalty, rootNull)
            val partnerPlayersRadioGroup = partnerAddPenaltyView.findViewById<RadioGroup>(R.id.singlePlayersRadioGroup)
            val givenPenaltyEditText = penaltyView.findViewById<EditText>(R.id.penalty)
            setPlayerToBePenalized(partnerAddPenaltyView)
            val firstDialog = createAlertDialog(requireContext(), partnerAddPenaltyView, R.string.select_player_punish, requireContext().getString(R.string.forward)) {
                val selectedText = partnerPlayersRadioGroup.findViewById<RadioButton>(partnerPlayersRadioGroup.checkedRadioButtonId)?.text?.toString()
                if (selectedText.isNullOrEmpty()){
                    requireContext().toast(requireContext().getString(R.string.warning_select_player_penalised),R.drawable.ic_warning)
                }else{
                    val secondDialog = createAlertDialog(requireContext(), penaltyView, R.string.determine_punishment, requireContext().getString(R.string.confirm)) {
                        val totalScoreTextView = totalScoreHasMap[selectedText]!!
                        val penalty = givenPenaltyEditText.text.toString().toInt()
                        updatePenaltyTextView(selectedText, penalty)
                        if(totalScoreTextView.text.toString() == ""){
                            totalScoreTextView.text = penalty.toString()
                        }else{
                            val totalScore = totalScoreTextView.text.toString().toInt() + penalty
                            totalScoreTextView.text = totalScore.toString()
                        }
                    }
                    secondDialog.show()
                }
            }
            firstDialog.show()
        }
    }


    private fun updatePenaltyTextView(player: String, penalty: Int) {
        val textViewList = penaltyHashMap[player]!!
        val lastTextView = textViewList.lastOrNull()
        lastTextView?.let {
            val currentPenalty = if (it.text.isEmpty()) 0 else it.text.split(requireContext().getString(R.string.penalty_text))[1].trimStart().toInt()
            it.text = requireContext().getString(R.string.penalty_text_value).format(currentPenalty + penalty)
        }
    }
   private fun saveGame(){
        binding.saveGame.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(requireContext().getString(R.string.confirmation_title))
            alertDialogBuilder.setMessage(requireContext().getString(R.string.confirmation_message))
            alertDialogBuilder.setPositiveButton(requireContext().getString(R.string.confirmation_yes)) { _, _ ->
                viewModel.insertSingleGame(allPlayerScoreEditTextList as List<List<EditText>>,allPlayerPenaltyTextViewList as List<List<TextView>>,playerNames(),totalScoreHasMap,findNavController(),requireContext())
            }
            alertDialogBuilder.setNegativeButton(requireContext().getString(R.string.confirmation_no),null)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun setPlayerToBePenalized(singlePlayerView: View){
        singlePlayerView.findViewById<RadioButton>(R.id.player1).text = binding.player1NameEntry.text.toString()
        singlePlayerView.findViewById<RadioButton>(R.id.player2).text = binding.player2NameEntry.text.toString()
        singlePlayerView.findViewById<RadioButton>(R.id.player3).text = binding.player3NameEntry.text.toString()
        singlePlayerView.findViewById<RadioButton>(R.id.player4).text = binding.player4NameEntry.text.toString()
    }

    private fun goToChooseGameFragment(){
        binding.goToChooseGameFragment.setOnClickListener {
            findNavController().navigate(SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToChooseGameFragment())
        }
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CustomDialog.showConfirmationDialog(
                        requireContext(),
                        "Kaydetmeden çıkmak üzeresiniz",
                        "Kaydetmeden çıkmak istiyor musunuz? Yaptığınız değişiklikler kaybolacak",
                        "Kaydetmeden çık",
                        "Vazgeç",
                        onPositiveClick = {
                            findNavController().navigate(SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToChooseGameFragment())
                        }
                    )
                }
            }
        )
    }

}