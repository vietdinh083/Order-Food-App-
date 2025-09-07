package com.example.orderfood.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.orderfood.model.FavoriteModel

@Database(entities = [FavoriteModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorite.db"
                ).allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
        }
    }

}
