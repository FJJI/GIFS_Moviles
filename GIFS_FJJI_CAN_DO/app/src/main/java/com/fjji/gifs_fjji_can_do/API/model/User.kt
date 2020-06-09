package com.fjji.gifs_fjji_can_do.API.model

import java.io.Serializable

data class RandGif(val data: Gif): Serializable // It haves to be different than ManyGifs because here it doesnt have the []
data class Gif(val id: String, val rating: String, val title: String?, val images: ImageType) : Serializable
data class ManyGifs(val data: List<Gif>): Serializable

data class GifSubcategory(val name: String): Serializable
data class GifCategories(val name: String, val subcategories: List<GifSubcategory>): Serializable
data class ListGifCategories(val data: List<GifCategories>): Serializable
data class ImageType(val original: Image): Serializable
data class Image(val url: String): Serializable













