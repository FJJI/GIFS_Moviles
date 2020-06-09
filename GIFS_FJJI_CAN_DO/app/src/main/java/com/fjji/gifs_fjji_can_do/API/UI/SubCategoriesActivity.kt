package com.fjji.gifs_fjji_can_do.API.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjji.gifs_fjji_can_do.API.config.API_KEY
import com.fjji.gifs_fjji_can_do.API.model.GifSubcategory
import com.fjji.gifs_fjji_can_do.API.model.ManyGifs
import com.fjji.gifs_fjji_can_do.API.networking.ApiGifQuery
import com.fjji.gifs_fjji_can_do.API.networking.DBService
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.SUBCAT
import com.fjji.gifs_fjji_can_do.OnSubCategoryItemClickListener
import com.fjji.gifs_fjji_can_do.R
import com.fjji.gifs_fjji_can_do.SubCategoriesAdapter
import kotlinx.android.synthetic.main.activity_sub_categories.*
import kotlinx.android.synthetic.main.sub_categories_cell.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class SubCategoriesActivity : AppCompatActivity(),
    OnSubCategoryItemClickListener {

    var subsList: List<GifSubcategory>? = null
    var subcategoriesData = ArrayList<GifSubcategory>()
    var subcateoriesLen: Int = 0
    var subcategoriesAdapter : SubCategoriesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_categories)
        subcategoriesData = intent.getSerializableExtra(SUBCAT)  as ArrayList<GifSubcategory>
        println(subcategoriesData)
        println(subcategoriesData!!.count())
        refreshSubCategory()
        rv_subcategories.layoutManager = LinearLayoutManager(this)
    }

    fun refreshSubCategory(){
        subcateoriesLen = subcategoriesData.size
        subsList = subcategoriesData
        subcategoriesAdapter = SubCategoriesAdapter(
            (subsList as List<GifSubcategory>),
            this
        )
        rv_subcategories.adapter = subcategoriesAdapter
    }

    override fun onSubCategoryItemClicked(item: GifSubcategory, view: View, pos: Int) {
        val request = DBService.buildService(ApiGifQuery::class.java)
        val call = request.getGif(
            API_KEY,
            item.name,
            10,
            listOf("G","PG"))
        call.enqueue(object : Callback<ManyGifs> {
            override fun onResponse(call: Call<ManyGifs>, response: Response<ManyGifs>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(view.context, GifActivity::class.java)
                        intent.putExtra(MenuActivity.GIPHY, response.body()!!.data as Serializable)
                        startActivityForResult(intent, 2)
                    }
                }

                else{
                    Toast.makeText(view.context, "Error al Procesar la Solicitud", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ManyGifs>, t: Throwable) {
                Toast.makeText(view.context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
