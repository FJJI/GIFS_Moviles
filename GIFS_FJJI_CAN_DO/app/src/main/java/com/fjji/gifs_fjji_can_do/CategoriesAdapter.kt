package com.fjji.gifs_fjji_can_do

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjji.gifs_fjji_can_do.API.model.Gif
import com.fjji.gifs_fjji_can_do.API.model.GifCategories
import com.fjji.gifs_fjji_can_do.API.model.GifDB
import com.fjji.gifs_fjji_can_do.API.model.GifSubcategory
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity.Companion.database
import kotlinx.android.synthetic.main.categories_cell.view.*
import kotlinx.android.synthetic.main.gif_cell.view.*
import kotlinx.android.synthetic.main.sub_categories_cell.view.*

class CategoriesAdapter(private val listOfList: ArrayList<GifCategories>, val categoryClickListener: OnCategoryItemClickListener): RecyclerView.Adapter<CategoriesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.categories_cell, parent, false)
        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listOfList.count()
    }

    fun getList(pos: Int) :GifCategories{
        return listOfList[pos]
    }

    fun deleteList(pos: Int){
        listOfList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun undoDeletion(pos: Int, listItem: GifCategories){
        listOfList.add(pos,listItem)
        notifyItemInserted(pos)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listOfList[position]
        holder.bindHistoric(currentItem, categoryClickListener)
    }

    class ListViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private var item: GifCategories? = null
        private val name: TextView  = v.tv_Categorias

        fun bindHistoric(listItem: GifCategories,  clickListener:OnCategoryItemClickListener) {
            this.item = listItem
            name.setText(item!!.name)
            itemView.setOnClickListener{
                clickListener.onCategoryItemClicked(listItem,view, adapterPosition)
            }
        }
    }
}

interface OnCategoryItemClickListener{
    fun onCategoryItemClicked(item:GifCategories, view: View, pos: Int)
}

class SubCategoriesAdapter(private val listOfList: List<GifSubcategory>, val subcategoryClickListener: OnSubCategoryItemClickListener): RecyclerView.Adapter<SubCategoriesAdapter.SubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.sub_categories_cell, parent, false)
        return SubViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listOfList.size
    }

    fun getList(pos: Int) :GifSubcategory{
        return listOfList[pos]
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        val currentItem = listOfList[position]
        holder.bindHistoric(currentItem, subcategoryClickListener)
    }

    class SubViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private var item: GifSubcategory? = null
        private val tv = v.tv_subcat

        fun bindHistoric(listItem: GifSubcategory, clickListener:OnSubCategoryItemClickListener) {
            this.item = listItem
            tv.text = listItem.name
            itemView.setOnClickListener{
                clickListener.onSubCategoryItemClicked(listItem,view, adapterPosition)
            }
        }
    }
}

interface OnSubCategoryItemClickListener{
    fun onSubCategoryItemClicked(item:GifSubcategory, view: View, pos: Int)
}

class GifAdapter(private val listOfList: List<Gif>, val gifClickListener: OnGifItemClickListener): RecyclerView.Adapter<GifAdapter.GifViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.gif_cell, parent, false)
        return GifViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listOfList.size
    }

    fun getList(pos: Int) :Gif{
        return listOfList[pos]
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val currentItem = listOfList[position]
        holder.bindHistoric(currentItem, gifClickListener)
    }

    class GifViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private var item: Gif? = null
        private val tv = v.tv_name_gif_cell
        private val star = v.iv_star_gif_cell
        private val image = v.iv_gif_gif_cell

        fun bindHistoric(listItem: Gif, clickListener:OnGifItemClickListener) {
            var DBGif: GifDB? = null
            this.item = listItem
            tv.text = listItem.title
            // star ver tema para tenerla apagada --> SQL
            // mientras, los meto aca

            Glide.with(view.context).load(item!!.images.original.url).thumbnail(0.1f).into(image)

            itemView.setOnClickListener{
                clickListener.onGifItemClicked(listItem,view, adapterPosition)
            }
            star.setOnClickListener{
                clickListener.onGifStarItemClicked(listItem, view, adapterPosition)

            }
        }
    }

    interface OnGifItemClickListener{
        fun onGifItemClicked(item:Gif, view: View, pos: Int)
        fun onGifStarItemClicked(item:Gif, view: View, pos: Int)
    }
}
