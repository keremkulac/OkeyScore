package com.keremkulac.okeyscore.ui.finishedSingleGameDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
            holder.player1ScoreHint.hint = ("${position+1}. tur ")
            holder.player2ScoreHint.hint = ("${position+1}. tur ")
            holder.player3ScoreHint.hint = ("${position+1}. tur ")
            holder.player4ScoreHint.hint = ("${position+1}. tur ")
            holder.player1Score.setText(it.player1!!.allScores!![position])
            holder.player2Score.setText(it.player2!!.allScores!![position])
            holder.player3Score.setText(it.player3!!.allScores!![position])
            holder.player4Score.setText(it.player4!!.allScores!![position])

        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
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

    }
}