package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DatabaseProvider
import com.example.data.Habit
import com.example.data.HabitLog
import com.example.data.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository

    init {
        val database = DatabaseProvider.getDatabase(application)
        repository = HabitRepository(database.habitDao())
    }

    val habits: StateFlow<List<Habit>> = repository.allHabits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val logs: StateFlow<List<HabitLog>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addHabit(name: String, description: String, frequencyMinutes: Int, sound: String) {
        viewModelScope.launch {
            repository.insertHabit(
                Habit(
                    name = name,
                    description = description,
                    frequencyMinutes = frequencyMinutes,
                    notificationSound = sound
                )
            )
        }
    }
    
    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }

    fun markHabitCompleted(habitId: Int) {
        viewModelScope.launch {
            // Check if already completed today
            val todayStart = getTodayStartMillis()
            val allLogs = repository.allLogs.first()
            val alreadyDone = allLogs.any { it.habitId == habitId && it.timestamp >= todayStart }
            if (!alreadyDone) {
                repository.logHabit(habitId)
            }
        }
    }

    private fun getTodayStartMillis(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}

class HabitViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
