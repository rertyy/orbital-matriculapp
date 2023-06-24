package com.example.frontend.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hyperlink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)