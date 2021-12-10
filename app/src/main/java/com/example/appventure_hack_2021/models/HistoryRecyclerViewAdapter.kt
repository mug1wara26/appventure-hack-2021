package com.example.appventure_hack_2021.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HistoryRecyclerViewAdapter(private val histories: List<History>, private val context: Context) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>()  {
    class ViewHolder(private val view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        fun bind(history: History) {
            view.findViewById<TextView>(R.id.header).text = context.getString(
                R.string.history_start_stop_text, history.startName, history.startName
            )
            val offset = ZoneOffset.ofHours(context.resources.getInteger(R.integer.tz_offset))
            val now = LocalDateTime.now(offset)
            val startTime = LocalDateTime.ofEpochSecond(history.startTime * 1000, 0, offset)
            val endTime = LocalDateTime.ofEpochSecond(history.endTime * 1000, 0, offset)
            val firstFormatter = DateTimeFormatter.ofPattern(if (now.year == startTime.year) { "d MMM" } else { "d MMM yyyy" })
            val secondFormatter = DateTimeFormatter.ofPattern("h:mm aa")
            val additional = if (startTime.dayOfMonth == endTime.dayOfMonth) { "" } else { "of the next day" }
            view.findViewById<TextView>(R.id.content).text = context.getString(
                R.string.history_content_text,
                history.totalDistance,
                startTime.format(firstFormatter),
                startTime.format(secondFormatter),
                endTime.format(secondFormatter) + additional
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.cardview_history_fragment, parent, false),
        context
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    override fun getItemCount(): Int = histories.size
}