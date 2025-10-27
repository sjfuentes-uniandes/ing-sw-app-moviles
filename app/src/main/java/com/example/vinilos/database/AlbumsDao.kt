package com.example.vinilos.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vinilos.models.Album

@Dao
interface AlbumsDao {
    @Query("SELECT * FROM albums_table ORDER BY albumId ASC")
    fun getAlbums(): LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(albums: List<Album>)

    @Query("DELETE FROM albums_table")
    fun deleteAll()
}