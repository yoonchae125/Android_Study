package com.chaeyoon.roomcomponent.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.chaeyoon.roomcomponent.entity.Cat

interface CatDao {
    @Query("SELECT * FROM cat")
    fun getAll():List<Cat>
    /* import android.arch.persistence.room.OnConflictStrategy.REPLACE */
    @Insert(onConflict = REPLACE)
    fun insert(cat: Cat)

    @Query("DELETE FROM cat")
    fun deleteAll()
}