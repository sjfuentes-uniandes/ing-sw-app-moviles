package com.example.vinilos.repository

import androidx.lifecycle.LiveData
import com.example.vinilos.database.ArtistsDao
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ArtistsRepository(
    private val artistsDao: ArtistsDao,
    private val networkService: NetworkServiceAdapter
) {

    fun getArtists() = artistsDao.getArtists()


    suspend fun refreshArtists(): Result<Unit>{
        return try{
            val artists = suspendCoroutine<List<Artist>> {continuation ->
                networkService.getArtists(
                    onComplete = { artists ->
                        continuation.resume(artists)
                    },
                    onError = { error ->
                        continuation.resumeWithException(error)
                    }
                )

            }
            withContext(Dispatchers.IO){
                artistsDao.deleteAll()
                artistsDao.insertAll(artists)
            }
            Result.success(Unit)

        } catch (e: Exception){
            Result.failure(e)
        }

    }

}