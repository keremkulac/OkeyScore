package com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import java.util.regex.Pattern
import javax.inject.Inject

class FinishedPartnerGameAdapter @Inject constructor()  : RecyclerView.Adapter<FinishedPartnerGameAdapter.FinishedGameViewHolder>() {

    var clickListener: ((FinishedPartnerGame) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_partner_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(finishedPartnerGameLists[position])
        holder.itemView.setOnClickListener {
            clickListener?.invoke(finishedPartnerGameLists[position])
        }
    }

    override fun getItemCount(): Int {
        return finishedPartnerGameLists.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<FinishedPartnerGame>(){
        override fun areItemsTheSame(oldItem: FinishedPartnerGame, newItem: FinishedPartnerGame): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FinishedPartnerGame, newItem: FinishedPartnerGame): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var finishedPartnerGameLists : List<FinishedPartnerGame>
        get() = recyclerListDiffer.currentList.toMutableList()
        set(value)  {
            recyclerListDiffer.submitList(value as List<FinishedPartnerGame?>?)
        }

    class FinishedGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gameInfo =  itemView.findViewById<TextView>(R.id.gameInfo)
        private val date =  itemView.findViewById<TextView>(R.id.gameDate)
        fun bind(finishedPartnerGame: FinishedPartnerGame?) {
            finishedPartnerGame?.let {
                val infoItems = finishedPartnerGame.gameInfo.gameInfo.split(" ")
                val pattern = Pattern.compile("Kazanan takım: (.+?)\\. Skor: (\\d+)")
                val matcher = pattern.matcher(finishedPartnerGame.gameInfo.gameInfo)
                if (matcher.find()) {
                    gameInfo.text = itemView.context.getString(R.string.winning_team_info_text).format(matcher.group(1),matcher.group(2))
                }else{
                    gameInfo.text = itemView.context.getString(R.string.winning_team_info_text).format(infoItems[0],infoItems[1])
                }
                date.text = finishedPartnerGame.gameInfo.date
            }
        }
    }

    fun updateData(newList: List<FinishedPartnerGame>) {
        finishedPartnerGameLists = newList
    }
}