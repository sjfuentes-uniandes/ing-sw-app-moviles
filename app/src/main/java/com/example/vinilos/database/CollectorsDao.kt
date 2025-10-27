package com.example.vinilos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vinilos.models.Collector

@Dao
interface CollectorsDao {
    @Query("SELECT * FROM collectors_table ORDER BY collectorId ASC")
    fun getCollectors(): LiveData<List<Collector>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(collectors: List<Collector>)

    @Query("DELETE FROM collectors_table")
    fun deleteAll()
}