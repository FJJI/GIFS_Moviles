package com.fjji.gifs_fjji_can_do.API.model

import android.media.Rating
import androidx.lifecycle.LiveData
import androidx.room.*
import com.bumptech.glide.Glide.init
import com.fjji.gifs_fjji_can_do.API.model.GifDB.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

@Dao
interface GifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: GifDB)

    @Query("SELECT * FROM ${TABLE_NAME}")
    fun getAllGifs(): List<GifDB>

    @Query("SELECT * FROM ${TABLE_NAME} WHERE title LIKE (:userIds)")
    fun getSomeGifs(userIds: String): List<GifDB>

    @Delete
    fun delete(gif: GifDB)


}

@Entity(tableName = TABLE_NAME)
data class GifDB(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,
    @ColumnInfo(name = RATING)
    val rating: String,
    @ColumnInfo(name = TITLE)
    val title: String,
    @ColumnInfo(name = URL)
    val url: String,
    @ColumnInfo(name = LATITUDE)
    val latitude: Double,
    @ColumnInfo(name = LONGITUDE)
    val longitude: Double
) {
    companion object {
        const val TABLE_NAME = "gif"
        const val ID = "id"
        const val URL = "url"
        const val RATING = "rating"
        const val TITLE = "title"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "long"
    }

}
