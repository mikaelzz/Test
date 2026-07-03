package com.example.data

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()
    val allLogs: Flow<List<HabitLog>> = habitDao.getAllLogs()

    suspend fun getHabitById(id: Int): Habit? = habitDao.getHabitById(id)

    suspend fun insertHabit(habit: Habit): Long = habitDao.insertHabit(habit)

    suspend fun deleteHabit(id: Int) = habitDao.deleteHabit(id)

    suspend fun logHabit(habitId: Int) {
        habitDao.insertLog(HabitLog(habitId = habitId))
        val habit = habitDao.getHabitById(habitId)
        if (habit != null) {
            habitDao.updateStreak(habitId, habit.streak + 1)
        }
    }
}
