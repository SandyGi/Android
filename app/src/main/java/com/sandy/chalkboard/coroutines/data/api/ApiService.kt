package com.sandy.chalkboard.coroutines.data.api

import com.sandy.chalkboard.coroutines.data.model.Class
import com.sandy.chalkboard.coroutines.data.model.School
import retrofit2.http.GET

interface ApiService {

    @GET("school")
    suspend fun getSchools(): List<School>

    @GET("class")
    suspend fun getClasses(): List<Class>

}