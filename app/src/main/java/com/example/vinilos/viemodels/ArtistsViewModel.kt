package com.example.vinilos.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = VinilosRoomDatabase.getDatabase(application)

    private val repository = ArtistsRepository(
        database.artistsDao(),
        NetworkServiceAdapter.getInstance(application)
    )

    val artist: LiveData<List<Artist>> = repository.getArtists()

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

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
            repository.refreshArtists()
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

    fun refreshArtists() {
        refreshDataFromNetwork()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(ArtistsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}