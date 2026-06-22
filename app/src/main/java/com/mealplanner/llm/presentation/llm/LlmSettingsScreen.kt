package com.mealplanner.llm.presentation.llm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * LLM 设置页面
 * 
 * @param viewModel LlmSettingsViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LlmSettingsScreen(
    viewModel: LlmSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var selectedProvider by remember { mutableStateOf(uiState.config?.provider ?: "OPENAI") }
    var apiKey by remember { mutableStateOf(uiState.config?.apiKey ?: "") }
    var baseUrl by remember { mutableStateOf(uiState.config?.baseUrl ?: "https://api.openai.com/") }
    var model by remember { mutableStateOf(uiState.config?.model ?: "gpt-3.5-turbo") }
    var expanded by remember { mutableStateOf(false) }
    
    val providers = listOf("OPENAI", "GEMINI", "CLAUDE", "CUSTOM")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LLM 设置") }
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
            // Provider 选择
            Text(
                text = "提供商",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedProvider,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    providers.forEach { provider ->
                        DropdownMenuItem(
                            text = { Text(provider) },
                            onClick = {
                                selectedProvider = provider
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // API Key 输入
            Text(
                text = "API Key",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("请输入 API Key") }
            )
            
            // Base URL 输入
            Text(
                text = "Base URL",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = baseUrl,
                onValueChange = { baseUrl = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("请输入 Base URL") }
            )
            
            // Model 输入
            Text(
                text = "模型",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("请输入模型名称") }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 测试连接按钮
            Button(
                onClick = {
                    viewModel.onEvent(LlmSettingsUiEvent.OnTestConnection("Hello"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("测试连接")
            }
            
            // 保存配置按钮
            Button(
                onClick = {
                    val config = com.mealplanner.llm.domain.model.LlmConfig(
                        provider = selectedProvider,
                        apiKey = apiKey,
                        baseUrl = baseUrl,
                        model = model
                    )
                    viewModel.onEvent(LlmSettingsUiEvent.OnSaveConfig(config))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text("保存配置")
            }
            
            // 清除配置按钮
            TextButton(
                onClick = {
                    viewModel.onEvent(LlmSettingsUiEvent.OnClearConfig)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Clear, contentDescription = null)
                Text("清除配置")
            }
            
            // 测试结果
            if (uiState.testResult != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = uiState.testResult!!,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
