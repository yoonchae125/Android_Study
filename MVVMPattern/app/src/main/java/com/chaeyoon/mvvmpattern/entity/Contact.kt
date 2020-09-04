package com.chaeyoon.mvvmpattern.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true) var id:Int?,
    var name: String,
    var number: String,
    var initial: String
)