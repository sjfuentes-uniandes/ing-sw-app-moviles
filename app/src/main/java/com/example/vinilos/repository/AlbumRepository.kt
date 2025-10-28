package com.example.vinilos.repository

import androidx.lifecycle.LiveData
import com.example.vinilos.database.AlbumsDao
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AlbumRepository(
    private val albumsDao: AlbumsDao,
    private val networkService: NetworkServiceAdapter
) {
    
    fun getAlbums(): LiveData<List<Album>> = albumsDao.getAlbums()
    
    suspend fun refreshAlbums(): Result<Unit> {
        return try {
            val albums = suspendCoroutine<List<Album>> { continuation ->
                networkService.getAlbums(
                    onComplete = { albums ->
                        continuation.resume(albums)
                    },
                    onError = { error ->
                        continuation.resumeWithException(error)
                    }
                )
            }
            withContext(Dispatchers.IO) {
                albumsDao.deleteAll()
                albumsDao.insertAll(albums)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}