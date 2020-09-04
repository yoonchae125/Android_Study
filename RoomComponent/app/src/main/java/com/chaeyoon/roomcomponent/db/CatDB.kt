package com.chaeyoon.roomcomponent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chaeyoon.roomcomponent.dao.CatDao
import com.chaeyoon.roomcomponent.entity.Cat

@Database(entities = [Cat::class], version = 1)
abstract class CatDB :RoomDatabase(){
    abstract fun catDao():CatDao

    companion object {
        private var INSTANCE: CatDB? = null

        fun getInstance(context: Context): CatDB? {
            if (INSTANCE == null) {
                synchronized(CatDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        CatDB::class.java, "cat.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}