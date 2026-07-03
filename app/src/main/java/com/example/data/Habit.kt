package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val frequencyMinutes: Int = 0,
    val notificationSound: String = "Default",
    val streak: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
