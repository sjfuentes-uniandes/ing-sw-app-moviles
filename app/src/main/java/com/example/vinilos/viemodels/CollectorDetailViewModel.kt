package com.example.vinilos.viemodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.models.Collector
import com.example.vinilos.network.NetworkServiceAdapter

class CollectorDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _collector = MutableLiveData<Collector>()
    val collector: LiveData<Collector>
        get() = _collector

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    
    fun loadCollectorDetail(collectorId: Int) {
        Log.d("CollectorDetailViewModel", "Cargando detalle del colleccionista con ID: $collectorId")
        _isLoading.value = true

        NetworkServiceAdapter.getInstance(getApplication())
            .getCollectorDetail(
                collectorId,
                { collector ->
                    Log.d("CollectorDetailViewModel", "Coleccionista cargado exitosamente: ${collector.name}")
                    _collector.value = collector
                    _isLoading.value = false
                },
                { error ->
                    val errorMessage = when {
                        error.networkResponse != null -> {
                            val statusCode = error.networkResponse.statusCode
                            val data = error.networkResponse.data?.let { String(it) } ?: "Sin datos"
                            Log.e("CollectorDetailViewModel", "Error de red - Código: $statusCode, Datos: $data")
                            "Error $statusCode: $data"
                        }
                        error.message != null -> {
                            Log.e("CollectorDetailViewModel", "Error con mensaje: ${error.message}")
                            error.message!!
                        }
                        else -> {
                            Log.e("CollectorDetailViewModel", "Error desconocido: ${error.toString()}")
                            "Error desconocido al cargar el coleccionista: ${error.toString()}"
                        }
                    }
                    _error.value = errorMessage
                    _isLoading.value = false
                }
            )
    }
}