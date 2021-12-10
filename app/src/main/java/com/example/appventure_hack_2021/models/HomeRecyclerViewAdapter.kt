package com.example.appventure_hack_2021.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomeRecyclerViewAdapter(private val views: List<Pair<Int, (ViewHolder) -> Unit>>) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int = views[position].first

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        views[position].second(holder)
    }

    override fun getItemCount(): Int = views.size
}