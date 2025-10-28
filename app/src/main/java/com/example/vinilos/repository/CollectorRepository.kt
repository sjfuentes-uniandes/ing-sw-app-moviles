package com.example.vinilos.repository

import androidx.lifecycle.LiveData
import com.example.vinilos.database.CollectorsDao
import com.example.vinilos.models.Collector
import com.example.vinilos.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CollectorRepository(
    private val collectorsDao: CollectorsDao,
    private val networkService: NetworkServiceAdapter
) {
    
    fun getCollectors(): LiveData<List<Collector>> = collectorsDao.getCollectors()
    
    suspend fun refreshCollectors(): Result<Unit> {
        return try {
            val collectors = suspendCoroutine<List<Collector>> { continuation ->
                networkService.getCollectors(
                    onComplete = { collectors ->
                        continuation.resume(collectors)
                    },
                    onError = { error ->
                        continuation.resumeWithException(error)
                    }
                )
            }
            withContext(Dispatchers.IO) {
                collectorsDao.deleteAll()
                collectorsDao.insertAll(collectors)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}