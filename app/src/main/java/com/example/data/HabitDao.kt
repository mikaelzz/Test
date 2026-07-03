package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Query("UPDATE habits SET streak = :streak WHERE id = :id")
    suspend fun updateStreak(id: Int, streak: Int)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY timestamp DESC")
    fun getLogsForHabit(habitId: Int): Flow<List<HabitLog>>
    
    @Query("SELECT * FROM habit_logs")
    fun getAllLogs(): Flow<List<HabitLog>>
}
