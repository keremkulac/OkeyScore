package com.keremkulac.okeyscore.ui.finishedGame

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
import com.keremkulac.okeyscore.util.SwipeGesture
import com.keremkulac.okeyscore.databinding.FragmentFinishedGameBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FinishedGameFragment @Inject constructor(
    private val finishedGameAdapter: FinishedGameAdapter
)  : Fragment(R.layout.fragment_finished_game)  {


    private val viewModel by viewModels<FinishedGameViewModel>()
    private lateinit var binding : FragmentFinishedGameBinding
    private var fabVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFinishedGameBinding.bind(view)
        observeAllFinishedGame()
        setRecyclerView()
        goToSaveGameFragment()
        searchClick()
        deleteItemDatabase()
        clickFinishedGame()
    }

    private fun setRecyclerView(){
        binding.finishedGameRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.finishedGameRecyclerView.setHasFixedSize(false)
    }

    private fun observeAllFinishedGame(){
        viewModel.finishedGame.observe(viewLifecycleOwner){finishedList->
            if(finishedList.isNotEmpty()){
                binding.recordNotFound.visibility = View.GONE
                finishedGameAdapter.finishedList = ArrayList(finishedList)
                binding.finishedGameRecyclerView.adapter =  finishedGameAdapter

            }else{
                binding.recordNotFound.visibility = View.VISIBLE
            }
        }
    }
    private fun searchClick(){
        binding.searchView.setOnSearchClickListener {
            binding.gameHistoryText.visibility = View.GONE
            search()
        }
        binding.searchView.setOnCloseListener {
            binding.gameHistoryText.visibility = View.VISIBLE
            false
        }
    }

    private fun search(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText,finishedGameAdapter)
                return true
            }
        })
    }


    private fun goToSaveGameFragment(){
        fabVisible = false
        binding.fab.setOnClickListener {
            //findNavController().navigate(R.id.action_finishedGameFragment_to_saveGameFragment)
            if(!fabVisible){
                binding.fabSingle.show()
                binding.fabPartner.show()
                binding.fabPartnerText.visibility = View.VISIBLE
                binding.fabSingleText.visibility = View.VISIBLE
                binding.fabSingle.visibility = View.VISIBLE
                binding.fabPartner.visibility = View.VISIBLE
                binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_close))
                fabVisible = true
            }else{
                binding.fabSingle.hide()
                binding.fabPartner.hide()
                binding.fabPartnerText.visibility = View.GONE
                binding.fabSingleText.visibility = View.GONE
                binding.fabSingle.visibility = View.GONE
                binding.fabPartner.visibility = View.GONE
                binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_add))

                fabVisible = false
            }

        }

    }

    private fun deleteItemDatabase(){
        val swipeGesture = object  : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val itemToDelete = finishedGameAdapter.finishedList[position]
                viewModel.deleteFinishedGame(itemToDelete)
                findNavController().navigate(R.id.finishedGameFragment)
                Snackbar.make(binding.finishedGameRecyclerView,"Silindi",Snackbar.LENGTH_LONG).setAction(
                    "Geri al",View.OnClickListener {
                        viewModel.saveFinishedGame(itemToDelete!!)
                        findNavController().navigate(R.id.finishedGameFragment)
                    }
                ).show()

            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.finishedGameRecyclerView)
    }

    private fun clickFinishedGame(){
        finishedGameAdapter.clickListener={
            val action = FinishedGameFragmentDirections.actionFinishedGameFragmentToFinishedGameDetailFragment(it.id)
            findNavController().navigate(action)
        }
    }
}