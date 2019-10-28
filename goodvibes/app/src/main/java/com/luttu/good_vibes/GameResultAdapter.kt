package com.luttu.good_vibes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


class GameResultAdapter(private val gameList: List<Game>)
    : RecyclerView.Adapter<GameResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GameResultViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: GameResultViewHolder, position: Int) {
        val game: Game = gameList[position]
        holder.bind(game)
    }

    override fun getItemCount(): Int = gameList.size

}