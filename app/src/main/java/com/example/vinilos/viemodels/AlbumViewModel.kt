package com.example.vinilos.viemodels

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.repository.AlbumRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class AlbumViewModel(application: Application): AndroidViewModel(application) {
    private val database = VinilosRoomDatabase.getDatabase(application)
    private val repository = AlbumRepository(
        database.albumsDao(), 
        NetworkServiceAdapter.getInstance(application)
    )
    
    val albums: LiveData<List<Album>> = repository.getAlbums()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    private var _createAlbumResult = MutableLiveData<Result<Album>>()

    val createAlbumResult: LiveData<Result<Album>>
        get() = _createAlbumResult

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork(){
        viewModelScope.launch {
            repository.refreshAlbums()
                .onSuccess {
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                }
                .onFailure {
                    _eventNetworkError.postValue(true)
                }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun refreshAlbums() {
        refreshDataFromNetwork()
    }

    fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String,
        tracks: List<Map<String, String>>
    ) {
        viewModelScope.launch {
            val result = repository.createAlbum(
                name = name,
                cover = cover,
                releaseDate = releaseDate,
                description = description,
                genre = genre,
                recordLabel = recordLabel,
                tracks = tracks
            )
            _createAlbumResult.postValue(result)
            if (result.isSuccess) {
                // Refresh albums list after successful creation
                refreshDataFromNetwork()
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
