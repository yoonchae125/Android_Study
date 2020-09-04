package com.chaeyoon.mvvmpattern.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chaeyoon.mvvmpattern.dao.ContactDao
import com.chaeyoon.mvvmpattern.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactDB : RoomDatabase(){
    abstract fun getContactDao():ContactDao

    companion object{
        private var contactDB:ContactDB? = null

        fun getInstance(context: Context):ContactDB?{
            if (contactDB == null) {
                synchronized(ContactDB::class) {
                    contactDB = Room.databaseBuilder(context.applicationContext,
                        ContactDB::class.java, "contact")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return contactDB
        }

    }
}