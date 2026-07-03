package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.viewmodel.HabitViewModel

data class Badge(val name: String, val requiredStreak: Int, val iconResId: Int)

val ALL_BADGES = listOf(
    Badge("Starter", 1, R.drawable.img_badge_gold_1783113535529),
    Badge("Consistency", 7, R.drawable.img_badge_gold_1783113535529),
    Badge("Dedication", 30, R.drawable.img_badge_gold_1783113535529),
    Badge("Mastery", 100, R.drawable.img_badge_gold_1783113535529)
)

@Composable
fun AchievementsScreen(viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    val maxStreak = habits.maxOfOrNull { it.streak } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Achievements", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Highest Streak: $maxStreak 🔥", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(ALL_BADGES) { badge ->
                val isUnlocked = maxStreak >= badge.requiredStreak
                BadgeCard(badge, isUnlocked)
            }
        }
    }
}

@Composable
fun BadgeCard(badge: Badge, isUnlocked: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(0.8f),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val alpha = if (isUnlocked) 1f else 0.3f
            Image(
                painter = painterResource(id = badge.iconResId),
                contentDescription = badge.name,
                modifier = Modifier.size(80.dp),
                alpha = alpha
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(badge.name, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Requires ${badge.requiredStreak} streak", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        }
    }
}
