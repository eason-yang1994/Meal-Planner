package com.mealplanner.userprofile.presentation.setup

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mealplanner.userprofile.domain.model.ActivityLevel
import com.mealplanner.userprofile.domain.model.DietGoal
import com.mealplanner.userprofile.domain.model.Gender
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * 设置向导页面
 * 
 * @param navController 导航控制器
 * @param viewModel 视图模型
 */
@Composable
fun SetupWizardScreen(
    navController: NavController,
    viewModel: SetupWizardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                SetupWizardUiEffect.NavigateToHome -> {
                    navController.navigate("home") {
                        popUpTo("setup-wizard") { inclusive = true }
                    }
                }
                is SetupWizardUiEffect.ShowError -> {
                    // 显示错误提示（可以添加到 Snackbar）
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("首次设置") }
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
            // 步骤指示器
            Text(
                text = "步骤 ${uiState.currentStep} / 5",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 根据当前步骤显示不同内容
            when (uiState.currentStep) {
                1 -> Step1Content(viewModel = viewModel, uiState = uiState)
                2 -> Step2Content(viewModel = viewModel, uiState = uiState)
                3 -> Step3Content(viewModel = viewModel, uiState = uiState)
                4 -> Step4Content(viewModel = viewModel, uiState = uiState)
                5 -> Step5Content(viewModel = viewModel, uiState = uiState)
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 底部按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                if (uiState.currentStep > 1) {
                    Button(
                        onClick = { viewModel.onEvent(SetupWizardUiEvent.OnPreviousStep) }
                    ) {
                        Text("上一步")
                    }
                }
                
                Button(
                    onClick = {
                        if (uiState.currentStep < 5) {
                            viewModel.onEvent(SetupWizardUiEvent.OnNextStep)
                        } else {
                            viewModel.onEvent(SetupWizardUiEvent.OnSave)
                        }
                    },
                    enabled = canProceedToNextStep(uiState)
                ) {
                    Text(if (uiState.currentStep < 5) "下一步" else "完成设置")
                }
            }
        }
    }
}

/**
 * 步骤 1 内容：性别和出生日期
 */
@Composable
fun Step1Content(viewModel: SetupWizardViewModel, uiState: SetupWizardUiState) {
    Column {
        Text(text = "基本信息", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 性别选择卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "性别", style = MaterialTheme.typography.bodyMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.gender == Gender.MALE,
                            onClick = { viewModel.onEvent(SetupWizardUiEvent.OnGenderChange(Gender.MALE)) }
                        )
                        Text(text = "男")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = uiState.gender == Gender.FEMALE,
                            onClick = { viewModel.onEvent(SetupWizardUiEvent.OnGenderChange(Gender.FEMALE)) }
                        )
                        Text(text = "女")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 出生日期选择
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "出生日期", style = MaterialTheme.typography.bodyMedium)
                var showDatePicker by remember { mutableStateOf(false) }
                
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = uiState.birthDate?.toString() ?: "选择日期")
                }
                
                if (showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                                        Instant.ofEpochMilli(millis)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                    }
                                    selectedDate?.let {
                                        viewModel.onEvent(SetupWizardUiEvent.OnBirthDateChange(it))
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("确定")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDatePicker = false }
                            ) {
                                Text("取消")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }
    }
}

/**
 * 步骤 2 内容：身高和体重
 */
@Composable
fun Step2Content(viewModel: SetupWizardViewModel, uiState: SetupWizardUiState) {
    Column {
        Text(text = "身体数据", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 身高输入
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "身高 (cm)", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = if (uiState.height > 0) uiState.height.toString() else "",
                    onValueChange = { value ->
                        val height = value.toFloatOrNull() ?: 0f
                        viewModel.onEvent(SetupWizardUiEvent.OnHeightChange(height))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 体重输入
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "体重 (kg)", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = if (uiState.weight > 0) uiState.weight.toString() else "",
                    onValueChange = { value ->
                        val weight = value.toFloatOrNull() ?: 0f
                        viewModel.onEvent(SetupWizardUiEvent.OnWeightChange(weight))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * 步骤 3 内容：活动水平
 */
@Composable
fun Step3Content(viewModel: SetupWizardViewModel, uiState: SetupWizardUiState) {
    Column {
        Text(text = "活动水平", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ActivityLevel.values().forEach { level ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.activityLevel == level) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                onClick = { viewModel.onEvent(SetupWizardUiEvent.OnActivityLevelChange(level)) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.activityLevel == level,
                        onClick = { viewModel.onEvent(SetupWizardUiEvent.OnActivityLevelChange(level)) }
                    )
                    Column {
                        Text(text = level.label, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "活动系数: ${level.factor}", 
                            fontSize = 12.sp, 
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/**
 * 步骤 4 内容：饮食目标
 */
@Composable
fun Step4Content(viewModel: SetupWizardViewModel, uiState: SetupWizardUiState) {
    Column {
        Text(text = "饮食目标", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DietGoal.values().forEach { goal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.goal == goal) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                onClick = { viewModel.onEvent(SetupWizardUiEvent.OnGoalChange(goal)) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.goal == goal,
                        onClick = { viewModel.onEvent(SetupWizardUiEvent.OnGoalChange(goal)) }
                    )
                    Text(
                        text = when (goal) {
                            DietGoal.LOSE_WEIGHT -> "减重"
                            DietGoal.MAINTAIN -> "维持体重"
                            DietGoal.GAIN_WEIGHT -> "增重"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 目标体重输入（如果选择减重或增重）
        if (uiState.goal != DietGoal.MAINTAIN) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "目标体重 (kg)", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = if (uiState.targetWeight > 0) uiState.targetWeight.toString() else "",
                        onValueChange = { value ->
                            val targetWeight = value.toFloatOrNull() ?: 0f
                            viewModel.onEvent(SetupWizardUiEvent.OnTargetWeightChange(targetWeight))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * 步骤 5 内容：确认信息
 */
@Composable
fun Step5Content(viewModel: SetupWizardViewModel, uiState: SetupWizardUiState) {
    Column {
        Text(text = "确认信息", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "性别: ${if (uiState.gender == Gender.MALE) "男" else "女"}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "出生日期: ${uiState.birthDate}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "身高: ${uiState.height} cm", style = MaterialTheme.typography.bodyLarge)
                Text(text = "体重: ${uiState.weight} kg", style = MaterialTheme.typography.bodyLarge)
                Text(text = "活动水平: ${uiState.activityLevel?.label ?: ""}", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "饮食目标: ${
                        when (uiState.goal) {
                            DietGoal.LOSE_WEIGHT -> "减重"
                            DietGoal.MAINTAIN -> "维持体重"
                            DietGoal.GAIN_WEIGHT -> "增重"
                            null -> ""
                        }
                    }",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (uiState.targetWeight > 0) {
                    Text(text = "目标体重: ${uiState.targetWeight} kg", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "正在保存...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/**
 * 检查是否可以进入下一步
 * 
 * @param uiState 当前 UI 状态
 * @return 如果可以进入下一步则返回 true
 */
fun canProceedToNextStep(uiState: SetupWizardUiState): Boolean {
    return when (uiState.currentStep) {
        1 -> uiState.gender != null && uiState.birthDate != null
        2 -> uiState.height > 0 && uiState.weight > 0
        3 -> uiState.activityLevel != null
        4 -> uiState.goal != null
        5 -> true
        else -> false
    }
}
