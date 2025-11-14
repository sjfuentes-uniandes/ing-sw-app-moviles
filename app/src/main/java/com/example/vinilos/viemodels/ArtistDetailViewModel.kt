package com.example.vinilos.viemodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter

class ArtistDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _artist = MutableLiveData<Artist>()
    val artist: LiveData<Artist> get() = _artist

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadArtistDetail(artistId: Int) {
        Log.d("ArtistDetailViewModel", "Cargando detalle del artista con ID: $artistId")
        _isLoading.value = true

        NetworkServiceAdapter.getInstance(getApplication()).getArtistDetail(
            artistId,
            { artist ->
                Log.d("ArtistDetailViewModel", "Artista cargado exitosamente: ${artist.name}")
                _artist.value = artist
                _isLoading.value = false
            },
            { error ->
                val errorMessage = when {
                    error.networkResponse != null -> {
                        val statusCode = error.networkResponse.statusCode
                        val data = error.networkResponse.data?.let { String(it) } ?: "Sin datos"
                        Log.e("ArtistDetailViewModel", "Error de red - Código: $statusCode, Datos: $data")
                        "Error $statusCode: $data"
                    }
                    error.message != null -> {
                        Log.e("ArtistDetailViewModel", "Error con mensaje: ${error.message}")
                        error.message!!
                    }
                    else -> {
                        Log.e("ArtistDetailViewModel", "Error desconocido: ${error.toString()}")
                        "Error desconocido al cargar el artista: ${error.toString()}"
                    }
                }
                _error.value = errorMessage
                _isLoading.value = false
            }
        )
    }
}
