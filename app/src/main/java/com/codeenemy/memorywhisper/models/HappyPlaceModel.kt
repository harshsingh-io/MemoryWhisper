package com.codeenemy.memorywhisper.models

import androidx.room.Entity

@Entity(tableName = "happyPlace-table")

data class HappyPlaceModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
)