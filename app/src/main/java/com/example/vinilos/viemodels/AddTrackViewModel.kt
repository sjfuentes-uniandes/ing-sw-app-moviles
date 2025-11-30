package com.example.vinilos.viemodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vinilos.network.NetworkServiceAdapter

/**
 * ViewModel para manejar la lógica de agregar tracks a un álbum
 * Sigue el patrón MVVM del proyecto
 */
class AddTrackViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _trackAddedResult = MutableLiveData<Result<Boolean>>()
    val trackAddedResult: LiveData<Result<Boolean>> = _trackAddedResult

    /**
     * Agrega un nuevo track al álbum especificado
     *
     * @param albumId ID del álbum al que se agregará el track
     * @param trackName Nombre de la canción
     * @param trackDuration Duración en formato M:SS o MM:SS
     * @param context Contexto de la aplicación
     */
    fun addTrackToAlbum(
        albumId: Int,
        trackName: String,
        trackDuration: String,
        context: Context
    ) {
        _isLoading.value = true
        Log.d("AddTrackViewModel", "Agregando track: $trackName ($trackDuration) al álbum $albumId")

        NetworkServiceAdapter.getInstance(context).addTrackToAlbum(
            albumId = albumId,
            trackName = trackName,
            trackDuration = trackDuration,
            onComplete = { success ->
                _isLoading.value = false
                _trackAddedResult.value = Result.success(success)
                Log.d("AddTrackViewModel", "✅ Track agregado exitosamente")
            },
            onError = { error ->
                _isLoading.value = false
                val errorMessage = error.message ?: "Error desconocido al agregar el track"
                Log.e("AddTrackViewModel", "❌ Error: $errorMessage", error)
                _trackAddedResult.value = Result.failure(Exception(errorMessage))
            }
        )
    }
}

