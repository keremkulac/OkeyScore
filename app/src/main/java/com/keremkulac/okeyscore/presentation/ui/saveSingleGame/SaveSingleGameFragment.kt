package com.keremkulac.okeyscore.presentation.ui.saveSingleGame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSaveSingleGameBinding
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.model.Player
import com.keremkulac.okeyscore.util.CustomDialog
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import com.keremkulac.okeyscore.util.SINGLE_PLAYER_SIZE
import com.keremkulac.okeyscore.util.createAlertDialog
import com.keremkulac.okeyscore.util.dpToPx
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveSingleGameFragment : Fragment(R.layout.fragment_save_single_game) {
    private lateinit var binding: FragmentSaveSingleGameBinding
    private val viewModel: SaveSingleGameViewModel by viewModels()
    private var lineCount = 1
    private lateinit var expandableLayoutManager: ExpandableLayoutManager
    private lateinit var expandableLayoutManager2: ExpandableLayoutManager
    private val penaltyHashMap = HashMap<String, List<TextView>>()
    private var playerNames = mutableListOf<String>()
    private var playerScoresTextView = mutableListOf<TextView>()
    private val allPlayerPenaltyTextViewList: List<MutableList<TextView>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val allPlayerScoreEditTextList: List<MutableList<EditText>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    val totalScores = mutableListOf(0, 0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveSingleGameBinding.bind(view)
        expandableLayoutManager = ExpandableLayoutManager()
        expandableLayoutManager2 = ExpandableLayoutManager()
        createPlayerScores()
        penalty()
        confirmNames()
        saveFinishedGame()
        observeValidation()
        handleOnBackPressed()
    }

    private fun createNewLine(inflater: LayoutInflater) {
        val includedLayout = inflateScoreLayout(inflater)
        setupExpandableLayout(includedLayout)
        setupScoreEditTexts(includedLayout)
        addPenaltyTextViews(includedLayout)
        addScoreEditTexts(includedLayout)
        setupPlayerNames(includedLayout)
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
        val roundLayout = includedLayout.findViewById<ConstraintLayout>(R.id.roundLayout)
        val icon = includedLayout.findViewById<ImageView>(R.id.icon)
        val count = includedLayout.findViewById<TextView>(R.id.roundCount)
        expandableLayoutManager.toggleLayout(scoreContainer, icon)
        count.text = requireContext().getString(R.string.round_count).format(lineCount)
        roundLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(scoreContainer, icon)
        }
        binding.newRound.setOnClickListener {
            if (viewModel.checkAllRoundScoreFilled(
                    allPlayerScoreEditTextList[allPlayerScoreEditTextList.size - 1],
                    getString(R.string.warning_check_all_rounds).format(lineCount-1)
                )
            ) {
                expandableLayoutManager.expandLayout(scoreContainer, icon)
                createNewLine(LayoutInflater.from(requireContext()))
            }
        }
    }

    private fun setupScoreEditTexts(includedLayout: View) {
        val playerScoreList = listOf<EditText>(
            includedLayout.findViewById(R.id.player1Score),
            includedLayout.findViewById(R.id.player2Score),
            includedLayout.findViewById(R.id.player3Score),
            includedLayout.findViewById(R.id.player4Score)
        )
        playerScoreList.forEachIndexed { index, editText ->
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

    private fun setupPlayerNames(includedLayout: View) {
        val playerNameList = listOf<TextView>(
            includedLayout.findViewById(R.id.player1Name),
            includedLayout.findViewById(R.id.player2Name),
            includedLayout.findViewById(R.id.player3Name),
            includedLayout.findViewById(R.id.player4Name)
        )
        playerNameList.forEachIndexed { index, textView ->
            textView.text = playerNames[index]
        }
    }

    private fun setupTotalScorePlayerNames() {
        val playerNameList = listOf(
            binding.totalScorePlayer1Name,
            binding.totalScorePlayer2Name,
            binding.totalScorePlayer3Name,
            binding.totalScorePlayer4Name,
        )
        playerNameList.forEachIndexed { index, textView ->
            textView.text = playerNames[index]
        }
    }

    private fun confirmNames() {
        binding.apply {
            confirmNames.setOnClickListener {
                createPlayerNames()
                if (viewModel.checkPlayerNames(
                        playerNames,
                        getString(R.string.validation_message_fill_all_player_names)
                    ) && viewModel.sameNamesCheck(
                        playerNames,
                        getString(R.string.validation_message_check_same_player_names)
                    )
                ) {
                    createPenaltyHashMap()
                    playerNameEntryCardView.visibility = View.GONE
                    playerScoresTitle.visibility = View.VISIBLE
                    playerTotalScoreCardView.visibility = View.VISIBLE
                    scrollView.visibility = View.VISIBLE
                    createNewLine(layoutInflater)
                    setupTotalScorePlayerNames()
                }
            }
        }
    }

    private fun addPenaltyTextViews(includedLayout: View) {
        val penaltyTextViewIds = listOf(
            R.id.player1Penalty,
            R.id.player2Penalty,
            R.id.player3Penalty,
            R.id.player4Penalty
        )
        val textViewList = penaltyTextViewIds.map { includedLayout.findViewById<TextView>(it) }
        createAllPlayersPenaltyTextViewList(textViewList)
    }

    private fun addScoreEditTexts(includedLayout: View) {
        val scoreIdList = listOf(
            R.id.player1Score,
            R.id.player2Score,
            R.id.player3Score,
            R.id.player4Score
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
            R.id.player1Score,
            R.id.player2Score,
            R.id.player3Score,
            R.id.player4Score
        )
        val penaltyIdList = listOf(
            R.id.player1Penalty,
            R.id.player2Penalty,
            R.id.player3Penalty,
            R.id.player4Penalty
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
                        if (totalScoreTextView!!.text.toString() == "") {
                            totalScoreTextView.text = penalty.toString()
                            totalScoreTextView.visibility = View.VISIBLE
                        } else {
                            val totalScore = totalScoreTextView.text.toString().toInt() + penalty
                            totalScoreTextView.text = totalScore.toString()
                            totalScoreTextView.visibility = View.VISIBLE
                        }
                        updatePenaltyTextView(selectedText, penalty)
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

    private fun createAllPlayersScoreEditTextList(list: List<EditText>) {
        for (i in list.indices) {
            allPlayerScoreEditTextList[i].add(list[i])
        }
    }

    private fun createPlayerNames() {
        playerNames = mutableListOf(
            binding.player1NameEntry.text?.trim().toString(),
            binding.player2NameEntry.text?.trim().toString(),
            binding.player3NameEntry.text?.trim().toString(),
            binding.player4NameEntry.text?.trim().toString(),
        )
    }

    private fun createPlayerScores() {
        playerScoresTextView = mutableListOf(
            binding.player1TotalScore,
            binding.player2TotalScore,
            binding.player3TotalScore,
            binding.player4TotalScore
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
        totalScores[playerIndex] = calculateTotalScoreForPlayer(playerIndex)
        updateTotalScoreUI()
    }

    private fun setPlayerToBePenalized(singlePlayerView: View) {
        singlePlayerView.findViewById<RadioButton>(R.id.player1).text = playerNames[0]
        singlePlayerView.findViewById<RadioButton>(R.id.player2).text = playerNames[1]
        singlePlayerView.findViewById<RadioButton>(R.id.player3).text = playerNames[2]
        singlePlayerView.findViewById<RadioButton>(R.id.player4).text = playerNames[3]
    }

    private fun saveFinishedGame() {
        binding.saveScores.setOnClickListener {
            if (viewModel.checkAllRoundScoreFilled(
                    allPlayerScoreEditTextList[allPlayerScoreEditTextList.size - 1],
                    getString(R.string.warning_check_all_rounds).format(lineCount-1)
                )
            ) {
                CustomDialog.showConfirmationDialog(
                    requireContext(),
                    requireContext().getString(R.string.confirmation_title),
                    requireContext().getString(R.string.confirmation_message),
                    requireContext().getString(R.string.confirmation_yes),
                    requireContext().getString(R.string.confirmation_no)
                ) {
                    val players = (0 until 4).map { index ->
                        val name = playerNames[index]
                        val totalScore = totalScores[index].toString()
                        val scores = allPlayerScoreEditTextList[index].map { it.text.toString() }
                        val penalties = allPlayerPenaltyTextViewList[index].map {
                            it.text.toString()
                                .split(requireContext().getString(R.string.penalty_text))
                                .getOrNull(1)?.trim() ?: "0"
                        }
                        Player(
                            id = index,
                            name = name,
                            allScores = scores,
                            totalScore = totalScore,
                            penalties = penalties
                        )
                    }
                    val finishedGame = FinishedSingleGame(
                        id = 0,
                        player1 = players.getOrNull(0),
                        player2 = players.getOrNull(1),
                        player3 = players.getOrNull(2),
                        player4 = players.getOrNull(3),
                        gameInfo = viewModel.createInfo(players, requireContext())
                    )
                    viewModel.saveFinishedGame(finishedGame)
                    findNavController().navigate(SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToChooseGameFragment())
                }
            }
        }
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    CustomDialog.showConfirmationDialog(
                        requireContext(),
                        requireContext().getString(R.string.exit_confirmation_title),
                        requireContext().getString(R.string.exit_confirmation_message),
                        requireContext().getString(R.string.exit_confirmation_yes),
                        requireContext().getString(R.string.exit_confirmation_no),
                        onPositiveClick = {
                            findNavController().navigate(SaveSingleGameFragmentDirections.actionSaveSingleGameFragmentToChooseGameFragment())
                        }
                    )
                }
            }
        )
    }
}