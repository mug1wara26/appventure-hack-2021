package com.example.appventure_hack_2021.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R

class HistoryRecyclerViewAdapter(private val history: List<History>) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>()  {
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind() {
            // TODO: write bind for history
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_history_fragment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = history.size
}