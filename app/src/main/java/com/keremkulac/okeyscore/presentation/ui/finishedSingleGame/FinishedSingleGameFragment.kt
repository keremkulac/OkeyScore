package com.keremkulac.okeyscore.presentation.ui.finishedSingleGame

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
import com.keremkulac.okeyscore.databinding.FragmentFinishedSingleGameBinding
import com.keremkulac.okeyscore.presentation.ui.finishedGameView.FinishedGameViewFragmentDirections
import com.keremkulac.okeyscore.util.BaseFragment
import com.keremkulac.okeyscore.util.SwipeGesture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FinishedSingleGameFragment : BaseFragment<FragmentFinishedSingleGameBinding>(
    FragmentFinishedSingleGameBinding::inflate) {

    @Inject
    lateinit var finishedSingleGameAdapter: FinishedSingleGameAdapter
    private val viewModel: FinishedSingleGameViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAllFinishedGame()
        createSingleGame()
        observeAllFinishedGame()
        setRecyclerView()
        clickFinishedGame()
        deleteItemDatabase()
        search()
    }

    private fun setShimmer(){
        binding.shimmerLayout.startShimmer()
        lifecycleScope.launch {
            delay(2000)
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }
    }

    private fun setRecyclerView() {
        binding.finishedGameRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame() {
        viewModel.allFinishedSingleGames.observe(viewLifecycleOwner) { finishedSingleList ->
            if (finishedSingleList.isNotEmpty()) {
                setShimmer()
                binding.createSingleGame.visibility = View.GONE
                binding.recordNotFoundImage.visibility = View.GONE
                finishedSingleGameAdapter.finishedSingleGameLists = ArrayList(finishedSingleList)
                binding.finishedGameRecyclerView.adapter = finishedSingleGameAdapter

            } else {
                binding.createSingleGame.visibility = View.VISIBLE
                binding.recordNotFoundImage.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun observeFilteredList() {
        viewModel.filteredList.observe(viewLifecycleOwner) { filteredList ->
            if (filteredList.isNotEmpty()) {
                binding.createSingleGame.visibility = View.GONE
                binding.recordNotFoundImage.visibility = View.GONE
            } else {
                binding.createSingleGame.visibility = View.VISIBLE
                binding.recordNotFoundImage.visibility = View.VISIBLE
            }
        }
    }

    private fun deleteItemDatabase() {
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val itemToDelete = finishedSingleGameAdapter.finishedSingleGameLists[position]
                viewModel.deleteFinishedGame(itemToDelete)
                val action =
                    FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentSelf("single")
                findNavController().navigate(action)
                Snackbar.make(
                    binding.root,
                    requireContext().getString(R.string.deleted),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(requireContext().getString(R.string.take_it_back)) {
                        viewModel.saveSingleGame(itemToDelete)
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


    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText, finishedSingleGameAdapter)
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
        binding.searchView.clearAnimation()
    }

    private fun clickFinishedGame() {
        finishedSingleGameAdapter.clickListener = {
            findNavController().navigate(
                FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToFinishedSingleGameDetailFragment(
                    it.id
                )
            )
        }
    }

    private fun createSingleGame() {
        binding.createSingleGame.setOnClickListener {
            findNavController().navigate(FinishedGameViewFragmentDirections.actionFinishedGameViewFragmentToSaveSingleGameFragment())
        }
    }

}