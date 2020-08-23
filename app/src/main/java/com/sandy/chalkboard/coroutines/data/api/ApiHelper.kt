package com.sandy.chalkboard.coroutines.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getSchools() = apiService.getSchools()

    suspend fun getClasses() = apiService.getClasses()
}