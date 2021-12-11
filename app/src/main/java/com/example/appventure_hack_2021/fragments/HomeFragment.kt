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
import com.example.appventure_hack_2021.firebaseUser
import com.example.appventure_hack_2021.models.HomeRecyclerViewAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<TextView>(R.id.title).text = getString(
            R.string.home_greeting, firebaseUser.displayName
        )

        val onRoute = true
        view.findViewById<RecyclerView>(R.id.home_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            // decide which cardview is the first elem
            val first: Pair<Int, (HomeRecyclerViewAdapter.ViewHolder) -> Unit> = if (onRoute) {
                Pair(R.layout.cardview_new_destination) {
                    it.view.findViewById<Button>(R.id.new_dest_button).setOnClickListener {
                        val parent = (activity as NavigationActivity)
                        parent.navView.selectedItemId = R.id.nav_map
                        (parent.fragments[R.id.nav_map] as MapFragment).focusSearch()
                    }
                    // it.view.findViewById<Button>(R.id.) {}
                }
            } else {
                Pair(R.layout.cardview_current_pos) {

                }
            }
            adapter = HomeRecyclerViewAdapter(listOf(
                first,
                Pair(R.layout.cardview_history) {
                    it.view.findViewById<Button>(R.id.open_history_button).setOnClickListener {
                        // open history fragment
                        (activity as NavigationActivity).replaceFragment(R.id.nav_history)
                    }
                },
                Pair(R.layout.cardview_daily_route) {

                }
            ))
        }
        return view
    }
}