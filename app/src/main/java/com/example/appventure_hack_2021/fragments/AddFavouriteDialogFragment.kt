package com.example.appventure_hack_2021.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.models.AddFavouriteRecyclerViewAdapter
import com.example.appventure_hack_2021.models.LocationData

class AddFavouriteDialogFragment(
    private vararg val locations: LocationData
    ) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            val view = layoutInflater.inflate(R.layout.dialog_add_favourite, null)
            view.findViewById<RecyclerView>(R.id.add_favourites_recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = AddFavouriteRecyclerViewAdapter(*locations)
            }
            AlertDialog.Builder(it).apply {
                setView(view)
                setNeutralButton(R.string.done) { _, _ -> }
            }.create()
        }
    }
}