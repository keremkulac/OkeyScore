package com.keremkulac.okeyscore.ui.finishedGameDetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class FinishedDetailGameAdapter @Inject constructor(): RecyclerView.Adapter<FinishedDetailGameAdapter.ViewHolder>() {

    var finishedPartnerGame : FinishedPartnerGame? = null
    var numberOfGames =0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.round_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.roundCount.setText("${position+1}. tur ")
        finishedPartnerGame?.let {
            if(it.team1!!.allScores!![position].equals("") && it.team2!!.allScores!![position].equals("")){

            }else{
                holder.team1ScoreEditText.setText(it.team1!!.allScores!![position])
                holder.team2ScoreEditText.setText(it.team2!!.allScores!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundCount: TextView = itemView.findViewById(R.id.roundCount)
        val team1ScoreEditText: EditText = itemView.findViewById(R.id.team1Score)
        val team2ScoreEditText: EditText = itemView.findViewById(R.id.team2Score)
    }

}