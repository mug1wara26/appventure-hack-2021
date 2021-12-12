package com.example.appventure_hack_2021.models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.getDataFromRTDB
import com.example.appventure_hack_2021.userRef
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.CountDownLatch
import kotlin.properties.Delegates


class AddFavouriteRecyclerViewAdapter private constructor(
    private val context: Context,
    private val locations: Array<out LocationData>,
    private val favourites: List<LocationData>
): RecyclerView.Adapter<AddFavouriteRecyclerViewAdapter.ViewHolder>() {
    init {
        if (locations.isEmpty()) throw IllegalArgumentException("what even is the point")
    }

    class ViewHolder(private val context: Context, private val view: View) : RecyclerView.ViewHolder(view) {
        var isFavourite by Delegates.notNull<Boolean>()
        private lateinit var button: Button

        private fun setButtonText() {
            button.text =
                context.getString(if (isFavourite) R.string.remove_favourite_text else R.string.add_favourite_text)
        }

        fun bind(location: LocationData) {
            Log.i("AddFavourite", "location name: ${location.name}")
            view.findViewById<TextView>(R.id.add_favourite_location_textview).text = location.name
            button = view.findViewById(R.id.add_favourite_location_button)
            setButtonText()
            button.setOnClickListener {
                if (isFavourite) {
                    favoriteRef.child(location.name).setValue(location.pos)
                } else {
                    favoriteRef.child(location.name).removeValue()
                }
                isFavourite = !isFavourite
                setButtonText()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context,
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_add_favourite_location, parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.isFavourite = locations[position] in favourites
        holder.bind(locations[position])
    }

    override fun getItemCount(): Int = locations.size

    companion object {
        val favoriteRef = userRef.child("settings/favourites")
        fun new(context: Context, vararg locations: LocationData): AddFavouriteRecyclerViewAdapter {
            val latch = CountDownLatch(1)
            var favourites: List<LocationData>? = null
            getDataFromRTDB(favoriteRef) { snapshot ->
                Log.i("AddFavouritesAdapter", "snapshot: $snapshot")
                if (snapshot.value == null) {
                    // no favourites added yet
                    favourites = listOf()
                    latch.countDown()
                    return@getDataFromRTDB
                }

                favourites = snapshot.children.map { LocationData(it.key!!, it.getValue(LatLng::class.java)!!) }
                Log.i("AddFavouritesAdapter", "favourites set")
                latch.countDown()
            }
            Log.i("AddFavouritesAdapter", "starting block")
            latch.await()
            Log.i("AddFavouritesAdapter", "block ending")
            return AddFavouriteRecyclerViewAdapter(context, locations, favourites!!)
        }
    }
}