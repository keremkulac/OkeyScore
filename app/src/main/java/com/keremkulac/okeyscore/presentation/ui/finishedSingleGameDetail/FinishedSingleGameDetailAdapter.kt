package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class FinishedSingleGameDetailAdapter @Inject constructor() : RecyclerView.Adapter<FinishedSingleGameDetailAdapter.ViewHolder>(){

    var finishedSingleGame : FinishedSingleGame? = null
    var numberOfGames = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedSingleGameDetailAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_game_round_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedSingleGameDetailAdapter.ViewHolder, position: Int) {
        finishedSingleGame?.let {
            holder.roundCount.text = holder.itemView.context.getString(R.string.game_detail_round_count).format(position+1)
            holder.player1Score.setText(it.player1!!.allScores!![position])
            holder.player2Score.setText(it.player2!!.allScores!![position])
            holder.player3Score.setText(it.player3!!.allScores!![position])
            holder.player4Score.setText(it.player4!!.allScores!![position])
            it.player1.penalties?.let {list->
                if(list.isNotEmpty()){
                    holder.playerPenalty1.text = createPenaltiesString(list[position],holder.itemView.context)
                }
            }
            it.player2.penalties?.let {list->
                if(list.isNotEmpty()){
                    holder.playerPenalty2.text = createPenaltiesString(list[position],holder.itemView.context)
                }
            }
            it.player3.penalties?.let {list->
                if(list.isNotEmpty()){
                    holder.playerPenalty3.text = createPenaltiesString(list[position],holder.itemView.context)
                }
            }
            it.player4.penalties?.let {list->
                if(list.isNotEmpty()){
                    holder.playerPenalty4.text = createPenaltiesString(list[position],holder.itemView.context)
                }
            }



            for (editText in listOf(holder.player1Score, holder.player2Score, holder.player3Score, holder.player4Score)) {
                editText.isFocusable = false
            }
            if (position == numberOfGames-1){
                holder.divider.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    private fun createPenaltiesString(penalty : String?,context: Context) : String{
        var penaltyText = ""
        if(penalty != "" && penalty != null){
            penaltyText= context.getString(R.string.penalty_text_value).format(penalty.toInt())
        }
        return penaltyText
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundCount : TextView = itemView.findViewById(R.id.roundCount)
        val player1Score : EditText = itemView.findViewById(R.id.player1Score)
        val player2Score : EditText = itemView.findViewById(R.id.player2Score)
        val player3Score : EditText = itemView.findViewById(R.id.player3Score)
        val player4Score : EditText = itemView.findViewById(R.id.player4Score)
        val playerPenalty1 : TextView = itemView.findViewById(R.id.player1Penalty)
        val playerPenalty2 : TextView = itemView.findViewById(R.id.player2Penalty)
        val playerPenalty3 : TextView = itemView.findViewById(R.id.player3Penalty)
        val playerPenalty4 : TextView = itemView.findViewById(R.id.player4Penalty)
        val divider : MaterialDivider = itemView.findViewById(R.id.horizontalDivider)
    }
}