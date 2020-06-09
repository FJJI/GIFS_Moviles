package com.fjji.gifs_fjji_can_do.API.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjji.gifs_fjji_can_do.R
import kotlinx.android.synthetic.main.fav_cell.view.*

class GifDBAdapter( val delClickListener: OnDeleteClickListener) : RecyclerView.Adapter<GifDBAdapter.ViewHolder>() {

    private var data = listOf<GifDB>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindItem(item,delClickListener)
    }

    fun setData(newData: List<GifDB>) {
        this.data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val view = mView
        val gif_image = mView.iv_fav_gif
        val name  = mView.tv_fav_name
        val delete = mView.iv_fav_delete
        fun bindItem(gif: GifDB, clickListener: OnDeleteClickListener) {
            name.text = gif.title
            Glide.with(view.context).load(gif.url).thumbnail(0.1f).into(gif_image)
            delete.setOnClickListener{
                clickListener.onDeleteClicked(gif, view, adapterPosition)
                }
            }
        }

    interface OnDeleteClickListener{
        fun onDeleteClicked(item: GifDB, view: View, pos: Int)
    }
}