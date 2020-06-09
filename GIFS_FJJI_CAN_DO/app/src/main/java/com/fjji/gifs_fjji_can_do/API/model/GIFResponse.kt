package com.fjji.gifs_fjji_can_do.API.model

import com.google.gson.annotations.SerializedName

data class GIFResponse(val data: GIFObject, val meta: MetaObject)

data class GIFObject(val id: String,
                     @SerializedName(value="imageUrl", alternate= ["image_url"])
                     val imageUrl: String,
                     var rating: String,
                     val title: String?,
                     val images: ImageType)

data class MetaObject( val status: Int)
