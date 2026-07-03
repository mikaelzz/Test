package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Habit
import com.example.viewmodel.HabitViewModel
import java.util.Calendar

@Composable
fun DashboardScreen(
    viewModel: HabitViewModel,
    onAddHabitClick: () -> Unit
) {
    val habits by viewModel.habits.collectAsState()
    val logs by viewModel.logs.collectAsState()

    val todayStart = getTodayStartMillis()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabitClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Your Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            ProgressChart(habits = habits)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Today's Habits", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Tap + to add one!")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(habits) { habit ->
                        val isCompletedToday = logs.any { it.habitId == habit.id && it.timestamp >= todayStart }
                        HabitCard(
                            habit = habit,
                            isCompletedToday = isCompletedToday,
                            onComplete = { viewModel.markHabitCompleted(habit.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HabitCard(habit: Habit, isCompletedToday: Boolean, onComplete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompletedToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(habit.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("🔥 Streak: ${habit.streak}", color = Color(0xFFE65100), fontWeight = FontWeight.SemiBold)
            }
            if (isCompletedToday) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            } else {
                Button(onClick = onComplete) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun ProgressChart(habits: List<Habit>) {
    // Simple bar chart showing top 5 streaks
    val topHabits = habits.sortedByDescending { it.streak }.take(5)
    val maxStreak = topHabits.maxOfOrNull { it.streak }?.coerceAtLeast(1) ?: 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Top Streaks", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (topHabits.isEmpty()) {
                Text("No data", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                val barColor = MaterialTheme.colorScheme.primary
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp)
                ) {
                    val barWidth = size.width / (topHabits.size * 2)
                    topHabits.forEachIndexed { index, habit ->
                        val barHeight = (habit.streak.toFloat() / maxStreak) * size.height
                        val xOffset = index * (barWidth * 2) + barWidth / 2
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(xOffset, size.height - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

fun getTodayStartMillis(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}
