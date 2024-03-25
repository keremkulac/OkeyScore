package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class FinishedPartnerGameDetailAdapter @Inject constructor(): RecyclerView.Adapter<FinishedPartnerGameDetailAdapter.ViewHolder>() {

    var finishedPartnerGame : FinishedPartnerGame? = null
    var numberOfGames =0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.partner_game_round_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        finishedPartnerGame?.let { finishedPartnerGame ->
            holder.team1ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.team2ScoreHint.hint = holder.itemView.context.getString(R.string.hint_round_cont).format(position+1)
            holder.team1Score.setText(finishedPartnerGame.team1!!.allScores!![position])
            holder.team2Score.setText(finishedPartnerGame.team2!!.allScores!![position])
            holder.team1Score.isFocusable = false
            holder.team2Score.isFocusable = false
            if(position % 2 == 0){
                holder.team1ScoreHint.setBackgroundColor(holder.team1Score.context.getColor(R.color.line_color_dark))
                holder.team2ScoreHint.setBackgroundColor(holder.team2Score.context.getColor(R.color.line_color_dark))
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1ScoreHint : TextInputLayout = itemView.findViewById(R.id.team1ScoreHint)
        val team2ScoreHint : TextInputLayout = itemView.findViewById(R.id.team2ScoreHint)
        val team1Score : EditText = itemView.findViewById(R.id.team1Score)
        val team2Score : EditText = itemView.findViewById(R.id.team2Score)
    }

}