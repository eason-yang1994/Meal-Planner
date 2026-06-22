package com.mealplanner.home.presentation

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mealplanner.core.ui.components.GlassCard
import kotlinx.coroutines.flow.first

/**
 * 首页
 * 
 * 显示今日热量进度、运动消耗、体重变化趋势和快速操作按钮
 * 
 * @param navController 导航控制器
 * @param viewModel 视图模型
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("三餐规划") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 今日热量进度卡片
            TodaysCalorieCard(
                consumedCalories = uiState.todayCaloriesConsumed,
                targetCalories = uiState.targetCalories
            )
            
            // 今日运动消耗卡片
            TodaysExerciseCard(
                exerciseCaloriesBurned = uiState.todayExerciseCaloriesBurned
            )
            
            // 体重变化趋势卡片（简化版）
            WeightTrendCard(
                currentWeight = uiState.currentWeight,
                bmi = uiState.bmi
            )
            
            // 快速操作按钮
            QuickActionButtons(
                onRecordWeight = {
                    // TODO: Navigate to weight recording
                },
                onRecordExercise = {
                    navController.navigate("fitness")
                },
                onGenerateMeal = {
                    navController.navigate("meal_plan")
                }
            )
        }
    }
}

/**
 * 今日热量进度卡片
 */
@Composable
fun TodaysCalorieCard(
    consumedCalories: Float,
    targetCalories: Float
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "今日热量",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 热量进度（简化显示）
            val progress = if (targetCalories > 0) {
                (consumedCalories / targetCalories).coerceIn(0f, 1f)
            } else {
                0f
            }
            
            androidx.compose.material3.LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = if (progress > 0.8f) Color(0xFFE53935) else MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "摄入: ${String.format("%.1f", consumedCalories)} 千卡",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "目标: ${String.format("%.1f", targetCalories)} 千卡",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * 今日运动消耗卡片
 */
@Composable
fun TodaysExerciseCard(
    exerciseCaloriesBurned: Float
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "运动消耗",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Text(
                text = "${String.format("%.1f", exerciseCaloriesBurned)} 千卡",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * 体重变化趋势卡片
 */
@Composable
fun WeightTrendCard(
    currentWeight: Float?,
    bmi: Float?
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
                    text = "当前体重",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                if (currentWeight != null) {
                    Text(
                        text = "${String.format("%.1f", currentWeight)} kg",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "未记录",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (bmi != null) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "BMI",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = String.format("%.1f", bmi),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 快速操作按钮
 */
@Composable
fun QuickActionButtons(
    onRecordWeight: () -> Unit,
    onRecordExercise: () -> Unit,
    onGenerateMeal: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "快速操作",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 记录体重按钮
                Button(
                    onClick = onRecordWeight,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Scale, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("记录体重", style = MaterialTheme.typography.bodySmall)
                }
                
                // 记录运动按钮
                Button(
                    onClick = onRecordExercise,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("记录运动", style = MaterialTheme.typography.bodySmall)
                }
                
                // 生成三餐按钮
                Button(
                    onClick = onGenerateMeal,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("生成三餐", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
