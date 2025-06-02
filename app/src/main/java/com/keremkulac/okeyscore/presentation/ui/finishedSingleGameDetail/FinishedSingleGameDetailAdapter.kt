package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class FinishedSingleGameDetailAdapter @Inject constructor() : RecyclerView.Adapter<FinishedSingleGameDetailAdapter.ViewHolder>(){

    var finishedSingleGame : FinishedSingleGame? = null
    var clickListener: ((LinearLayout, ImageView) -> Unit)? = null
    var numberOfGames = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedSingleGameDetailAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedSingleGameDetailAdapter.ViewHolder, position: Int) {
        finishedSingleGame?.let {
            holder.player1Name.text = it.player1?.name ?: ""
            holder.player2Name.text = it.player2?.name ?: ""
            holder.player3Name.text = it.player3?.name ?: ""
            holder.player4Name.text = it.player4?.name ?: ""
            holder.player1Score.text = it.player1?.allScores?.getOrNull(position)?.toString() ?: "0"
            holder.player2Score.text = it.player2?.allScores?.getOrNull(position)?.toString() ?: "0"
            holder.player3Score.text = it.player3?.allScores?.getOrNull(position)?.toString() ?: "0"
            holder.player4Score.text = it.player4?.allScores?.getOrNull(position)?.toString() ?: "0"
            holder.player1Penalty.text = it.player1?.penalties?.getOrNull(position)?.toString() ?: "0"
            holder.player2Penalty.text = it.player2?.penalties?.getOrNull(position)?.toString() ?: "0"
            holder.player3Penalty.text = it.player3?.penalties?.getOrNull(position)?.toString() ?: "0"
            holder.player4Penalty.text = it.player4?.penalties?.getOrNull(position)?.toString() ?: "0"
            holder.roundLayout.setOnClickListener {
                clickListener?.invoke(holder.scoreContainer,holder.icon)
            }
            holder.scoreContainer.visibility = View.GONE
            holder.roundCount.text = holder.itemView.context.getString(R.string.round_count).format(position+1)
        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val player1Name: TextView = itemView.findViewById(R.id.player1Name)
        val player2Name: TextView = itemView.findViewById(R.id.player2Name)
        val player3Name: TextView = itemView.findViewById(R.id.player3Name)
        val player4Name: TextView = itemView.findViewById(R.id.player4Name)
        val player1Score: TextView = itemView.findViewById(R.id.player1Score)
        val player2Score: TextView = itemView.findViewById(R.id.player2Score)
        val player3Score: TextView = itemView.findViewById(R.id.player3Score)
        val player4Score: TextView = itemView.findViewById(R.id.player4Score)
        val player1Penalty: TextView = itemView.findViewById(R.id.player1Penalty)
        val player2Penalty: TextView = itemView.findViewById(R.id.player2Penalty)
        val player3Penalty: TextView = itemView.findViewById(R.id.player3Penalty)
        val player4Penalty: TextView = itemView.findViewById(R.id.player4Penalty)
        val roundLayout : ConstraintLayout = itemView.findViewById(R.id.roundLayout)
        val scoreContainer : LinearLayout = itemView.findViewById(R.id.scoreContainer)
        val icon : ImageView = itemView.findViewById(R.id.icon)
        val roundCount : TextView = itemView.findViewById(R.id.roundCount)
    }
}