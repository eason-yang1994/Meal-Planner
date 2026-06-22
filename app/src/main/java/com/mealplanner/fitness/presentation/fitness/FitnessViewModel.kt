package com.mealplanner.fitness.presentation.fitness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.fitness.domain.model.ExerciseRecord
import com.mealplanner.fitness.domain.model.ExerciseType
import com.mealplanner.fitness.domain.usecase.AddExerciseRecordUseCase
import com.mealplanner.fitness.domain.usecase.CalculateCaloriesBurnedUseCase
import com.mealplanner.fitness.domain.usecase.GetExerciseRecordsUseCase
import com.mealplanner.fitness.domain.usecase.GetExerciseTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 运动追踪 ViewModel
 * 
 * @param addExerciseRecordUseCase 添加运动记录用例
 * @param getExerciseRecordsUseCase 获取运动记录用例
 * @param calculateCaloriesBurnedUseCase 计算消耗热量用例
 * @param getExerciseTypesUseCase 获取运动类型用例
 */
@HiltViewModel
class FitnessViewModel @Inject constructor(
    private val addExerciseRecordUseCase: AddExerciseRecordUseCase,
    private val getExerciseRecordsUseCase: GetExerciseRecordsUseCase,
    private val calculateCaloriesBurnedUseCase: CalculateCaloriesBurnedUseCase,
    private val getExerciseTypesUseCase: GetExerciseTypesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FitnessUiState())
    val uiState: StateFlow<FitnessUiState> = _uiState.asStateFlow()
    
    init {
        loadExerciseTypes()
        loadTodayRecords()
    }
    
    /**
     * 加载运动类型列表
     */
    private fun loadExerciseTypes() {
        viewModelScope.launch {
            val types = getExerciseTypesUseCase()
            _uiState.update { it.copy(exerciseTypes = types) }
        }
    }
    
    /**
     * 加载今日运动记录
     */
    private fun loadTodayRecords() {
        viewModelScope.launch {
            getExerciseRecordsUseCase.getByDate(LocalDate.now()).collectLatest { records ->
                val totalCalories = records.sumOf { it.caloriesBurned.toDouble() }.toFloat()
                _uiState.update { 
                    it.copy(
                        todayRecords = records,
                        totalCaloriesBurned = totalCalories
                    )
                }
            }
        }
    }
    
    /**
     * 处理 UI 事件
     */
    fun onEvent(event: FitnessUiEvent) {
        when (event) {
            is FitnessUiEvent.OnAddRecord -> addRecord(event.record)
            is FitnessUiEvent.OnDeleteRecord -> deleteRecord(event.recordId)
            is FitnessUiEvent.OnShowAddDialog -> showAddDialog()
            is FitnessUiEvent.OnDismissAddDialog -> dismissAddDialog()
            is FitnessUiEvent.OnCalculateCalories -> calculateCalories(event.exerciseType, event.durationMinutes, event.weightKg)
        }
    }
    
    /**
     * 添加运动记录
     */
    private fun addRecord(record: ExerciseRecord) {
        viewModelScope.launch {
            addExerciseRecordUseCase(record)
            dismissAddDialog()
        }
    }
    
    /**
     * 删除运动记录
     */
    private fun deleteRecord(recordId: String) {
        viewModelScope.launch {
            // TODO: Implement delete in repository
        }
    }
    
    /**
     * 显示添加对话框
     */
    private fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true) }
    }
    
    /**
     * 关闭添加对话框
     */
    private fun dismissAddDialog() {
        _uiState.update { it.copy(showAddDialog = false) }
    }
    
    /**
     * 计算消耗热量
     */
    private fun calculateCalories(exerciseType: ExerciseType, durationMinutes: Int, weightKg: Float) {
        val calories = calculateCaloriesBurnedUseCase(exerciseType.metValue, durationMinutes, weightKg)
        _uiState.update { it.copy(calculatedCalories = calories) }
    }
}

/**
 * 运动追踪 UI 状态
 * 
 * @param exerciseTypes 运动类型列表
 * @param todayRecords 今日运动记录
 * @param totalCaloriesBurned 今日总消耗热量
 * @param showAddDialog 是否显示添加对话框
 * @param calculatedCalories 计算出的消耗热量
 */
data class FitnessUiState(
    val exerciseTypes: List<ExerciseType> = emptyList(),
    val todayRecords: List<ExerciseRecord> = emptyList(),
    val totalCaloriesBurned: Float = 0f,
    val showAddDialog: Boolean = false,
    val calculatedCalories: Float = 0f
)

/**
 * 运动追踪 UI 事件
 */
sealed class FitnessUiEvent {
    data class OnAddRecord(val record: ExerciseRecord) : FitnessUiEvent()
    data class OnDeleteRecord(val recordId: String) : FitnessUiEvent()
    object OnShowAddDialog : FitnessUiEvent()
    object OnDismissAddDialog : FitnessUiEvent()
    data class OnCalculateCalories(
        val exerciseType: ExerciseType,
        val durationMinutes: Int,
        val weightKg: Float
    ) : FitnessUiEvent()
}
