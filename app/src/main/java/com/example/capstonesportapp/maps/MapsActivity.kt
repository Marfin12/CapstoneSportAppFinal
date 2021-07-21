package com.example.capstonesportapp.maps

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import com.example.capstonesportapp.R
import com.example.capstonesportapp.core.domain.model.Team
import com.example.capstonesportapp.databinding.ActivityMapsBinding
import com.example.capstonesportapp.detail.DetailActivity
import com.google.gson.Gson
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions


class MapsActivity : AppCompatActivity() {

    companion object {
        @Volatile
        var teamData: List<Team>? = null
        private const val ICON_ID = "ICON_ID"
    }

    private lateinit var mapboxMap: MapboxMap

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.map_box_access_token))

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Team Location"
        val text: Spannable = SpannableString(supportActionBar!!.title)
        text.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            text.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        supportActionBar!!.title = text

        teamData?.map {
            println(it)
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            showMarker()
        }
    }

    private fun showMarker() {
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            style.addImage(
                ICON_ID, BitmapFactory.decodeResource(
                    resources,
                    R.drawable.mapbox_marker_icon_default
                )
            )
            val latLngBoundsBuilder = LatLngBounds.Builder()

            val symbolManager = SymbolManager(binding.mapView, mapboxMap, style)
            symbolManager.iconAllowOverlap = true

            val options = ArrayList<SymbolOptions>()
            val sameLocation = ArrayList<String>()
            var grip = 0.0
            teamData?.forEach { data ->
                val geoCoder = Geocoder(this)
                val location = if (data.location.isNotEmpty()) data.location else data.country
                var latitude = geoCoder.getFromLocationName(location, 5)[0].latitude
                var longitude = geoCoder.getFromLocationName(location, 5)[0].longitude

                if (sameLocation.contains(location)) {
                    grip += 0.1
                    latitude += grip
                    longitude += grip
                } else {
                    sameLocation.add(location)
                }

                latLngBoundsBuilder.include(LatLng(latitude, longitude))
                options.add(
                    SymbolOptions()
                        .withLatLng(LatLng(latitude, longitude))
                        .withIconImage(ICON_ID)
                        .withData(Gson().toJsonTree(data))
                )
            }
            symbolManager.create(options)

            val latLngBounds = latLngBoundsBuilder.build()
            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 5000)

            symbolManager.addClickListener { symbol ->
                val data = Gson().fromJson(symbol.data, Team::class.java)
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.TEAM_DATA, data)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}