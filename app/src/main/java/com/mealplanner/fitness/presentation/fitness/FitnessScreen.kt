package com.mealplanner.fitness.presentation.fitness

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mealplanner.core.ui.components.GlassCard
import java.time.LocalDate

/**
 * 运动追踪页面
 * 
 * @param viewModel FitnessViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessScreen(
    viewModel: FitnessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("运动追踪") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(FitnessUiEvent.OnShowAddDialog) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "记录运动")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 今日消耗热量卡片
            item {
                TodayCaloriesCard(totalCaloriesBurned = uiState.totalCaloriesBurned)
            }
            
            // 运动记录列表标题
            item {
                Text(
                    text = "今日运动记录",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // 运动记录列表
            if (uiState.todayRecords.isEmpty()) {
                item {
                    EmptyExerciseRecord()
                }
            } else {
                items(uiState.todayRecords) { record ->
                    ExerciseRecordItem(record = record)
                }
            }
        }
        
        // 添加运动记录对话框
        if (uiState.showAddDialog) {
            AddExerciseDialog(
                exerciseTypes = uiState.exerciseTypes,
                onDismiss = { viewModel.onEvent(FitnessUiEvent.OnDismissAddDialog) },
                onConfirm = { exerciseType, durationMinutes, weightKg ->
                    val calories = viewModel.uiState.value.calculatedCalories
                    val record = com.mealplanner.fitness.domain.model.ExerciseRecord(
                        id = LocalDate.now().toString() + "_" + java.util.UUID.randomUUID().toString(),
                        exerciseName = exerciseType.name,
                        metValue = exerciseType.metValue,
                        durationMinutes = durationMinutes,
                        caloriesBurned = calories,
                        date = LocalDate.now()
                    )
                    viewModel.onEvent(FitnessUiEvent.OnAddRecord(record))
                },
                onCalculateCalories = { exerciseType, durationMinutes, weightKg ->
                    viewModel.onEvent(FitnessUiEvent.OnCalculateCalories(exerciseType, durationMinutes, weightKg))
                },
                calculatedCalories = uiState.calculatedCalories
            )
        }
    }
}

/**
 * 今日消耗热量卡片
 */
@Composable
fun TodayCaloriesCard(
    totalCaloriesBurned: Float
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "今日消耗热量",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${String.format("%.1f", totalCaloriesBurned)} 千卡",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 运动记录列表项
 */
@Composable
fun ExerciseRecordItem(
    record: com.mealplanner.fitness.domain.model.ExerciseRecord
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = record.exerciseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${record.durationMinutes} 分钟",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "${String.format("%.1f", record.caloriesBurned)} 千卡",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 空运动记录提示
 */
@Composable
fun EmptyExerciseRecord() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "暂无运动记录",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 添加运动记录对话框
 */
@Composable
fun AddExerciseDialog(
    exerciseTypes: List<com.mealplanner.fitness.domain.model.ExerciseType>,
    onDismiss: () -> Unit,
    onConfirm: (com.mealplanner.fitness.domain.model.ExerciseType, Int, Float) -> Unit,
    onCalculateCalories: (com.mealplanner.fitness.domain.model.ExerciseType, Int, Float) -> Unit,
    calculatedCalories: Float
) {
    // TODO: Implement full dialog with exercise type selection, duration input, etc.
    // This is a simplified version
}
