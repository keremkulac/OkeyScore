package com.keremkulac.okeyscore.ui.finishedPartnerGame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class FinishedPartnerGameAdapter @Inject constructor()  : RecyclerView.Adapter<FinishedPartnerGameAdapter.FinishedGameViewHolder>() {

    var clickListener: ((FinishedPartnerGame) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_partner_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(finishedPartnerGameLists[position],holder.itemView.context)
        holder.itemView.setOnClickListener {
            clickListener?.invoke(finishedPartnerGameLists[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return finishedPartnerGameLists.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<FinishedPartnerGame?>(){
        override fun areItemsTheSame(oldItem: FinishedPartnerGame, newItem: FinishedPartnerGame): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: FinishedPartnerGame, newItem: FinishedPartnerGame): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var finishedPartnerGameLists : ArrayList<FinishedPartnerGame?>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<FinishedPartnerGame?>
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

        fun bind(finishedPartnerGame: FinishedPartnerGame?,context: Context) {
            finishedPartnerGame?.let {
                team1Name.text = finishedPartnerGame.team1!!.name
                team2Name.text = finishedPartnerGame.team2!!.name
                team1TotalScore.text = context.getString(R.string.total_score_text, finishedPartnerGame.team1.totalScore)
                team2TotalScore.text = context.getString(R.string.total_score_text, finishedPartnerGame.team2.totalScore)
                gameInfo.text = finishedPartnerGame.gameInfo.gameInfo
                date.text = finishedPartnerGame.gameInfo.date
            }
        }
    }

    fun updateData(newList: ArrayList<FinishedPartnerGame?>) {
        finishedPartnerGameLists = newList
        notifyDataSetChanged()
    }
}