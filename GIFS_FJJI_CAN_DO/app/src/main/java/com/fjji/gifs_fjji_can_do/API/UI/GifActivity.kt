package com.fjji.gifs_fjji_can_do.API.UI

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjji.gifs_fjji_can_do.API.model.Gif
import com.fjji.gifs_fjji_can_do.API.model.GifDB
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.GIPHY
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.GIPHYRAN
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.database
import com.fjji.gifs_fjji_can_do.GifAdapter
import com.fjji.gifs_fjji_can_do.R
import com.fjji.gifs_fjji_can_do.utils.LocationUtil
import kotlinx.android.synthetic.main.activity_gif.*
import kotlinx.android.synthetic.main.gif_cell.*
import kotlinx.android.synthetic.main.gif_cell.view.*
import java.lang.Exception

class GifActivity : AppCompatActivity(),
    GifAdapter.OnGifItemClickListener {

    var gifsData = ArrayList<Gif>()
    var gifLen: Int = 0
    var gifList : List<Gif>? = null
    var gifAdapter: GifAdapter? =null

    private lateinit var locationData: LocationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)
        locationData = LocationUtil(this)
        try{
            gifsData = intent.getSerializableExtra(GIPHY) as ArrayList<Gif>
        }catch (e:Exception){
            gifsData = intent.getSerializableExtra(GIPHYRAN) as ArrayList<Gif>
        }
        println(gifsData)
        refreshGifs()
        rv_Gifs.layoutManager = LinearLayoutManager(this)
    }
    fun refreshGifs(){
        gifLen = gifsData.size
        gifList = gifsData
        gifAdapter =
            GifAdapter((gifList as List<Gif>)!!, this)
        rv_Gifs.adapter = gifAdapter
    }

    override fun onGifItemClicked(item: Gif, view: View, pos: Int) {
        //TODO("Not yet implemented")
        println("Im the IMAGE")
    }

    override fun onGifStarItemClicked(item: Gif, view: View, pos: Int) {
        println("Im the star")
        var longitude:Double = 0.0
        var latitude:Double = 0.0
        when {isPermissionsGranted() -> locationData.observe(this, Observer {
            println("${it.latitude} , ${it.longitude}")
            longitude = it.longitude
            latitude = it.latitude
            })
        }
        val gif = GifDB(item.id, item.rating, item.title!!, item.images.original.url, longitude, latitude) //Mejor que forzar un optional pero aum asi hay mejores formas
        view.iv_star_gif_cell.setImageResource(R.drawable.full_star_50)
        AsyncTask.execute{
            database.insert(gif)
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

    companion object {
        var LOCATION_PERMISSION = 100
    }
}
