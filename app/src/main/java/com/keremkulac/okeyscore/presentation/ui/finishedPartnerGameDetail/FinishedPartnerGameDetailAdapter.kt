package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
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
        finishedPartnerGame?.let {
            holder.roundCount.text = holder.itemView.context.getString(R.string.game_detail_round_count).format(position+1)
            holder.team1Score.setText(it.team1!!.allScores!![position])
            holder.team2Score.setText(it.team2!!.allScores!![position])
            it.team1.penalties?.let {list->
                if (list.isNotEmpty()){
                    holder.team1Penalty.text = createPenaltiesString(list[position]!!,holder.itemView.context)
                }
            }
            it.team2.penalties?.let {list->
                if (list.isNotEmpty()){
                    holder.team2Penalty.text = createPenaltiesString(list[position]!!,holder.itemView.context)
                }
            }
            holder.team1Score.isFocusable = false
            holder.team2Score.isFocusable = false
            if (position == numberOfGames-1){
                holder.divider.visibility = View.GONE
            }
        }
    }

    private fun createPenaltiesString(penalty : String,context: Context) : String{
        var penaltyText = ""
        if(penalty != ""){
            penaltyText= context.getString(R.string.penalty_text_value).format(penalty.toInt())
        }
        return penaltyText
    }

    override fun getItemCount(): Int {
        return numberOfGames
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val roundCount : TextView = itemView.findViewById(R.id.roundCount)
        val team1Score : EditText = itemView.findViewById(R.id.team1Score)
        val team2Score : EditText = itemView.findViewById(R.id.team2Score)
        val team1Penalty : TextView = itemView.findViewById(R.id.team1Penalty)
        val team2Penalty : TextView = itemView.findViewById(R.id.team2Penalty)
        val divider : MaterialDivider = itemView.findViewById(R.id.horizontalDivider)
    }

}