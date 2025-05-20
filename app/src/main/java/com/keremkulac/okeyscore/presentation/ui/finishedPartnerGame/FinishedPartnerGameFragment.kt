package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameBinding
import com.keremkulac.okeyscore.presentation.ui.finishedGameView.FinishedGameViewFragmentDirections
import com.keremkulac.okeyscore.util.SwipeGesture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FinishedPartnerGameFragment : Fragment(R.layout.fragment_finished_partner_game) {

    @Inject
    lateinit var finishedPartnerGameAdapter: FinishedPartnerGameAdapter
    private val viewModel by viewModels<FinishedPartnerGameViewModel>()
    private lateinit var binding: FragmentFinishedPartnerGameBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedPartnerGameBinding.bind(view)
        createPartnerGame()
        observeAllFinishedGame()
        setRecyclerView()
        deleteItemDatabase()
        clickFinishedGame()
        search()
    }

    private fun setRecyclerView() {
        binding.finishedGameRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame() {
        viewModel.finishedPartnerGame.observe(viewLifecycleOwner) { finishedList ->
            if (finishedList.isNotEmpty()) {
                binding.createPartnerGame.visibility = View.GONE
                binding.recordNotFoundImage.visibility = View.GONE
                finishedPartnerGameAdapter.finishedPartnerGameLists = ArrayList(finishedList)
                binding.finishedGameRecyclerView.adapter = finishedPartnerGameAdapter
            } else {
                binding.createPartnerGame.visibility = View.VISIBLE
                binding.recordNotFoundImage.visibility = View.VISIBLE
            }
        }
    }

    private fun observeFilteredList() {
        viewModel.filteredList.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList.isNotEmpty()) {
                binding.createPartnerGame.visibility = View.GONE
                binding.recordNotFoundImage.visibility = View.GONE
            } else {
                binding.createPartnerGame.visibility = View.VISIBLE
                binding.recordNotFoundImage.visibility = View.VISIBLE
            }
        }
    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText, finishedPartnerGameAdapter)
                observeFilteredList()
                if (newText.isNullOrEmpty()) {
                    observeAllFinishedGame()
                }
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            observeAllFinishedGame()
            false
        }
    }


    private fun deleteItemDatabase() {
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val itemToDelete = finishedPartnerGameAdapter.finishedPartnerGameLists[position]
                viewModel.deleteFinishedGame(itemToDelete)
                val action =
                    FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentSelf("partner")
                findNavController().navigate(action)
                Snackbar.make(
                    binding.root,
                    requireContext().getString(R.string.deleted),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(requireContext().getString(R.string.take_it_back)) {
                        viewModel.saveFinishedGame(itemToDelete)
                        findNavController().navigate(action)
                    }
                    .setBackgroundTint(requireContext().getColor(R.color.snackbar_background_color))
                    .setTextColor(requireContext().getColor(R.color.snackbar_text_color))
                    .setActionTextColor(requireContext().getColor(R.color.snackbar_text_color))
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.finishedGameRecyclerView)
    }

    private fun clickFinishedGame() {
        finishedPartnerGameAdapter.clickListener = {
            findNavController().navigate(
                FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToFinishedPartnerGameDetailFragment(
                    it.id
                )
            )
        }
    }

    private fun createPartnerGame() {
        binding.createPartnerGame.setOnClickListener {
            findNavController().navigate(FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToSavePartnerGameFragment())
        }
    }
}