package com.example.appventure_hack_2021.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.appventure_hack_2021.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.Marker


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        var addressList: List<Address>? = null

        val autoCompleteTextView: AutoCompleteTextView = view.findViewById(R.id.input_search)

        val addressNameArrayList = ArrayList<String>()
        autoCompleteTextView.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    addressNameArrayList
                )
                autoCompleteTextView.setAdapter(adapter)

                autoCompleteTextView.showDropDown()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                autoCompleteTextView.dismissDropDown()
                // on below line we are getting the
                // location name from search view.
                val location = autoCompleteTextView.text.toString()

                val geocoder = Geocoder(context)
                try {
                    // on below line we are getting location from the
                    // location name and adding that location to address list.
                    addressList = geocoder.getFromLocationName(location, 1)

                    addressNameArrayList.removeAll(addressNameArrayList)
                    (addressList as MutableList<Address>?)?.forEach {
                        addressNameArrayList.add(it.getAddressLine(0))
                    }

                    Log.i("addressList", addressNameArrayList.toString())

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        })

        val searchButton: ImageButton = view.findViewById(R.id.search_layout_search_button)
        searchButton.setOnClickListener {
            if (!addressList.isNullOrEmpty()) {
                val address = addressList!![0]
                // on below line we are creating a variable for our location
                // where we will add our locations latitude and longitude.
                val latLng = LatLng(address.latitude, address.longitude)

                // on below line we are adding marker to that position.
                marker.remove()
                marker = mMap.addMarker(MarkerOptions().position(latLng).title(address.featureName))!!

                // below line is to animate camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            }
        }

        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        marker = mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"))!!
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}