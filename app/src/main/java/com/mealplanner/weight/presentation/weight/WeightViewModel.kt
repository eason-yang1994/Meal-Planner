package com.mealplanner.weight.presentation.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import com.mealplanner.weight.domain.usecase.AddWeightRecordUseCase
import com.mealplanner.weight.domain.usecase.CalculateBMIUseCase
import com.mealplanner.weight.domain.usecase.GetWeightRecordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 体重页面 ViewModel
 * 
 * 管理体重记录的状态和事件
 */
@HiltViewModel
class WeightViewModel @Inject constructor(
    private val getWeightRecordsUseCase: GetWeightRecordsUseCase,
    private val addWeightRecordUseCase: AddWeightRecordUseCase,
    private val calculateBMIUseCase: CalculateBMIUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    // UI 状态
    private val _uiState = MutableStateFlow(WeightUiState())
    val uiState: StateFlow<WeightUiState> = _uiState.asStateFlow()
    
    // 单次事件
    private val _uiEffect = MutableSharedFlow<WeightUiEffect>()
    val uiEffect: SharedFlow<WeightUiEffect> = _uiEffect.asSharedFlow()
    
    init {
        loadWeightRecords()
        loadUserProfile()
    }
    
    /**
     * 处理事件
     * 
     * @param event 用户事件
     */
    fun onEvent(event: WeightUiEvent) {
        when (event) {
            is WeightUiEvent.OnAddRecord -> {
                addWeightRecord(event.weight, event.note)
            }
            is WeightUiEvent.OnDeleteRecord -> {
                deleteWeightRecord(event.id)
            }
            WeightUiEvent.OnShowAddDialog -> {
                _uiState.update { it.copy(showAddDialog = true) }
            }
            WeightUiEvent.OnDismissAddDialog -> {
                _uiState.update { it.copy(showAddDialog = false) }
            }
        }
    }
    
    /**
     * 加载体重记录
     */
    private fun loadWeightRecords() {
        viewModelScope.launch {
            getWeightRecordsUseCase().collect { records ->
                _uiState.update { it.copy(records = records) }
                
                // 更新最新体重
                if (records.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(latestWeight = records.first().weightKg)
                    }
                    
                    // 计算 BMI
                    calculateBMI()
                }
            }
        }
    }
    
    /**
     * 加载用户资料（用于获取身高计算 BMI）
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                _uiState.update { it.copy(userProfile = profile) }
                calculateBMI()
            }
        }
    }
    
    /**
     * 计算 BMI
     */
    private fun calculateBMI() {
        val state = _uiState.value
        if (state.latestWeight != null && state.userProfile != null) {
            val bmi = calculateBMIUseCase(
                state.latestWeight,
                state.userProfile.heightCm
            )
            _uiState.update { it.copy(bmi = bmi) }
        }
    }
    
    /**
     * 添加体重记录
     * 
     * @param weight 体重（公斤）
     * @param note 备注（可选）
     */
    private fun addWeightRecord(weight: Float, note: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = addWeightRecordUseCase(weight, note)
            if (result.isSuccess) {
                _uiEffect.emit(WeightUiEffect.RecordAdded)
                _uiState.update { it.copy(showAddDialog = false) }
            } else {
                _uiEffect.emit(WeightUiEffect.ShowError("添加失败，请重试"))
            }
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    
    /**
     * 删除体重记录
     * 
     * @param id 记录 ID
     */
    private fun deleteWeightRecord(id: String) {
        viewModelScope.launch {
            // TODO: 实现删除功能
        }
    }
}

/**
 * 体重页面 UI 状态
 * 
 * @param records 体重记录列表
 * @param latestWeight 最新体重（公斤）
 * @param bmi BMI 值
 * @param showAddDialog 是否显示添加对话框
 * @param isLoading 是否正在加载
 * @param userProfile 用户资料（用于计算 BMI）
 */
data class WeightUiState(
    val records: List<com.mealplanner.weight.domain.model.WeightRecord> = emptyList(),
    val latestWeight: Float? = null,
    val bmi: Float? = null,
    val showAddDialog: Boolean = false,
    val isLoading: Boolean = false,
    val userProfile: com.mealplanner.userprofile.domain.model.UserProfile? = null
)

/**
 * 体重页面 UI 事件
 */
sealed class WeightUiEvent {
    data class OnAddRecord(val weight: Float, val note: String?) : WeightUiEvent()
    data class OnDeleteRecord(val id: String) : WeightUiEvent()
    object OnShowAddDialog : WeightUiEvent()
    object OnDismissAddDialog : WeightUiEvent()
}

/**
 * 体重页面 UI 效果（单次事件）
 */
sealed class WeightUiEffect {
    object RecordAdded : WeightUiEffect()
    data class ShowError(val message: String) : WeightUiEffect()
}
