package com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
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
            holder.player1ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.player2ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.player3ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.player4ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.player1Score.setText(it.player1!!.allScores!![position])
            holder.player2Score.setText(it.player2!!.allScores!![position])
            holder.player3Score.setText(it.player3!!.allScores!![position])
            holder.player4Score.setText(it.player4!!.allScores!![position])
            holder.playerPenalty1.text = createPenaltiesString(it.player1.penalties!![position]!!,holder.itemView.context)
            holder.playerPenalty2.text = createPenaltiesString(it.player2.penalties!![position]!!,holder.itemView.context)
            holder.playerPenalty3.text = createPenaltiesString(it.player3.penalties!![position]!!,holder.itemView.context)
            holder.playerPenalty4.text = createPenaltiesString(it.player4.penalties!![position]!!,holder.itemView.context)
            if(position % 2 == 0){
                holder.player1ScoreHint.setBackgroundColor(holder.player1Score.context.getColor(R.color.line_color_dark))
                holder.player2ScoreHint.setBackgroundColor(holder.player2Score.context.getColor(R.color.line_color_dark))
                holder.player3ScoreHint.setBackgroundColor(holder.player3Score.context.getColor(R.color.line_color_dark))
                holder.player4ScoreHint.setBackgroundColor(holder.player4Score.context.getColor(R.color.line_color_dark))
            }
            for (editText in listOf(holder.player1Score, holder.player2Score, holder.player3Score, holder.player4Score)) {
                editText.isFocusable = false
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    private fun createPenaltiesString(penalty : String,context: Context) : String{
        var penaltyText = ""
        if(penalty != ""){
            penaltyText= context.getString(R.string.penalty_text_value).format(penalty.toInt())
        }
        return penaltyText
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val player1ScoreHint : TextInputLayout = itemView.findViewById(R.id.player1ScoreHint)
        val player2ScoreHint : TextInputLayout = itemView.findViewById(R.id.player2ScoreHint)
        val player3ScoreHint : TextInputLayout = itemView.findViewById(R.id.player3ScoreHint)
        val player4ScoreHint : TextInputLayout = itemView.findViewById(R.id.player4ScoreHint)
        val player1Score : EditText = itemView.findViewById(R.id.player1Score)
        val player2Score : EditText = itemView.findViewById(R.id.player2Score)
        val player3Score : EditText = itemView.findViewById(R.id.player3Score)
        val player4Score : EditText = itemView.findViewById(R.id.player4Score)
        val playerPenalty1 : TextView = itemView.findViewById(R.id.player1Penalty)
        val playerPenalty2 : TextView = itemView.findViewById(R.id.player2Penalty)
        val playerPenalty3 : TextView = itemView.findViewById(R.id.player3Penalty)
        val playerPenalty4 : TextView = itemView.findViewById(R.id.player4Penalty)
    }
}