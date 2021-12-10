package com.example.appventure_hack_2021.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.NavigationActivity
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.model.DatabaseAccessor
import com.example.appventure_hack_2021.model.HomeRecyclerViewAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // view.findViewById<TextView>(R.id.title).text = getString(
        //    R.string.home_greeting, DatabaseAccessor.instance.user.displayName
        // )
        val onRoute = true
        view.findViewById<RecyclerView>(R.id.home_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            // decide which cardview is the first elem
            val first: Pair<Int, (HomeRecyclerViewAdapter.ViewHolder) -> Unit> = if (onRoute) {
                Pair(R.layout.new_destination_cardview) {
                    it.view.findViewById<Button>(R.id.new_dest_button).setOnClickListener {
                        (activity as NavigationActivity).navView.selectedItemId = R.id.nav_map
                    }
                    // it.view.findViewById<Button>(R.id.) {}
                }
            } else {
                Pair(R.layout.current_pos_cardview) {

                }
            }
            adapter = HomeRecyclerViewAdapter(listOf(
                first,
                Pair(R.layout.history_cardview) {

                }
            ))
        }
        return view
    }
}