package com.keremkulac.okeyscore.ui.finishedSingleGame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.okeyscore.R
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject


class FinishedSingleGameAdapter @Inject constructor()  : RecyclerView.Adapter<FinishedSingleGameAdapter.FinishedGameViewHolder>() {

    var clickListener: ((FinishedSingleGame) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finished_game_single_item, parent, false)
        return FinishedGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FinishedGameViewHolder, position: Int) {
        holder.bind(finishedSingleGameLists[position],holder.itemView.context,findMinScore(position))
        holder.itemView.setOnClickListener {
            clickListener?.invoke(finishedSingleGameLists[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return finishedSingleGameLists.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<FinishedSingleGame?>(){
        override fun areItemsTheSame(oldItem: FinishedSingleGame, newItem: FinishedSingleGame): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FinishedSingleGame, newItem: FinishedSingleGame): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)
    var finishedSingleGameLists : ArrayList<FinishedSingleGame?>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<FinishedSingleGame?>
        set(value)  {
            recyclerListDiffer.submitList(value)
            notifyDataSetChanged()
        }

    class FinishedGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val player1Name =  itemView.findViewById<TextView>(R.id.player1Name)
        private val player2Name =  itemView.findViewById<TextView>(R.id.player2Name)
        private val player3Name =  itemView.findViewById<TextView>(R.id.player3Name)
        private val player4Name =  itemView.findViewById<TextView>(R.id.player4Name)
        private val player1TotalScore =  itemView.findViewById<TextView>(R.id.player1TotalScore)
        private val player2TotalScore =  itemView.findViewById<TextView>(R.id.player2TotalScore)
        private val player3TotalScore =  itemView.findViewById<TextView>(R.id.player3TotalScore)
        private val player4TotalScore =  itemView.findViewById<TextView>(R.id.player4TotalScore)
        private val gameInfo =  itemView.findViewById<TextView>(R.id.gameInfo)
        private val date =  itemView.findViewById<TextView>(R.id.gameDate)

        fun bind(finishedSingleGame: FinishedSingleGame?,context: Context,min : Int) {
            finishedSingleGame?.let {
                player1Name.text = finishedSingleGame.player1!!.name
                player2Name.text = finishedSingleGame.player2!!.name
                player3Name.text = finishedSingleGame.player3!!.name
                player4Name.text = finishedSingleGame.player4!!.name
                player1TotalScore.text = finishedSingleGame.player1.totalScore
                player2TotalScore.text = finishedSingleGame.player2.totalScore
                player3TotalScore.text = finishedSingleGame.player3.totalScore
                player4TotalScore.text = finishedSingleGame.player4.totalScore
                val scores = listOf(player1TotalScore, player2TotalScore, player3TotalScore, player4TotalScore)

                scores.forEach{
                    if(it.text.toString().toInt() == min   ){
                        it.setTextColor(ContextCompat.getColor(context,R.color.min_score_color))
                        it.isFocusable = false
                    }else{
                        it.setTextColor(ContextCompat.getColor(context,R.color.max_score_color))
                        it.isFocusable = false
                    }
                }
                gameInfo.text = finishedSingleGame.gameInfo.gameInfo
                date.text = finishedSingleGame.gameInfo.date
            }
        }
    }

   private fun findMinScore(position: Int) : Int{
        val list = listOf(
            finishedSingleGameLists[position]!!.player1!!.totalScore.toInt(),
            finishedSingleGameLists[position]!!.player2!!.totalScore.toInt(),
            finishedSingleGameLists[position]!!.player3!!.totalScore.toInt(),
            finishedSingleGameLists[position]!!.player4!!.totalScore.toInt()
        )

        return  list.min()
    }

    fun updateData(newList: ArrayList<FinishedSingleGame?>) {
        finishedSingleGameLists = newList
        notifyDataSetChanged()
    }

}