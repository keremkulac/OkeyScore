package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.databinding.FragmentFinishedPartnerGameBinding
import com.keremkulac.okeyscore.presentation.ui.finishedGameView.FinishedGameViewFragmentDirections
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.GAME_TYPE_PARTNER
import com.keremkulac.okeyscore.util.SwipeGesture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FinishedPartnerGameFragment :
    BaseFragment<FragmentFinishedPartnerGameBinding>(FragmentFinishedPartnerGameBinding::inflate) {

    @Inject
    lateinit var finishedPartnerGameAdapter: FinishedPartnerGameAdapter
    private val viewModel: FinishedPartnerGameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllFinishedGame()
        createPartnerGame()
        observeAllFinishedGame()
        setRecyclerView()
        deleteItemDatabase()
        clickFinishedGame()
        search()
    }

    private fun setShimmer() = with(binding.shimmerLayout) {
        startShimmer()
        lifecycleScope.launch {
            delay(2000)
            stopShimmer()
            visibility = View.GONE
        }
    }

    private fun setRecyclerView() = with(binding.finishedGameRecyclerView) {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        setHasFixedSize(false)
    }

    private fun observeAllFinishedGame() = with(binding) {
        viewModel.finishedPartnerGame.observe(viewLifecycleOwner) { finishedList ->
            if (finishedList.isNotEmpty()) {
                setShimmer()
                createPartnerGame.visibility = View.GONE
                recordNotFoundImage.visibility = View.GONE
                finishedPartnerGameAdapter.finishedPartnerGameLists = ArrayList(finishedList)
                finishedGameRecyclerView.adapter = finishedPartnerGameAdapter
            } else {
                createPartnerGame.visibility = View.VISIBLE
                recordNotFoundImage.visibility = View.VISIBLE
                shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun observeFilteredList() = with(binding) {
        viewModel.filteredList.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList.isNotEmpty()) {
                createPartnerGame.visibility = View.GONE
                recordNotFoundImage.visibility = View.GONE
            } else {
                createPartnerGame.visibility = View.VISIBLE
                recordNotFoundImage.visibility = View.VISIBLE
            }
        }
    }

    private fun search() = with(binding.searchView) {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        setOnCloseListener {
            observeAllFinishedGame()
            false
        }
    }


    private fun deleteItemDatabase() = with(binding) {
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val itemToDelete = finishedPartnerGameAdapter.finishedPartnerGameLists[position]
                viewModel.deleteFinishedGame(itemToDelete)
                val action =
                    FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentSelf(
                        GAME_TYPE_PARTNER
                    )
                findNavController().navigate(action)
                Snackbar.make(
                    root,
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
        itemTouchHelper.attachToRecyclerView(finishedGameRecyclerView)
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