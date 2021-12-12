package com.example.appventure_hack_2021.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.models.HistoryRecyclerViewAdapter
import com.example.appventure_hack_2021.refreshUser
import com.example.appventure_hack_2021.user

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.history_recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryRecyclerViewAdapter(user.historyList, context, childFragmentManager)
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        refreshUser()
        val adapter = recyclerView.adapter as HistoryRecyclerViewAdapter
        adapter.histories = user.historyList
        adapter.notifyDataSetChanged()
    }
}