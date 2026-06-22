package com.mealplanner.weight.presentation.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mealplanner.core.ui.components.GlassCard
import java.time.format.DateTimeFormatter

/**
 * 体重页面
 * 
 * @param navController 导航控制器
 * @param viewModel 视图模型
 */
@Composable
fun WeightScreen(
    navController: NavController,
    viewModel: WeightViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                WeightUiEffect.RecordAdded -> {
                    // 记录添加成功
                }
                is WeightUiEffect.ShowError -> {
                    // 显示错误提示
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("体重追踪") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部：今日体重卡片
            TodayWeightCard(uiState = uiState)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 中部：体重曲线图（占位，Task-012 实现）
            WeightChartPlaceholder()
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 底部："记录体重" 按钮
            Button(
                onClick = { viewModel.onEvent(WeightUiEvent.OnShowAddDialog) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("记录体重")
            }
        }
    }
    
    // 添加体重对话框
    if (uiState.showAddDialog) {
        AddWeightDialog(
            onDismiss = { viewModel.onEvent(WeightUiEvent.OnDismissAddDialog) },
            onConfirm = { weight, note ->
                viewModel.onEvent(WeightUiEvent.OnAddRecord(weight, note))
            }
        )
    }
}

/**
 * 今日体重卡片
 */
@Composable
fun TodayWeightCard(uiState: WeightUiState) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "今日体重",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (uiState.latestWeight != null) {
                Text(
                    text = "${String.format("%.1f", uiState.latestWeight)} kg",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (uiState.bmi != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "BMI: ${String.format("%.1f", uiState.bmi)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    // BMI 分类
                    val bmiCategory = when {
                        uiState.bmi < 18.5 -> "偏瘦"
                        uiState.bmi < 24 -> "正常"
                        uiState.bmi < 28 -> "偏胖"
                        else -> "肥胖"
                    }
                    Text(
                        text = bmiCategory,
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (bmiCategory) {
                            "正常" -> Color(0xFF4CAF50)
                            "偏瘦" -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                    )
                }
            } else {
                Text(
                    text = "暂无记录",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * 体重曲线图占位
 */
@Composable
fun WeightChartPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "体重曲线图\n（待实现）",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

/**
 * 添加体重对话框
 */
@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (weight: Float, note: String?) -> Unit
) {
    var weightText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("记录体重") },
        text = {
            Column {
                // 体重输入
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { 
                        weightText = it
                        showError = false
                    },
                    label = { Text("体重 (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = showError
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 备注输入（可选）
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("备注（可选）") },
                    singleLine = true
                )
                
                if (showError) {
                    Text(
                        text = "请输入有效的体重",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    if (weight != null && weight > 0) {
                        onConfirm(weight, note.ifEmpty { null })
                    } else {
                        showError = true
                    }
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        }
    )
}
