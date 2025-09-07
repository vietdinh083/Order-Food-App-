package com.example.orderfood.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.orderfood.model.FavoriteModel

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite WHERE  userId = :userId")
    fun getFavoriteList(userId :String) : MutableList<FavoriteModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favoriteModel: FavoriteModel)

    // check record đã tồn tại chưa
    @Query("SELECT COUNT(*) FROM favorite WHERE userId = :userId AND name = :name")
    fun exists(userId: String, name: String): Int


    @Query("DELETE FROM favorite WHERE userId = :userId AND name = :name")
    fun deleteFavorite(userId: String, name: String)

}