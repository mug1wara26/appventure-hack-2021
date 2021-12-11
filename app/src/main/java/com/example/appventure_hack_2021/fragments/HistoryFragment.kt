package com.example.appventure_hack_2021.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.NavigationActivity
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.models.HistoryRecyclerViewAdapter
import com.example.appventure_hack_2021.user

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            // switch fragment back to home
            (activity as NavigationActivity).replaceFragment(R.id.nav_home)
        }

        view.findViewById<RecyclerView>(R.id.history_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryRecyclerViewAdapter(user.historyList, context, childFragmentManager)
        }

        return view
    }
}