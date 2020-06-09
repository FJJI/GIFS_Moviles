package com.fjji.gifs_fjji_can_do.API.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.database
import com.fjji.gifs_fjji_can_do.API.model.Database
import com.fjji.gifs_fjji_can_do.API.model.Gif
import com.fjji.gifs_fjji_can_do.API.model.GifDB
import com.fjji.gifs_fjji_can_do.API.model.GifDBAdapter
import com.fjji.gifs_fjji_can_do.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var list: List<GifDB>? = null


    companion object {
        var looking : String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        database = Room.databaseBuilder(this, Database::class.java,"gif").build().gifDao()
        list = database.getAllGifs()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


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
        /*for (item: GifDB in list!!) {
            val place = LatLng(item.latitude!!, item.long!!)
            mMap.addMarker(MarkerOptions().position(place).title(item.title))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
        }*/

        // Click on the Markers
        mMap.setOnMarkerClickListener { marker ->
            looking = marker.title
            startActivity(Intent(this, FavoritosActivity::class.java))
            finish()
            true
        }

    }
}