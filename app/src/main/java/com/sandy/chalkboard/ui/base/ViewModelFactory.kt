package com.sandy.chalkboard.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sandy.chalkboard.coroutines.data.api.ApiHelper
import com.sandy.chalkboard.coroutines.data.repositories.DataRepository
import com.sandy.chalkboard.ui.viewmodel.DataViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
            return DataViewModel(DataRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Class name not found")
    }
}