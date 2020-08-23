package com.sandy.chalkboard.coroutines.data.repositories

import com.sandy.chalkboard.coroutines.data.api.ApiHelper

class DataRepository (private val apiHelper: ApiHelper) {

     suspend fun getSchools() = apiHelper.getSchools()

     suspend fun getClasses() = apiHelper.getClasses()
 }