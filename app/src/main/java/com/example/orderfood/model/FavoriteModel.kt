package com.example.orderfood.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class FavoriteModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name = "userId") val userId : String?,
    @ColumnInfo(name = "description") val description :String?,
    @ColumnInfo(name = "image") val image :String?,
    @ColumnInfo(name = "name") val name :String?,
    @ColumnInfo(name = "price") val price: Int?,
) {
    constructor(
        userId: String?,
        description: String?,
        image: String?,
        name: String?,
        price: Int?
    ) : this(0, userId, description, image, name, price)

}