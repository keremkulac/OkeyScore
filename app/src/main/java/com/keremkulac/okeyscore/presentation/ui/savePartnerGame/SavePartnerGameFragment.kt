package com.keremkulac.okeyscore.presentation.ui.savePartnerGame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentSavePartnerGameBinding
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.Player
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.CustomDialog
import com.keremkulac.okeyscore.util.ExpandableLayoutManager
import com.keremkulac.okeyscore.util.InterstitialAdManager
import com.keremkulac.okeyscore.util.SINGLE_PLAYER_SIZE
import com.keremkulac.okeyscore.util.createAlertDialog
import com.keremkulac.okeyscore.util.dpToPx
import com.keremkulac.okeyscore.util.observeValidationMessage
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavePartnerGameFragment : BaseFragment<FragmentSavePartnerGameBinding>(
    FragmentSavePartnerGameBinding::inflate
) {
    private val viewModel: SavePartnerGameViewModel by viewModels()
    private var lineCount = 1
    private lateinit var expandableLayoutManager: ExpandableLayoutManager
    private lateinit var expandableLayoutManager2: ExpandableLayoutManager
    private lateinit var adManager: InterstitialAdManager
    private val penaltyHashMap = HashMap<String, List<TextView>>()
    private var playerNames = mutableListOf<String>()
    private var teamNames = mutableListOf<String>()
    private var playerScoresTextView = mutableListOf<TextView>()
    private val allPlayerPenaltyTextViewList: List<MutableList<TextView>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val allPlayerScoreEditTextList: List<MutableList<EditText>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    val totalScores = mutableListOf(0, 0, 0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adManager = InterstitialAdManager
        adManager.loadAd(requireActivity())
        expandableLayoutManager = ExpandableLayoutManager()
        expandableLayoutManager2 = ExpandableLayoutManager()
        createPlayerScores()
        clickTeam1Layout()
        clickTeam2Layout()
        penalty()
        confirmTeam1Names()
        saveFinishedGame()
        observeValidationMessage(viewModel.validationMessage)
        handleOnBackPressed()
        toolbarBackButtonClick()
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
                    getString(R.string.warning_check_all_rounds).format(lineCount - 1)
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

    private fun setupTeamNames() = with(binding) {
        team1Name.text = teamNames[0]
        team2Name.text = teamNames[1]
    }

    private fun setupTotalScorePlayerNames() = with(binding) {
        val playerNameList = listOf(
            totalScorePlayer1Name,
            totalScorePlayer2Name,
            totalScorePlayer3Name,
            totalScorePlayer4Name,
        )
        playerNameList.forEachIndexed { index, textView ->
            textView.text = playerNames[index]
        }
    }

    private fun confirmTeam2Names() = with(binding) {
        confirmTeam2Names.setOnClickListener {
            if (viewModel.checkTeamAndPlayerNames(
                    team2NameEntry.text.toString(),
                    team2Player1NameEntry.text.toString(),
                    team2Player2NameEntry.text.toString(),
                    getString(R.string.warning_check_team_names),
                    getString(R.string.warning_check_team_player_1_name),
                    getString(R.string.warning_check_team_player_2_name),
                    getString(R.string.warning_same_players_names)
                ) && viewModel.sameNamesCheck(
                    playerNames,
                    getString(R.string.validation_message_check_same_player_names)
                )
            ) {
                createTeamNames()
                createPlayerNames()
                createPenaltyHashMap()
                team2EntryCardView.visibility = View.GONE
                teamScoreCardView.visibility = View.VISIBLE
                scoreEntryTitle.visibility = View.VISIBLE
                scrollView.visibility = View.VISIBLE
                createNewLine(layoutInflater)
                setupTotalScorePlayerNames()
            }
        }
    }

    private fun confirmTeam1Names() = with(binding) {
        confirmTeam1Names.setOnClickListener {
            if (viewModel.checkTeamAndPlayerNames(
                    team1NameEntry.text.toString(),
                    team1Player1NameEntry.text.toString(),
                    team1Player2NameEntry.text.toString(),
                    getString(R.string.warning_check_team_names),
                    getString(R.string.warning_check_team_player_1_name),
                    getString(R.string.warning_check_team_player_2_name),
                    getString(R.string.warning_same_players_names)
                )
            ) {
                confirmTeam2Names()
                team2EntryCardView.visibility = View.VISIBLE
                team1EntryCardView.visibility = View.GONE
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
        createTeamTotalScores()
        createTeamTotalScores()
    }

    private fun calculateTotalScoreForPlayer(playerIndex: Int): Int = with(binding) {
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
        for (i in 0 until scoreLayout.childCount) {
            val card = scoreLayout.getChildAt(i)
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

    private fun clickTeam1Layout() = with(binding) {
        val expandableLayoutManager = ExpandableLayoutManager()
        team1MainLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(team1Layout, team1MainIcon)
        }
    }

    private fun clickTeam2Layout() = with(binding) {
        val expandableLayoutManager = ExpandableLayoutManager()
        team2MainLayout.setOnClickListener {
            expandableLayoutManager.toggleLayout(team2Layout, team2MainIcon)
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
            val playersPenaltyAddView = inflater.inflate(R.layout.players_add_penalty, rootNull)
            val penaltyView = inflater.inflate(R.layout.add_penalty, rootNull)
            val playersRadioGroup =
                playersPenaltyAddView.findViewById<RadioGroup>(R.id.singlePlayersRadioGroup)
            val forward = playersPenaltyAddView.findViewById<Button>(R.id.forward)
            val confirm = penaltyView.findViewById<Button>(R.id.confirm)
            val givenPenaltyEditText = penaltyView.findViewById<EditText>(R.id.penalty)
            setPlayerToBePenalized(playersPenaltyAddView)
            val firstDialog = createAlertDialog(requireContext(), playersPenaltyAddView)
            forward.setOnClickListener {
                val selectedText =
                    playersRadioGroup.findViewById<RadioButton>(playersRadioGroup.checkedRadioButtonId)?.text?.toString()
                if (viewModel.checkPenaltyPlayer(
                        selectedText,
                        getString(R.string.warning_select_player_penalised)
                    )
                ) {
                    firstDialog.dismiss()
                    val secondDialog = createAlertDialog(
                        requireContext(),
                        penaltyView
                    )
                    confirm.setOnClickListener {
                        val totalScoreTextView = createTotalScoresTextView()[selectedText]!!
                        val penalty = givenPenaltyEditText.text.toString()
                        if (viewModel.checkPenaltyValue(
                                penalty,
                                getString(R.string.warning_select_player_value_penalised)
                            )
                        ) {
                            val totalScore =
                                totalScoreTextView.text.toString().toInt() + penalty.toInt()
                            totalScoreTextView.text = totalScore.toString()
                            updatePenaltyTextView(selectedText!!, penalty.toInt())
                            secondDialog.dismiss()
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

    private fun createAllPlayersScoreEditTextList(list: List<EditText>) {
        for (i in list.indices) {
            allPlayerScoreEditTextList[i].add(list[i])
        }
    }

    private fun createPlayerNames() = with(binding) {
        playerNames = mutableListOf(
            team1Player1NameEntry.text?.trim().toString(),
            team1Player2NameEntry.text?.trim().toString(),
            team2Player1NameEntry.text?.trim().toString(),
            team2Player2NameEntry.text?.trim().toString(),
        )
    }

    private fun createTeamNames() = with(binding) {
        teamNames = mutableListOf(
            team1NameEntry.text?.trim().toString(),
            team2NameEntry.text?.trim().toString(),
        )
        setupTeamNames()
    }

    private fun createPlayerScores() = with(binding) {
        playerScoresTextView = mutableListOf(
            player1TotalScore,
            player2TotalScore,
            player3TotalScore,
            player4TotalScore
        )
    }

    private fun createTeamTotalScores() = with(binding) {
        val team1Score = player1TotalScore.text.toString()
            .toInt() + player2TotalScore.text.toString().toInt()
        val team2Score = player3TotalScore.text.toString()
            .toInt() + player4TotalScore.text.toString().toInt()
        team1TotalScore.text = getString(R.string.team_total_score_text).format(team1Score)
        team2TotalScore.text = getString(R.string.team_total_score_text).format(team2Score)
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

    private fun saveFinishedGame() = with(binding) {
        binding.saveScores.setOnClickListener {
            if (viewModel.checkAllRoundScoreFilled(
                    allPlayerScoreEditTextList[allPlayerScoreEditTextList.size - 1],
                    getString(R.string.warning_check_all_rounds).format(lineCount - 1)
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
                    val finishedPartnerGame = FinishedPartnerGame(
                        id = 0,
                        team1Name = team1Name.text.toString(),
                        team2Name = team2Name.text.toString(),
                        team1TotalScore = team1TotalScore.text.toString()
                            .split(" ")[1].toInt(),
                        team2TotalScore = team2TotalScore.text.toString()
                            .split(" ")[1].toInt(),
                        team1Player1 = players.getOrNull(0),
                        team1Player2 = players.getOrNull(1),
                        team2Player1 = players.getOrNull(2),
                        team2Player2 = players.getOrNull(3),
                        gameInfo = viewModel.createInfo(players, teamNames, requireContext())
                    )
                    if (adManager.isAdLoaded()) {
                        adManager.showAd(requireActivity(), onDismissed = {
                            completeSaveAndNavigate(finishedPartnerGame)
                            adManager.clear()
                        })
                    } else {
                        completeSaveAndNavigate(finishedPartnerGame)
                    }
                }
            }
        }
    }

    private fun completeSaveAndNavigate(finishedPartnerGame: FinishedPartnerGame) {
        viewModel.savePartnerGame(finishedPartnerGame)
        requireContext().toast(
            getString(R.string.warning_successful_game_save),
            R.drawable.ic_successful
        )
        findNavController().navigate(SavePartnerGameFragmentDirections.actionSavePartnerGameFragmentToChooseGameFragment())
    }

    private fun handleOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backChooseGameFragment()
                }
            }
        )
    }

    private fun backChooseGameFragment() {
        CustomDialog.showConfirmationDialog(
            requireContext(),
            requireContext().getString(R.string.exit_confirmation_title),
            requireContext().getString(R.string.exit_confirmation_message),
            requireContext().getString(R.string.exit_confirmation_yes),
            requireContext().getString(R.string.exit_confirmation_no),
            onPositiveClick = {
                findNavController().navigate(SavePartnerGameFragmentDirections.actionSavePartnerGameFragmentToChooseGameFragment())
            }
        )
    }

    private fun toolbarBackButtonClick() {
        binding.toolbar.setNavigationOnClickListener {
            backChooseGameFragment()
        }
    }
}