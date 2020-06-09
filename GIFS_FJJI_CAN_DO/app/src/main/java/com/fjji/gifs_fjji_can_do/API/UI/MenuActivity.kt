package com.fjji.gifs_fjji_can_do.API.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.fjji.gifs_fjji_can_do.*
import com.fjji.gifs_fjji_can_do.API.UI.LoginActivity.Companion.activeUser
import com.fjji.gifs_fjji_can_do.API.config.*
import com.fjji.gifs_fjji_can_do.API.model.*
import com.fjji.gifs_fjji_can_do.API.networking.*
import com.fjji.gifs_fjji_can_do.R
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class MenuActivity : AppCompatActivity(), OnCategoryItemClickListener {

    companion object{
        val SUBCAT = "AAA"
        val GIPHY = "GIFS"
        val GIPHYRAN = "GIFSRAN"
        lateinit var database: GifDao
    }

    // The categories list to get the
    var categoriesData = ArrayList<GifCategories>()

    // Categories Values
    var categoriesLen: Int = 0
    var categoriesList : List<GifCategories>? = null
    var categoriesAdapter : CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val tv_username = findViewById<View>(R.id.tv_username) as TextView
        tv_username.text = activeUser

        sign_out_menu.setOnClickListener{
            tv_username.text = ""
            finish()
        }
        //GIFS
        // we get de database
        database = Room.databaseBuilder(this, Database::class.java, "gif").build().gifDao()
        // We call the Categories section
        val request = DBService.buildService(Categories::class.java)
        val call = request.ApiCategories(API_KEY)
        call.enqueue(object : Callback<ListGifCategories> {
            override fun onResponse(call: Call<ListGifCategories>, response: Response<ListGifCategories>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        categoriesData = response.body()!!.data as ArrayList<GifCategories>
                        println(categoriesData)
                        refreshCategories()

                    }
                }
                else{
                    Toast.makeText(this@MenuActivity, "Error al Procesar la Solicitud", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ListGifCategories>, t: Throwable) {
                Toast.makeText(this@MenuActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        rv_categories.layoutManager = LinearLayoutManager(this)
    }

    fun refreshCategories(){
        // Es llamado cuando cargo las categorias
        categoriesLen = categoriesData.size
        categoriesList = categoriesData
        categoriesAdapter = CategoriesAdapter((categoriesList as ArrayList<GifCategories>)!!, this)
        rv_categories.adapter = categoriesAdapter

    }

    override fun onCategoryItemClicked(item: GifCategories, view: View, pos: Int) {
        val intent = Intent(view.context, SubCategoriesActivity::class.java)
        val info = item.subcategories as ArrayList<GifSubcategory>
        intent.putExtra(SUBCAT, info as Serializable)
        startActivityForResult(intent, 1)
    }

    fun onFavorite(view: View){
        val intent = Intent(this, FavoritosActivity::class.java)
        startActivityForResult(intent, 9)
    }

    fun imageButtonGetImages(v:View){
        val search = ev_search.text.toString()
        if (search == "") {
            val request = DBService.buildService(ApiGifQuery::class.java)
            val call = request.getRandomGif(API_KEY, listOf("G", "PG"))
            call.enqueue(object : Callback<RandGif> {
                override fun onResponse(call: Call<RandGif>, response: Response<RandGif>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val intent = Intent(this@MenuActivity, GifActivity::class.java)
                            intent.putExtra(GIPHYRAN, listOf(response.body()!!.data)  as Serializable)
                            println("RAND")
                            startActivityForResult(intent, 4)
                        }
                    }
                    else{
                        Toast.makeText(this@MenuActivity, "Error al Procesar la Solicitud", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RandGif>, t: Throwable) {
                    Toast.makeText(this@MenuActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        else{
            val request = DBService.buildService(ApiGifQuery::class.java)
            val call = request.getGif(API_KEY,
                search,
                10,
                listOf("G","PG"))
            call.enqueue(object : Callback<ManyGifs> {
                override fun onResponse(call: Call<ManyGifs>, response: Response<ManyGifs>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val intent = Intent(this@MenuActivity, GifActivity::class.java)
                            intent.putExtra(GIPHY, response.body()!!.data as Serializable)
                            println("ASDASDASDASD")
                            startActivityForResult(intent, 2)
                        }
                    }

                    else{
                        Toast.makeText(this@MenuActivity, "Error al Procesar la Solicitud", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ManyGifs>, t: Throwable) {
                    Toast.makeText(this@MenuActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    fun onMapClick(v:View){
        //val intent = Intent(this@MenuActivity, GifActivity::class.java)
        //intent.putExtra(GIPHYRAN, listOf(response.body()!!.data)  as Serializable)
        //println("RAND")
        //startActivityForResult(intent, 4)
        startActivity(Intent(this, MapActivity::class.java))
    }
}