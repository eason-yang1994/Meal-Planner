package com.mealplanner.llm.presentation.llm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.llm.domain.model.LlmConfig
import com.mealplanner.llm.domain.usecase.ChatWithLlmUseCase
import com.mealplanner.llm.domain.usecase.GetLlmConfigUseCase
import com.mealplanner.llm.domain.usecase.SaveLlmConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LLM 设置 ViewModel
 * 
 * @param saveLlmConfigUseCase 保存配置用例
 * @param getLlmConfigUseCase 获取配置用例
 * @param chatWithLlmUseCase 聊天用例
 */
@HiltViewModel
class LlmSettingsViewModel @Inject constructor(
    private val saveLlmConfigUseCase: SaveLlmConfigUseCase,
    private val getLlmConfigUseCase: GetLlmConfigUseCase,
    private val chatWithLlmUseCase: ChatWithLlmUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LlmSettingsUiState())
    val uiState: StateFlow<LlmSettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadConfig()
    }
    
    /**
     * 加载配置
     */
    private fun loadConfig() {
        viewModelScope.launch {
            getLlmConfigUseCase().collectLatest { config ->
                _uiState.update { 
                    it.copy(
                        config = config,
                        isConfigured = config != null
                    )
                }
            }
        }
    }
    
    /**
     * 处理 UI 事件
     */
    fun onEvent(event: LlmSettingsUiEvent) {
        when (event) {
            is LlmSettingsUiEvent.OnSaveConfig -> saveConfig(event.config)
            is LlmSettingsUiEvent.OnTestConnection -> testConnection(event.prompt)
            is LlmSettingsUiEvent.OnClearConfig -> clearConfig()
        }
    }
    
    /**
     * 保存配置
     */
    private fun saveConfig(config: LlmConfig) {
        viewModelScope.launch {
            saveLlmConfigUseCase(config)
            _uiState.update { it.copy(isConfigured = true) }
        }
    }
    
    /**
     * 测试连接
     */
    private fun testConnection(prompt: String) {
        viewModelScope.launch {
            val result = chatWithLlmUseCase(prompt)
            _uiState.update { 
                it.copy(
                    testResult = result.getOrNull() ?: result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 清除配置
     */
    private fun clearConfig() {
        viewModelScope.launch {
            // TODO: Clear config from DataStore
            _uiState.update { 
                it.copy(
                    config = null,
                    isConfigured = false,
                    testResult = null
                )
            }
        }
    }
}

/**
 * LLM 设置 UI 状态
 * 
 * @param config LLM 配置
 * @param isConfigured 是否已配置
 * @param testResult 测试结果
 */
data class LlmSettingsUiState(
    val config: LlmConfig? = null,
    val isConfigured: Boolean = false,
    val testResult: String? = null
)

/**
 * LLM 设置 UI 事件
 */
sealed class LlmSettingsUiEvent {
    data class OnSaveConfig(val config: LlmConfig) : LlmSettingsUiEvent()
    data class OnTestConnection(val prompt: String) : LlmSettingsUiEvent()
    object OnClearConfig : LlmSettingsUiEvent()
}
