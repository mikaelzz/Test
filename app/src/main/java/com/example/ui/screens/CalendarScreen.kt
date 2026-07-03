package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Habit
import com.example.viewmodel.HabitViewModel

@Composable
fun CalendarScreen(viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    
    // In a real app we'd map logs to dates. Here we show habits that are active and their frequencies.
    val scheduledHabits = habits.filter { it.frequencyMinutes > 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Scheduled Tasks", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (scheduledHabits.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tasks with reminders scheduled.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(scheduledHabits) { habit ->
                    ScheduledHabitCard(habit)
                }
            }
        }
    }
}

@Composable
fun ScheduledHabitCard(habit: Habit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(habit.name, fontWeight = FontWeight.Bold)
                Text("Reminds every ${habit.frequencyMinutes} mins", style = MaterialTheme.typography.bodySmall)
                Text("Sound: ${habit.notificationSound}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
