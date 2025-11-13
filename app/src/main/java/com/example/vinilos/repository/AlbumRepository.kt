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

    suspend fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String,
        tracks: List<Map<String, String>>
    ): Result<Album> {
        return try {
            val album = suspendCoroutine<Album> { continuation ->
                networkService.createAlbum(
                    name = name,
                    cover = cover,
                    releaseDate = releaseDate,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel,
                    tracks = tracks,
                    onComplete = { album ->
                        continuation.resume(album)
                    },
                    onError = { error ->
                        continuation.resumeWithException(error)
                    }
                )
            }
            withContext(Dispatchers.IO) {
                albumsDao.insertAll(listOf(album))
            }
            Result.success(album)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}