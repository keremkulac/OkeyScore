package com.keremkulac.okeyscore.presentation.ui.savePartnerGame

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
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
import com.keremkulac.okeyscore.databinding.FragmentSavePartnerGameBinding
import com.keremkulac.okeyscore.util.CustomDialog
import com.keremkulac.okeyscore.util.PARTNER_PLAYER_SIZE
import com.keremkulac.okeyscore.util.SINGLE_PLAYER_SIZE
import com.keremkulac.okeyscore.util.createAlertDialog
import com.keremkulac.okeyscore.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavePartnerGameFragment : Fragment(R.layout.fragment_save_partner_game) {
    private val viewModel by viewModels<SavePartnerGameViewModel>()
    private lateinit var binding: FragmentSavePartnerGameBinding
    private val allTeamScoreEditTextList: List<MutableList<EditText>> =
        List(SINGLE_PLAYER_SIZE) { mutableListOf() }
    private val allTeamPenaltyTextViewList: List<MutableList<TextView>> =
        List(PARTNER_PLAYER_SIZE) { mutableListOf() }
    private val dividerList: java.util.ArrayList<MaterialDivider> = java.util.ArrayList()
    private val penaltyHashMap = HashMap<String, List<TextView>>()
    private val totalScoreHasMap = HashMap<String, TextView>()
    private var lineCount = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavePartnerGameBinding.bind(view)
        handleOnBackPressed()
        checkTeamNames()
        saveToRoomDb()
        newRound(layoutInflater)
        goToChooseGameFragment()
        penalty()
    }

    private fun playerNames(): List<EditText> {
        return listOf(
            binding.team1NameEntry,
            binding.team2NameEntry
        )
    }

    private fun checkTeamNames() {
        binding.confirmNames.setOnClickListener {
            if (viewModel.checkList(playerNames())) {
                requireContext().toast(
                    requireContext().getString(R.string.warning_enter_all_team_names),
                    R.drawable.ic_warning
                )
            } else {
                if (viewModel.sameNamesCheck(playerNames())) {
                    requireContext().toast(
                        requireContext().getString(R.string.enter_different_names),
                        R.drawable.ic_warning
                    )
                } else {
                    binding.cardView.visibility = View.GONE
                    binding.newRound.visibility = View.VISIBLE
                    binding.saveGame.visibility = View.VISIBLE
                    binding.scoreLayout.visibility = View.VISIBLE
                    binding.roundScoreTitle.visibility = View.VISIBLE
                    binding.teamNamesAndScoresLayout.visibility = View.VISIBLE
                    binding.teamNamesTitle.visibility = View.VISIBLE
                    binding.penalty.visibility = View.VISIBLE
                    binding.saveGame.isEnabled = false
                    calculate()
                    setPlayerNames()
                    createTotalScoreHashMap()
                    createPenaltyHashMap(allTeamPenaltyTextViewList as List<List<TextView>>)
                }

            }
        }
    }

    private fun setPlayerNames() {
        for (i in playerNames().indices) {
            val id = resources.getIdentifier("team${i + 1}Name", "id", requireContext().packageName)
            requireActivity().findViewById<TextView>(id).text = playerNames()[i].text.toString()
        }
    }

    private fun checkRoundScores(newLine: List<EditText>) {
        for (i in newLine.indices) {
            editTextWatcher(newLine[i])
        }
    }

    private fun newRound(inflater: LayoutInflater) {
        var newLine = createNewLine(inflater)
        createAllTeamScoreEditTextList(newLine)
        checkRoundScores(newLine)
        checkDividerList()
        binding.newRound.setOnClickListener {
            if (viewModel.checkList(newLine)) {
                requireContext().toast(
                    resources.getString(R.string.warning_check_all_rounds).format(lineCount),
                    R.drawable.ic_warning
                )
            } else {
                lineCount++
                newLine = createNewLine(inflater)
                createAllTeamScoreEditTextList(newLine)
                checkRoundScores(newLine)
                checkDividerList()
                calculate()
                binding.saveGame.isEnabled = false
            }
        }
    }

    private fun createNewLine(inflater: LayoutInflater): List<EditText> {
        val nullParent: ViewGroup? = null
        val parentLayout = binding.scoreLayout
        val includedLayout = inflater.inflate(R.layout.partner_game_round_item, nullParent)
        parentLayout.addView(includedLayout)
        val penaltyTextViewIds = listOf(R.id.team1Penalty, R.id.team2Penalty)
        val editTextIds = listOf(R.id.team1Score, R.id.team2Score)
        val count = includedLayout.findViewById<TextView>(R.id.roundCount)
        count.text = lineCount.toString()
        val editTextList = mutableListOf<EditText>()
        val textViewList = mutableListOf<TextView>()
        for (id in editTextIds) {
            val editText = includedLayout.findViewById<EditText>(id)
            editTextList.add(editText)
        }
        for (id in penaltyTextViewIds) {
            val textView = includedLayout.findViewById<TextView>(id)
            textViewList.add(textView)
        }
        dividerList.add(includedLayout.findViewById(R.id.horizontalDivider))
        createAllTeamPenaltyTextViewList(textViewList)
        return editTextList
    }


    private fun createAllTeamScoreEditTextList(list: List<EditText>) {
        for (i in list.indices) {
            allTeamScoreEditTextList[i].add(list[i])
        }
    }

    private fun createAllTeamPenaltyTextViewList(list: List<TextView>) {
        for (i in list.indices) {
            allTeamPenaltyTextViewList[i].add(list[i])
        }
    }

    private fun checkDividerList() {
        if (lineCount == 1) {
            dividerList.last().visibility = View.GONE
        } else {
            dividerList.last().visibility = View.GONE
            dividerList[dividerList.size - 2].visibility = View.VISIBLE
        }
    }

    private fun saveToRoomDb() {
        binding.saveGame.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle(requireContext().getString(R.string.confirmation_title))
            alertDialogBuilder.setMessage(requireContext().getString(R.string.confirmation_message))
            alertDialogBuilder.setPositiveButton(requireContext().getString(R.string.confirmation_yes)) { _, _ ->
                viewModel.insertFinishedGame(
                    allTeamScoreEditTextList as ArrayList<ArrayList<EditText>>,
                    allTeamPenaltyTextViewList as List<List<TextView>>,
                    playerNames(),
                    totalScoreHasMap,
                    findNavController(),
                    requireContext()
                )
            }
            alertDialogBuilder.setNegativeButton(requireContext().getString(R.string.confirmation_no)) { _, _ -> }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun getAllTeamTotalScoreEditTextList(): List<TextView> {
        return listOf(
            binding.team1TotalScore,
            binding.team2TotalScore
        )
    }

    private fun createPenaltyHashMap(allTeamPenaltyTextViewList: List<List<TextView>>) {
        for ((i, list) in allTeamPenaltyTextViewList.withIndex()) {
            penaltyHashMap[playerNames()[i].text.toString()] = list
        }
    }

    private fun createTotalScoreHashMap() {
        for ((i, listItem) in getAllTeamTotalScoreEditTextList().withIndex()) {
            totalScoreHasMap[playerNames()[i].text.toString()] = listItem
        }
    }

    private fun calculate() {
        var size = allTeamScoreEditTextList.size
        if (size > PARTNER_PLAYER_SIZE) {
            size = PARTNER_PLAYER_SIZE
            for (i in 0 until size) {
                viewModel.setTotalScore(
                    requireContext(),
                    allTeamScoreEditTextList[i],
                    getAllTeamTotalScoreEditTextList()[i],
                    allTeamPenaltyTextViewList[i]
                )
            }
        } else {
            for (i in 0 until size) {
                viewModel.setTotalScore(
                    requireContext(),
                    allTeamScoreEditTextList[i],
                    getAllTeamTotalScoreEditTextList()[i],
                    allTeamPenaltyTextViewList[i]
                )
            }
        }
    }

    private fun editTextWatcher(editText: EditText): Boolean {
        var empty = false
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                empty = viewModel.areAllEditTextsFilled(
                    allTeamScoreEditTextList as ArrayList<ArrayList<EditText>>,
                    binding.saveGame
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                bottomNavigation.visibility = View.GONE
            }
        }
        return empty
    }

    private fun penalty() {
        binding.penalty.setOnClickListener {
            val rootNull = null
            val inflater = LayoutInflater.from(requireContext())
            val partnerAddPenaltyView =
                inflater.inflate(R.layout.partner_players_add_penalty, rootNull)
            val penaltyView = inflater.inflate(R.layout.add_penalty, rootNull)
            val partnerPlayersRadioGroup =
                partnerAddPenaltyView.findViewById<RadioGroup>(R.id.partnerPlayersRadioGroup)
            val givenPenaltyEditText = penaltyView.findViewById<EditText>(R.id.penalty)
            setTeamToBePenalized(partnerAddPenaltyView)
            val firstDialog = createAlertDialog(
                requireContext(),
                partnerAddPenaltyView,
                R.string.select_team_punish,
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
                        val totalScoreTextView = totalScoreHasMap[selectedText]!!
                        val penalty = givenPenaltyEditText.text.toString().toInt()
                        updatePenaltyTextView(selectedText, penalty)
                        if (totalScoreTextView.text.toString() == "") {
                            totalScoreTextView.text = penalty.toString()
                        } else {
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
            val currentPenalty =
                if (it.text.isEmpty()) 0 else it.text.split(requireContext().getString(R.string.penalty_text))[1].trimStart()
                    .toInt()
            it.text = requireContext().getString(R.string.penalty_text_value)
                .format(currentPenalty + penalty)
        }
    }

    private fun setTeamToBePenalized(singlePlayerView: View) {
        singlePlayerView.findViewById<RadioButton>(R.id.team1).text =
            binding.team1NameEntry.text.toString()
        singlePlayerView.findViewById<RadioButton>(R.id.team2).text =
            binding.team2NameEntry.text.toString()
    }


    private fun goToChooseGameFragment() {
        binding.goToChooseGameFragment.setOnClickListener {
            findNavController().navigate(SavePartnerGameFragmentDirections.actionSavePartnerGameFragmentToChooseGameFragment())
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
                            findNavController().navigate(SavePartnerGameFragmentDirections.actionMainActivityToChooseGameFragment())
                        }
                    )
                }
            }
        )
    }

}