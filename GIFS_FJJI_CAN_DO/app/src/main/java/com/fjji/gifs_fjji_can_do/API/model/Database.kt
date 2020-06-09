package com.fjji.gifs_fjji_can_do.API.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GifDB::class],version = 1)
abstract class Database: RoomDatabase(){
    abstract fun gifDao(): GifDao
}