package com.keremkulac.okeyscore.ui.finishedGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.Finished

class FinishedGameAdapter(private val itemList: List<Finished?>) : RecyclerView.Adapter<FinishedGameAdapter.FinishedGameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class FinishedGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1Name =  itemView.findViewById<TextView>(R.id.team1Name)
        val team2Name =  itemView.findViewById<TextView>(R.id.team2Name)
        val team1TotalScore =  itemView.findViewById<TextView>(R.id.team1TotalScore)
        val team2TotalScore =  itemView.findViewById<TextView>(R.id.team2TotalScore)
        val date =  itemView.findViewById<TextView>(R.id.gameDate)
        fun bind(item: Finished?) {
            item?.let {
                team1Name.text = item.team1Name
                team2Name.text = item.team2Name
                team1TotalScore.text = item.team1TotalScore
                team2TotalScore.text = item.team2TotalScore
                date.text = item.date
            }



        }
    }
}