package com.example.appventure_hack_2021.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.userRef

class AddFavouriteRecyclerViewAdapter(
    private vararg val locations: LocationData
): RecyclerView.Adapter<AddFavouriteRecyclerViewAdapter.ViewHolder>() {
    init {
        if (locations.isEmpty()) throw IllegalArgumentException("what even is the point")
    }
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(location: LocationData) {
            view.findViewById<TextView>(R.id.add_favourite_location_textview).text = location.name
            view.findViewById<Button>(R.id.add_favourite_location_button).setOnClickListener {
                userRef.child("settings/favourites").push().setValue(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_add_favourite_location, parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size
}