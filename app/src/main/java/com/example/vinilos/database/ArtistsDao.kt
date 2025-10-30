package com.example.vinilos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinilos.models.Artist

@Dao
interface ArtistsDao {

    @Query("SELECT * FROM artist_table ORDER BY artistId ASC")
    fun getArtists(): LiveData<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(artists: List<Artist>)

    fun insertOne(artist: Artist) {
        insertAll(listOf(artist))
    }

    @Query("DELETE FROM artist_table")
    fun deleteAll()

}