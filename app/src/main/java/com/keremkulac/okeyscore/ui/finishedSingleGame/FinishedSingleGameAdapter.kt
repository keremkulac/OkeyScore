package com.keremkulac.okeyscore.ui.finishedSingleGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedSingleGame


class FinishedSingleGameAdapter : RecyclerView.Adapter<FinishedSingleGameAdapter.FinishedGameViewHolder>() {

    var clickListener: ((FinishedSingleGame) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_single_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(finishedSingleGameLists[position])
        holder.itemView.setOnClickListener {
            clickListener?.invoke(finishedSingleGameLists[position])
        }
    }

    override fun getItemCount(): Int {
        return finishedSingleGameLists.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<FinishedSingleGame>(){
        override fun areItemsTheSame(oldItem: FinishedSingleGame, newItem: FinishedSingleGame): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FinishedSingleGame, newItem: FinishedSingleGame): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var finishedSingleGameLists : ArrayList<FinishedSingleGame>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<FinishedSingleGame>
        set(value)  {
            recyclerListDiffer.submitList(value as List<FinishedSingleGame?>?)
        }

    class FinishedGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameInfo =  itemView.findViewById<TextView>(R.id.gameInfo)
        private val date =  itemView.findViewById<TextView>(R.id.gameDate)

        fun bind(finishedSingleGame: FinishedSingleGame) {
              gameInfo.text = finishedSingleGame.gameInfo.gameInfo
                date.text = finishedSingleGame.gameInfo.date
        }
    }

    fun updateData(newList: ArrayList<FinishedSingleGame>) {
        finishedSingleGameLists = newList
    }

}