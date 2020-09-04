package com.chaeyoon.mvvmpattern.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chaeyoon.mvvmpattern.entity.Contact

@Dao
interface ContactDao {
    @Query("Select * from contact")
    fun getAll():LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)
}