package com.keremkulac.okeyscore.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentDenemeBinding
import com.keremkulac.okeyscore.presentation.ui.saveSingleGame.SaveSingleGameViewModel
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import com.keremkulac.okeyscore.util.SINGLE_PLAYER_SIZE
import com.keremkulac.okeyscore.util.createAlertDialog
import com.keremkulac.okeyscore.util.dpToPx
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Deneme : Fragment(R.layout.fragment_deneme) {
    private lateinit var binding: FragmentDenemeBinding
    private var lineCount = 1
    private lateinit var expandableLayoutManager: ExpandableLayoutManager
    private lateinit var expandableLayoutManager2: ExpandableLayoutManager
    private val penaltyHashMap = HashMap<String, List<TextView>>()
    private val viewModel by viewModels<DenemeViewModel>()
    private var playerNames = mutableListOf<String>()
    private var playerScoresTextView = mutableListOf<TextView>()
    private val allPlayerPenaltyTextViewList: List<MutableList<TextView>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val allPlayerScoreEditTextList : List<MutableList<EditText>> = List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    val totalScores = mutableListOf(0, 0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDenemeBinding.bind(view)
        expandableLayoutManager = ExpandableLayoutManager()
        expandableLayoutManager2 = ExpandableLayoutManager()
        createPlayerNames()
        createPlayerScores()
        createNewLine(layoutInflater)
        clickTotalScoresContainer()
        penalty()
        createPenaltyHashMap()
        saveGame()
    }

    private fun createNewLine(inflater: LayoutInflater) {
        val includedLayout = inflateScoreLayout(inflater)
        setupExpandableLayout(includedLayout)
        setupScoreEditTexts(includedLayout)
        addPenaltyTextViews(includedLayout)
        addScoreEditTexts(includedLayout)
        binding.scoreLayout.addView(includedLayout)
        lineCount++
    }

    private fun inflateScoreLayout(inflater: LayoutInflater): View {
        val includedLayout = inflater.inflate(R.layout.score_layout, null)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = requireContext().dpToPx(8)
        layoutParams.bottomMargin = requireContext().dpToPx(8)
        includedLayout.layoutParams = layoutParams
        return includedLayout
    }

    private fun setupExpandableLayout(includedLayout: View) {
        val scoreContainer = includedLayout.findViewById<LinearLayout>(R.id.scoreContainer)
        val mainLayout = includedLayout.findViewById<LinearLayout>(R.id.mainLayout)
        val icon = includedLayout.findViewById<ImageView>(R.id.icon)
        val count = includedLayout.findViewById<TextView>(R.id.roundCount)

        expandableLayoutManager.toggleLayout(scoreContainer, icon)
        count.text = "Tur $lineCount"

        mainLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(scoreContainer, icon)
        }

        if (lineCount == 1) {
            binding.newRound.setOnClickListener {
                createNewLine(LayoutInflater.from(requireContext()))
            }
        }
    }

    private fun setupScoreEditTexts(includedLayout: View) {
        val etList = listOf<EditText>(
            includedLayout.findViewById(R.id.etPlayer1Score),
            includedLayout.findViewById(R.id.etPlayer2Score),
            includedLayout.findViewById(R.id.etPlayer3Score),
            includedLayout.findViewById(R.id.etPlayer4Score)
        )
        etList.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    totalScores[index] = calculateTotalScoreForPlayer(index)
                    updateTotalScoreUI()
                }
            })
        }
    }

    private fun addPenaltyTextViews(includedLayout: View) {
        val penaltyTextViewIds = listOf(
            R.id.etPlayer1Penalty,
            R.id.etPlayer2Penalty,
            R.id.etPlayer3Penalty,
            R.id.etPlayer4Penalty
        )
        val textViewList = penaltyTextViewIds.map { includedLayout.findViewById<TextView>(it) }
        createAllPlayersPenaltyTextViewList(textViewList)
    }

    private fun addScoreEditTexts(includedLayout: View){
        val scoreIdList = listOf(
            R.id.etPlayer1Score,
            R.id.etPlayer2Score,
            R.id.etPlayer3Score,
            R.id.etPlayer4Score
        )
        val editTextList = scoreIdList.map { includedLayout.findViewById<EditText>(it) }
        createAllPlayersScoreEditTextList(editTextList)
    }
    private fun updateTotalScoreUI() {
        totalScores.forEachIndexed { i, score ->
            playerScoresTextView[i].text = score.toString()
        }
    }

    private fun calculateTotalScoreForPlayer(playerIndex: Int): Int {
        val scoreIdList = listOf(
            R.id.etPlayer1Score,
            R.id.etPlayer2Score,
            R.id.etPlayer3Score,
            R.id.etPlayer4Score
        )
        val penaltyIdList = listOf(
            R.id.etPlayer1Penalty,
            R.id.etPlayer2Penalty,
            R.id.etPlayer3Penalty,
            R.id.etPlayer4Penalty
        )
        var totalScore = 0
        val scoreEditTextId = scoreIdList.getOrNull(playerIndex) ?: return 0
        val penaltyTextViewId = penaltyIdList.getOrNull(playerIndex) ?: return 0

        for (i in 0 until binding.scoreLayout.childCount) {
            val card = binding.scoreLayout.getChildAt(i)
            val editText = card.findViewById<EditText>(scoreEditTextId)
            val penaltyTextView = card.findViewById<TextView>(penaltyTextViewId)
            val score = editText.text.toString().toIntOrNull() ?: 0
            val penalty = penaltyTextView.text.toString()
                .split(requireContext().getString(R.string.penalty_text))
                .getOrNull(1)?.trim()?.toIntOrNull() ?: 0
            totalScore += (score + penalty)
        }
        return totalScore
    }

    private fun clickTotalScoresContainer() {
        binding.totalScoresCardView.setOnClickListener {
            expandableLayoutManager2.toggleLayout(
                binding.totalScoreContainer,
                binding.totalScoresIcon
            )
        }
    }

    private fun createTotalScoresTextView(): HashMap<String, TextView> {
        val hashMap = HashMap<String, TextView>()
        playerNames.forEachIndexed { index, name ->
            hashMap[name] = playerScoresTextView[index]
        }
        return hashMap
    }

    private fun penalty() {
        binding.penalty.setOnClickListener {
            val rootNull = null
            val inflater = LayoutInflater.from(requireContext())
            val partnerAddPenaltyView =
                inflater.inflate(R.layout.single_players_add_penalty, rootNull)
            val penaltyView = inflater.inflate(R.layout.add_penalty, rootNull)
            val partnerPlayersRadioGroup =
                partnerAddPenaltyView.findViewById<RadioGroup>(R.id.singlePlayersRadioGroup)
            val givenPenaltyEditText = penaltyView.findViewById<EditText>(R.id.penalty)
            setPlayerToBePenalized(partnerAddPenaltyView)
            val firstDialog = createAlertDialog(
                requireContext(),
                partnerAddPenaltyView,
                R.string.select_player_punish,
                requireContext().getString(R.string.forward)
            ) {
                val selectedText =
                    partnerPlayersRadioGroup.findViewById<RadioButton>(partnerPlayersRadioGroup.checkedRadioButtonId)?.text?.toString()
                if (selectedText.isNullOrEmpty()) {
                    requireContext().toast(
                        requireContext().getString(R.string.warning_select_player_penalised),
                        R.drawable.ic_warning
                    )
                } else {
                    val secondDialog = createAlertDialog(
                        requireContext(),
                        penaltyView,
                        R.string.determine_punishment,
                        requireContext().getString(R.string.confirm)
                    ) {
                        val totalScoreTextView = createTotalScoresTextView()[selectedText]
                        val penalty = givenPenaltyEditText.text.toString().toInt()
                        updatePenaltyTextView(selectedText, penalty)
                        if (totalScoreTextView!!.text.toString() == "") {
                            totalScoreTextView.text = penalty.toString()
                            totalScoreTextView.visibility = View.VISIBLE
                        } else {
                            val totalScore = totalScoreTextView.text.toString().toInt() + penalty
                            totalScoreTextView.text = totalScore.toString()
                            totalScoreTextView.visibility = View.VISIBLE
                        }
                    }
                    secondDialog.show()
                }
            }
            firstDialog.show()

        }

    }

    private fun createAllPlayersPenaltyTextViewList(list: List<TextView>) {
        for (i in list.indices) {
            allPlayerPenaltyTextViewList[i].add(list[i])
        }
    }

    private fun createAllPlayersScoreEditTextList(list: List<EditText>){
        for (i in list.indices){

        }
    }

    private fun createPlayerNames() {
        playerNames = mutableListOf(
            binding.etPlayer1Name.text.trim().toString(),
            binding.etPlayer2Name.text.trim().toString(),
            binding.etPlayer3Name.text.trim().toString(),
            binding.etPlayer4Name.text.trim().toString()
        )
    }

    private fun createPlayerScores() {
        playerScoresTextView = mutableListOf(
            binding.etPlayer1TotalScore,
            binding.etPlayer2TotalScore,
            binding.etPlayer3TotalScore,
            binding.etPlayer4TotalScore
        )
    }

    private fun createPenaltyHashMap() {
        for ((i, list) in allPlayerPenaltyTextViewList.withIndex()) {
            penaltyHashMap[playerNames[i]] = list
        }
    }

    private fun updatePenaltyTextView(player: String, penalty: Int) {
        val playerIndex = playerNames.indexOf(player)
        if (playerIndex == -1) return
        val textViewList = penaltyHashMap[player]
        val lastTextView = textViewList?.lastOrNull() ?: return
        val currentPenalty = lastTextView.text.toString()
            .split(requireContext().getString(R.string.penalty_text))
            .getOrNull(1)?.trim()?.toIntOrNull() ?: 0

        lastTextView.text = requireContext().getString(R.string.penalty_text_value)
            .format(currentPenalty + penalty)

        updateTotalScoreUI()
    }

    private fun setPlayerToBePenalized(singlePlayerView: View) {
        singlePlayerView.findViewById<RadioButton>(R.id.player1).text = playerNames[0]
        singlePlayerView.findViewById<RadioButton>(R.id.player2).text = playerNames[1]
        singlePlayerView.findViewById<RadioButton>(R.id.player3).text = playerNames[2]
        singlePlayerView.findViewById<RadioButton>(R.id.player4).text = playerNames[3]
    }

    private fun saveGame() {
        binding.saveScores.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(requireContext().getString(R.string.confirmation_title))
            alertDialogBuilder.setMessage(requireContext().getString(R.string.confirmation_message))
            alertDialogBuilder.setPositiveButton(requireContext().getString(R.string.confirmation_yes)) { _, _ ->
                viewModel.insertSingleGame(
                    allPlayerScoreEditTextList as List<List<EditText>>,
                    allPlayerPenaltyTextViewList as List<List<TextView>>,
                    playerNames,
                    createTotalScoresTextView(),
                    findNavController(),
                    requireContext()
                )
            }
            alertDialogBuilder.setNegativeButton(
                requireContext().getString(R.string.confirmation_no),
                null
            )
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

}