package com.mealplanner.mealplan.presentation.mealplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.model.MealType
import java.time.LocalDate

/**
 * 三餐规划页面
 * 
 * @param navController 导航控制器
 * @param viewModel ViewModel
 */
@Composable
fun MealPlanScreen(
    navController: androidx.navigation.NavController,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("三餐规划") }
            )
        },
        floatingActionButton = {
            if (uiState.mealPlansForToday.isEmpty() && !uiState.isGenerating) {
                androidx.compose.material3.FloatingActionButton(
                    onClick = { viewModel.onEvent(MealPlanUiEvent.OnGenerateMealPlan) }
                ) {
                    Text("生成三餐")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 今日热量进度
            CalorieProgressSection(uiState = uiState)
            
            // 生成按钮
            if (uiState.mealPlansForToday.isEmpty() && !uiState.isGenerating) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Button(
                        onClick = { viewModel.onEvent(MealPlanUiEvent.OnGenerateMealPlan) }
                    ) {
                        Text("生成今日三餐")
                    }
                }
            }
            
            // 加载指示器
            if (uiState.isGenerating) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("正在生成三餐计划...")
                    }
                }
            }
            
            // 今日三餐列表
            if (uiState.mealPlansForToday.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.mealPlansForToday) { meal ->
                        MealPlanCard(
                            meal = meal,
                            onConfirmClick = { mealId ->
                                viewModel.onEvent(MealPlanUiEvent.OnConfirmMeal(mealId, true))
                            },
                            onModifyClick = { mealId ->
                                // 处理修改
                            },
                            onTakeoutClick = { mealId ->
                                viewModel.onEvent(MealPlanUiEvent.OnTakeout(mealId))
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 热量进度部分
 */
@Composable
fun CalorieProgressSection(uiState: MealPlanUiState) {
    val totalCalories = uiState.mealPlansForToday.sumOf { it.calories.toDouble() }.toFloat()
    val targetCalories = 2000f // 简化：固定目标
    val progress = if (targetCalories > 0) (totalCalories / targetCalories).coerceIn(0f, 1f) else 0f
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "今日热量",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 进度环
            Box(
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.height(100.dp),
                    color = if (progress > 0.9f) Color(0xFFF44336) else MaterialTheme.colorScheme.primary
                )
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${totalCalories.toInt()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/ ${targetCalories.toInt()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

/**
 * 三餐计划卡片
 */
@Composable
fun MealPlanCard(
    meal: MealPlan,
    onConfirmClick: (String) -> Unit,
    onModifyClick: (String) -> Unit,
    onTakeoutClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 餐类型 + 菜名
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = meal.mealType.getDisplayName(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${meal.calories.toInt()} 卡",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = meal.dishName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 食材状态
            Text(
                text = "食材: ${meal.ingredients.size} 项",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onConfirmClick(meal.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("按推荐吃了")
                }
                
                Button(
                    onClick = { onModifyClick(meal.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("有修改")
                }
                
                Button(
                    onClick = { onTakeoutClick(meal.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("外卖")
                }
            }
        }
    }
}
