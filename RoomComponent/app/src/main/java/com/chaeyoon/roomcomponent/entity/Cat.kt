package com.chaeyoon.roomcomponent.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat")
data class Cat(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "catname") var catName: String,
    @ColumnInfo(name = "lifespan") var lifeSpan: Int,
    @ColumnInfo(name = "origin") var origin: String
)