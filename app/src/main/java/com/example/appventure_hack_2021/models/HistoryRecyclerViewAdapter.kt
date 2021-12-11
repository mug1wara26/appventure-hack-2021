package com.example.appventure_hack_2021.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val stats_id = 0
class HistoryRecyclerViewAdapter(private val histories: List<History>, private val context: Context) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>()  {
    class ViewHolder(private val view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        fun bindStats(histories: List<History>) {
            val timeSpent = histories.sumOf { it.startTime - it.endTime }.toTime().toHoursMinutesSecondsString()
            view.findViewById<TextView>(R.id.content).text = context.getString(
                R.string.history_stats_text,
                histories.size,
                histories.sumOf { it.totalDistance.toDouble() },
                timeSpent
            )
            return
        }
        fun bind(history: History) {
            view.findViewById<TextView>(R.id.header).text = context.getString(
                R.string.history_start_stop_text, history.startName, history.startName
            )

            view.findViewById<TextView>(R.id.content).text = context.getString(
                R.string.history_content_text,
                history.totalDistance,
                fromStartToEndString(history.startTime.toTime(), history.endTime.toTime()),
                (history.startTime - history.endTime).toTime().toHoursMinutesSecondsString()
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val id = if (viewType == 0) R.layout.cardview_history_stats else R.layout.cardview_history_fragment
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(id, parent, false),
            context
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == stats_id) {
            holder.bindStats(histories)
            return
        }
        holder.bind(histories[position - 1])
    }

    override fun getItemCount(): Int = histories.size + 1

    override fun getItemViewType(position: Int): Int = if (position == 0) stats_id else 1
}