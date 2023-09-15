package com.keremkulac.okeyscore.ui.finishedGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.Finished
import javax.inject.Inject

class FinishedGameAdapter @Inject constructor()  : RecyclerView.Adapter<FinishedGameAdapter.FinishedGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(finishedList[position])
    }

    override fun getItemCount(): Int {
        return finishedList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Finished?>(){
        override fun areItemsTheSame(oldItem: Finished, newItem: Finished): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: Finished, newItem: Finished): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var finishedList : ArrayList<Finished?>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<Finished?>
        set(value)  {
            recyclerListDiffer.submitList(value)
            notifyDataSetChanged()
        }

    class FinishedGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val team1Name =  itemView.findViewById<TextView>(R.id.team1Name)
        private val team2Name =  itemView.findViewById<TextView>(R.id.team2Name)
        private val team1TotalScore =  itemView.findViewById<TextView>(R.id.team1TotalScore)
        private val team2TotalScore =  itemView.findViewById<TextView>(R.id.team2TotalScore)
        private val gameInfo =  itemView.findViewById<TextView>(R.id.gameInfo)
        private val date =  itemView.findViewById<TextView>(R.id.gameDate)

        fun bind(finished: Finished?) {
            finished?.let {
                team1Name.text = finished.team1Name
                team2Name.text = finished.team2Name
                team1TotalScore.text = finished.team1TotalScore
                team2TotalScore.text = finished.team2TotalScore
                gameInfo.text = finished.gameInfo
                date.text = finished.date
            }
        }
    }


    fun updateData(newList: ArrayList<Finished?>) {
        finishedList = newList
        notifyDataSetChanged()
    }
}