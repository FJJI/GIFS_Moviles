package com.fjji.gifs_fjji_can_do.API.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.fjji.gifs_fjji_can_do.API.UI.MapActivity.Companion.looking
import com.fjji.gifs_fjji_can_do.API.model.Database
import com.fjji.gifs_fjji_can_do.API.model.GifDB
import com.fjji.gifs_fjji_can_do.API.model.GifDBAdapter
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.database
import com.fjji.gifs_fjji_can_do.R
import kotlinx.android.synthetic.main.activity_favoritos.*

class FavoritosActivity : AppCompatActivity(), GifDBAdapter.OnDeleteClickListener {

    val adapter = GifDBAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)
        database = Room.databaseBuilder(this, Database::class.java,"gif").build().gifDao()
        rv_fav.adapter = adapter
        AsyncTask.execute() {
            adapter.setData(database.getAllGifs())
        }
        rv_fav.layoutManager = LinearLayoutManager(this)
        if (looking != null){
            et_busqueda.text = Editable.Factory.getInstance().newEditable(looking)
            busqueda(et_busqueda)
            looking = null
        }

    }
    fun busqueda(v:View){
        rv_fav.adapter = adapter
        val criteria = "%"+et_busqueda.text+"%"
        AsyncTask.execute() {
            adapter.setData(database.getSomeGifs(criteria))
        }
    }

    override fun onDeleteClicked(item: GifDB, view: View, pos: Int) {
        rv_fav.adapter = adapter
        val eliminate = item
        AsyncTask.execute() {
            database.delete(item)
            adapter.setData(database.getAllGifs())
        }
    }

}