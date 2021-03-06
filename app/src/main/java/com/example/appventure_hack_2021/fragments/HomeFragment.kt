package com.example.appventure_hack_2021.fragments

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.*
import com.example.appventure_hack_2021.models.*
import com.google.android.gms.maps.model.LatLng
import java.time.Duration
import java.time.LocalDate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

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
                Pair(R.layout.cardview_history) { holder ->
                    userRef.child("historyList").getData {
                        val data = it.children.lastOrNull()?.getValue(HistoryNullable::class.java)
                        if (data == null) {
                            holder.view.findViewById<TextView>(R.id.history_latest_view).text =
                                getString(R.string.history_no_latest_text)
                        } else {
                            val lastHistory = data.toHistory()
                            holder.view.findViewById<TextView>(R.id.history_latest_view).text =
                                getString(
                                    R.string.history_latest_text,
                                    lastHistory.totalDistance / 1000,
                                    lastHistory.totalDistance % 1000 / 10,
                                    fromStartToEndString(
                                        lastHistory.startTime.toTime(),
                                        lastHistory.endTime.toTime()
                                    ),
                                    Duration.ofMillis(lastHistory.endTime - lastHistory.startTime)
                                        .toFormattedString()
                                )
                        }
                    }
                    holder.view.findViewById<Button>(R.id.open_history_button).setOnClickListener {
                        // open history fragment
                        (activity as NavigationActivity).navView.selectedItemId = R.id.nav_history
                    }
                },
                Pair(R.layout.cardview_daily_route) {
                    Log.i("home", "calculating daily route")
                    val today = LocalDate.now(offset)
                    val generator = Random(today.dayOfMonth * 301 - today.year xor 46 + today.monthValue)
                    val mapFragment = (activity as NavigationActivity).fragments[R.id.nav_map] as MapFragment
                    var start: LocationData? = null
                    var end: LocationData? = null
                    while (start == null) {
                        start = mapFragment.checkLocationInCountry(
                            Geocoder(context),
                            LatLng(
                                generator.nextDouble(
                                    countryLatRange.start,
                                    countryLatRange.endInclusive
                                ),
                                generator.nextDouble(
                                    countryLngRange.start,
                                    countryLngRange.endInclusive
                                )
                            )
                        )
                    }
                    Log.i("home", "start: $start")
                    while (end == null) {
                        end = mapFragment.checkLocationInCountry(
                            Geocoder(context),
                            LatLng(
                                generator.nextDouble(
                                    countryLatRange.start,
                                    countryLatRange.endInclusive
                                ),
                                generator.nextDouble(
                                    countryLngRange.start,
                                    countryLngRange.endInclusive
                                )
                            )
                        )
                    }
                    Log.i("home", "end: $end")

                    it.view.findViewById<Button>(R.id.start_daily_route).setOnClickListener {
                        mapFragment.startLocationData = start
                        mapFragment.endLocationData = end
                        mapFragment.plotRoute()
                        (activity as NavigationActivity).navView.selectedItemId = R.id.nav_map
                    }
                    Log.i("home", "daily route calculated")
                }
            ))
        }
        return view
    }
}