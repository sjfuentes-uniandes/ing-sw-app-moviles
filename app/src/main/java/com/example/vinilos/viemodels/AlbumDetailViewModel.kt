package com.example.vinilos.viemodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter

class AlbumDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album>
        get() = _album

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    
    fun loadAlbumDetail(albumId: Int) {
        Log.d("AlbumDetailViewModel", "Cargando detalle del artista con ID: $albumId")
        _isLoading.value = true

        NetworkServiceAdapter.getInstance(getApplication())
            .getAlbumDetail(
                albumId,
                { album ->
                    Log.d("AlbumDetailViewModel", "Album cargado exitosamente: ${album.name}")
                    _album.value = album
                    _isLoading.value = false
                },
                { error ->
                    val errorMessage = when {
                        error.networkResponse != null -> {
                            val statusCode = error.networkResponse.statusCode
                            val data = error.networkResponse.data?.let { String(it) } ?: "Sin datos"
                            Log.e("AlbumDetailViewModel", "Error de red - Código: $statusCode, Datos: $data")
                            "Error $statusCode: $data"
                        }
                        error.message != null -> {
                            Log.e("AlbumDetailViewModel", "Error con mensaje: ${error.message}")
                            error.message!!
                        }
                        else -> {
                            Log.e("AlbumDetailViewModel", "Error desconocido: ${error.toString()}")
                            "Error desconocido al cargar el álbum: ${error.toString()}"
                        }
                    }
                    _error.value = errorMessage
                    _isLoading.value = false
                }
            )
    }
}