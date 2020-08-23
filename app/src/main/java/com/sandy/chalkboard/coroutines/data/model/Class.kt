package com.sandy.chalkboard.coroutines.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Class(
    @SerializedName("title")
    val title: String,

    @SerializedName("descrition")
    val descrition: String,

    @SerializedName("eventDate")
    val eventDate: Long,

    @SerializedName("createdDate")
    val createdDate: Long,

    @SerializedName("standard")
    val standard: String,

    @SerializedName("section")
    val section: String
)