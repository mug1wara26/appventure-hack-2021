package com.example.appventure_hack_2021.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.fragments.AddFavouriteDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.Duration

private const val stats_id = 0
class HistoryRecyclerViewAdapter(
    private val context: Context,
    private val manager: FragmentManager
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>()  {
    var histories: List<History> = listOf()

    class ViewHolder(private val view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        fun bindStats(histories: List<History>) {
            val totalDistance = histories.sumOf { it.totalDistance }
            view.findViewById<TextView>(R.id.content).text = context.getString(
                R.string.history_stats_text,
                histories.size,
                totalDistance / 1000,
                totalDistance % 1000 / 10,
                Duration.ofMillis(histories.sumOf { it.endTime - it.startTime }).toFormattedString()
            )
            return
        }

        fun bind(history: History, manager: FragmentManager) {
            view.findViewById<FloatingActionButton>(R.id.history_add_favourite_button).setOnClickListener {
                AddFavouriteDialogFragment(history.start, history.end).show(manager, "Add Favourite Dialog")
            }

            view.findViewById<TextView>(R.id.header).text = context.getString(
                R.string.history_start_stop_text, history.start.name, history.end.name
            )

            view.findViewById<TextView>(R.id.content).text = context.getString(
                R.string.history_content_text,
                history.totalDistance / 1000,
                history.totalDistance % 1000 / 10,
                fromStartToEndString(history.startTime.toTime(), history.endTime.toTime()),
                Duration.ofSeconds(history.endTime - history.startTime).toFormattedString()
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
        holder.bind(histories[position - 1], manager)
    }

    override fun getItemCount(): Int = histories.size + 1

    override fun getItemViewType(position: Int): Int = if (position == 0) stats_id else 1
}