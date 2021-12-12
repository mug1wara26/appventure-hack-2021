package com.example.appventure_hack_2021.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appventure_hack_2021.BuildConfig
import com.example.appventure_hack_2021.R
import com.example.appventure_hack_2021.models.LocationData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException
import android.graphics.Color
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appventure_hack_2021.models.History
import com.example.appventure_hack_2021.userRef
import com.google.android.gms.maps.model.*

import org.json.JSONArray

import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000
const val DEFAULT_ZOOM = 13.0F
const val countryName = "Singapore"
val countryLatRange = 1.284868..1.464896
val countryLngRange = 103.634678..103.989965

@SuppressLint("MissingPermission")
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var geocoder: Geocoder
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastKnownLocation: Location
    private lateinit var polylineToAdd: Polyline
    lateinit var startLocationData: LocationData
    lateinit var endLocationData: LocationData
    lateinit var searchLocationData: LocationData
    private lateinit var startButton: Button
    private lateinit var distanceTextView: TextView
    private var startTime by Delegates.notNull<Long>()
    private var endTime by Delegates.notNull<Long>()
    private var distance by Delegates.notNull<Int>()
    private var locationPermissionGranted = false
    private var toFocus = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        geocoder = Geocoder(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getDeviceLocation()
        var addressList: List<Address>? = null

        autoCompleteTextView = view.findViewById(R.id.input_search)

        var addressNameList: List<String> = ArrayList()
        autoCompleteTextView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    addressNameList
                )
                autoCompleteTextView.setAdapter(adapter)
                autoCompleteTextView.showDropDown()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                autoCompleteTextView.dismissDropDown()
                // on below line we are getting the
                // location name from search view.
                val location = autoCompleteTextView.text.toString()

                try {
                    // on below line we are getting location from the
                    // location name and adding that location to address list.
                    addressList = geocoder.getFromLocationName(location, 1)
                    if (addressList != null) {
                        addressNameList = (addressList as List<Address>).map { it.getAddressLine(0) }
                    }
                    Log.i("addressList", addressNameList.toString())

                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList!![0]
                        searchLocationData = LocationData(
                            address.featureName,
                            LatLng(address.latitude, address.longitude)
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })

        startButton = view.findViewById(R.id.start_route_button)
        val endButton: Button = view.findViewById(R.id.end_route_button)
        distanceTextView = view.findViewById(R.id.distance_textview)

        val searchButton: ImageButton = view.findViewById(R.id.search_layout_search_button)
        searchButton.setOnClickListener {

            if (!addressList.isNullOrEmpty()) {
                autoCompleteTextView.setText("")
                autoCompleteTextView.dismissDropDown()

                val imm: InputMethodManager? = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(searchButton.windowToken, 0)


                // on below line we are creating a variable for our location
                // where we will add our locations latitude and longitude.

                val locationAddress: Address = Geocoder(requireContext(), Locale.getDefault()).getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)[0]
                startLocationData = LocationData(locationAddress.featureName, LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude))
                endLocationData = searchLocationData
                plotRoute()
            }
        }

        startButton.setOnClickListener {
            startButton.visibility = View.INVISIBLE
            endButton.visibility = View.VISIBLE
            startTime = System.currentTimeMillis()
        }

        endButton.setOnClickListener {
            endTime = System.currentTimeMillis()
            distanceTextView.visibility = View.INVISIBLE
            endButton.visibility = View.INVISIBLE

            val history = History(
                startLocationData,
                endLocationData,
                startTime,
                endTime,
                distance
            )

            userRef.child("historyList").push().setValue(history)
        }

        view.findViewById<FloatingActionButton>(R.id.map_add_favourite_button).setOnClickListener {
            getDeviceLocation()
            val locationAddress: Address = Geocoder(requireContext(), Locale.getDefault()).getFromLocation(lastKnownLocation.latitude, lastKnownLocation.longitude, 1)[0]
            val location = LocationData(locationAddress.featureName, LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude))
            AddFavouriteDialogFragment(location, searchLocationData).show(childFragmentManager, "Add Favourite Dialog")
        }

        mapFragment.getMapAsync(this)

        if (toFocus) {
            toFocus = false
            focusSearchWithContext(requireContext())
        }


        return view
    }

    fun plotRoute() {
        // on below line we are adding marker to that position.
        if (this::marker.isInitialized) marker.remove()
        marker = mMap.addMarker(MarkerOptions().position(endLocationData.pos).title(endLocationData.name))!!

        // below line is to animate camera to that position.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endLocationData.pos, DEFAULT_ZOOM))
        val url = (("https://maps.googleapis.com/maps/api/directions/json?origin="
                + lastKnownLocation.latitude) + "," + lastKnownLocation.longitude.toString() + "&destination="
                + endLocationData.pos.latitude.toString() + "," + endLocationData.pos.longitude.toString() + "&mode=BICYCLING&key=${BuildConfig.MAPS_API_KEY}"
                + "&units=metric")
        Log.i("request url", url)
        val queue = Volley.newRequestQueue(requireContext())

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                Log.i("String Request Response", "Response is: $response")

                val result = JSONObject(response)
                val routes: JSONArray = result.getJSONArray("routes")

                distanceTextView.text = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                    .getJSONObject("distance").getString("text")
                distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0)
                    .getJSONObject("distance").getInt("value")


                val steps = routes.getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps")

                val lines: MutableList<LatLng> = ArrayList()

                for (i in 0 until steps.length()) {
                    val polyline =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    for (p in decodePolyline(polyline)) {
                        lines.add(p)
                    }
                }

                if (this::polylineToAdd.isInitialized) polylineToAdd.remove()
                polylineToAdd = mMap.addPolyline(PolylineOptions().addAll(lines).width(6F).color(Color.BLUE))

                startButton.visibility = View.VISIBLE
            },
            { error -> Log.e("stringRequest error", error.toString()) })

        queue.add(stringRequest)
    }

    /** POLYLINE DECODER - http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java  */
    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true

        getLocationPermission()
        getDeviceLocation()
    }

    fun focusSearch() {
        if (context == null) {
            toFocus = true
        } else {
            focusSearchWithContext(requireContext())
        }
    }

    private fun focusSearchWithContext(context: Context) {
        autoCompleteTextView.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful || task.result != null) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation.latitude,
                                    lastKnownLocation.longitude
                                ), DEFAULT_ZOOM
                            )
                        )

                    } else {
                        Log.d("penis", "Current location is null. Using defaults.")
                        Log.e("fuck google", "Exception: %s", task.exception)
                        mMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(LatLng(33.865143, 151.209900), DEFAULT_ZOOM)
                        )
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    fun checkLocationInCountry(geocoder: Geocoder, pos: LatLng) : LocationData? {
        if (pos.latitude !in countryLatRange || pos.longitude !in countryLngRange) return null

        val addressList = geocoder.getFromLocation(pos.latitude, pos.longitude, 1)

        if (addressList.isNullOrEmpty()) return null

        val address = addressList[0]
        Log.i("map", "address: $address")
        if (address.countryName != countryName) return null

        return LocationData(address.featureName, pos)
    }
}