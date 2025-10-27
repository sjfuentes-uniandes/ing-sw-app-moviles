package com.example.vinilos.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewModelScope
import com.example.vinilos.models.Collector
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.database.VinilosRoomDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class CollectorViewModel(application: Application): AndroidViewModel(application) {
    private val database = VinilosRoomDatabase.getDatabase(application)
    private val collectorsDao = database.collectorsDao()
    
    val collectors: LiveData<List<Collector>> = collectorsDao.getCollectors()

    private  var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork(){
        NetworkServiceAdapter.getInstance(getApplication()).getCollectors({
            viewModelScope.launch(Dispatchers.IO) {
                collectorsDao.insertAll(it)
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        },{
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown(){
        _isNetworkErrorShown.value = true
    }

    fun refreshCollectors(){
        refreshDataFromNetwork()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return  CollectorViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
