package com.keremkulac.okeyscore.ui.finishedPartnerGameDetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        finishedPartnerGame?.let {
            holder.team1ScoreHint.hint = ("${position+1}. tur ")
            holder.team2ScoreHint.hint = ("${position+1}. tur ")
            holder.team1Score.setText(it.team1!!.allScores!![position])
            holder.team2Score.setText(it.team2!!.allScores!![position])
            holder.team1Score.isFocusable = false
            holder.team2Score.isFocusable = false
            if(position % 2 == 0){
                holder.partnerGameItemBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.item_background_color1))
            }else{
                holder.partnerGameItemBackground.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.item_background_color2))
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
        val partnerGameItemBackground : LinearLayout = itemView.findViewById(R.id.partnerGameItemBackground)

    }

}