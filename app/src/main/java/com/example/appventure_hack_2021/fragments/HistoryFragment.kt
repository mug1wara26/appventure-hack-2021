package com.example.appventure_hack_2021.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.models.HistoryNullable
import com.example.appventure_hack_2021.models.HistoryRecyclerViewAdapter
import com.example.appventure_hack_2021.models.getListener
import com.example.appventure_hack_2021.userRef

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val adapter = HistoryRecyclerViewAdapter(requireContext(), childFragmentManager)

        userRef.child("historyList").addValueEventListener(getListener { snapshot ->
            adapter.histories = snapshot.children.map { it.getValue(HistoryNullable::class.java)!!.toHistory() }
            Log.i("historyRecycler", "dataset set: ${adapter.histories}")
            adapter.notifyDataSetChanged()
        })

        view.findViewById<RecyclerView>(R.id.history_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        return view
    }
}