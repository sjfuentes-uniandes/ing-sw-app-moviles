package com.example.vinilos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vinilos.models.Album
import com.example.vinilos.models.Collector
import com.example.vinilos.models.Artist

@Database(entities = [Album::class, Collector::class, Artist::class], version = 2, exportSchema = false)
abstract class VinilosRoomDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumsDao
    abstract fun collectorsDao(): CollectorsDao
    abstract fun artistsDao(): ArtistsDao

    companion object {
        @Volatile
        private var INSTANCE: VinilosRoomDatabase? = null

        fun getDatabase(context: Context): VinilosRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinilosRoomDatabase::class.java,
                    "vinilos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}