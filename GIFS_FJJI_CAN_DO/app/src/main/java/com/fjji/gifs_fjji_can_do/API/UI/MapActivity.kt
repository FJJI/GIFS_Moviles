package com.fjji.gifs_fjji_can_do.API.UI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.fjji.gifs_fjji_can_do.API.model.GifDB
import com.fjji.gifs_fjji_can_do.API.model.GifDao
import com.fjji.gifs_fjji_can_do.R
import com.fjji.gifs_fjji_can_do.API.model.Database
import com.fjji.gifs_fjji_can_do.utils.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import androidx.lifecycle.Observer

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var list: List<GifDB>
    private lateinit var database: GifDao
    private lateinit var locationData: LocationUtil
    var coordinates = mutableListOf<LatLng>()

    companion object {
        var looking : String? = null
        var LOCATION_PERMISSION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        database = Room.databaseBuilder(this, Database::class.java,"gif").build().gifDao()
        AsyncTask.execute() {
            list = database.getAllGifs()
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        locationData = LocationUtil(this)
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            // Add a marker in Santiago and move the camera
            val santiago = LatLng(-33.40414, -70.50858)

            onMapReady(mMap)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        for (item: GifDB in list) {
            val place = LatLng(item.latitude, item.longitude)
            mMap.addMarker(MarkerOptions().position(place).title(item.title))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
        }
        when {
            isPermissionsGranted() -> locationData.observe(this, Observer {
                println("${it.latitude} , ${it.longitude}, speed: ${it.speed}")


                for (item: GifDB in list) {
                    val place = LatLng(item.latitude, item.longitude)
                    mMap.addMarker(MarkerOptions().position(place).title(item.title))
                }

                mMap.setOnMarkerClickListener { marker ->
                    looking = marker.title
                    startActivity(Intent(this, FavoritosActivity::class.java))
                    true
                }

            })

            shouldShowRequestPermissionRationale() -> println("Ask Permission")

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION
            )
        }
        mMap.setOnMarkerClickListener { marker ->
            looking = marker.title
            startActivity(Intent(this, FavoritosActivity::class.java))
            true
        }


    }
    private fun invokeLocationAction() {
        when {
            isPermissionsGranted() -> locationData.observe(this, Observer {
                println("${it.latitude} , ${it.longitude}, speed: ${it.speed}")

                val polylineOptions = PolylineOptions().clickable(false).color(R.color.colorAccent).geodesic(true)
                    .width(10f)

                for (item: GifDB in list) {
                    val place = LatLng(item.latitude, item.longitude)
                    mMap.addMarker(MarkerOptions().position(place).title(item.title))
                }
                coordinates.add(LatLng(it.latitude,it.longitude))
                polylineOptions.addAll(coordinates)
                mMap.addPolyline(polylineOptions)
                mMap.setOnMarkerClickListener { marker ->
                    looking = marker.title
                    startActivity(Intent(this, FavoritosActivity::class.java))
                    true
                }

            })

            shouldShowRequestPermissionRationale() -> println("Ask Permission")

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION
            )
        }
        mMap.setOnMarkerClickListener { marker ->
            looking = marker.title
            startActivity(Intent(this, FavoritosActivity::class.java))
            true
        }
    }
    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )


}