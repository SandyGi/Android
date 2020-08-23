package com.sandy.chalkboard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sandy.chalkboard.coroutines.Resource
import com.sandy.chalkboard.coroutines.data.repositories.DataRepository
import kotlinx.coroutines.Dispatchers

class DataViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun getSchools() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getSchools()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
    fun getClasses() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dataRepository.getClasses()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}