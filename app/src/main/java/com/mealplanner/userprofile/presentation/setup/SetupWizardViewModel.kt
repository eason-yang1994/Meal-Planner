package com.mealplanner.userprofile.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.userprofile.domain.model.ActivityLevel
import com.mealplanner.userprofile.domain.model.DietGoal
import com.mealplanner.userprofile.domain.model.Gender
import com.mealplanner.userprofile.domain.model.UserProfile
import com.mealplanner.userprofile.domain.usecase.CalculateBMRUseCase
import com.mealplanner.userprofile.domain.usecase.CalculateTDEEUseCase
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import com.mealplanner.userprofile.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * 设置向导 ViewModel
 * 
 * 管理设置向导的状态和事件
 */
@HiltViewModel
class SetupWizardViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val calculateBMRUseCase: CalculateBMRUseCase,
    private val calculateTDEEUseCase: CalculateTDEEUseCase
) : ViewModel() {
    
    // UI 状态
    private val _uiState = MutableStateFlow(SetupWizardUiState())
    val uiState: StateFlow<SetupWizardUiState> = _uiState.asStateFlow()
    
    // 单次事件
    private val _uiEffect = MutableSharedFlow<SetupWizardUiEffect>()
    val uiEffect: SharedFlow<SetupWizardUiEffect> = _uiEffect.asSharedFlow()
    
    init {
        checkIfProfileExists()
    }
    
    /**
     * 处理事件
     * 
     * @param event 用户事件
     */
    fun onEvent(event: SetupWizardUiEvent) {
        when (event) {
            is SetupWizardUiEvent.OnGenderChange -> {
                _uiState.value = _uiState.value.copy(gender = event.gender)
            }
            is SetupWizardUiEvent.OnBirthDateChange -> {
                _uiState.value = _uiState.value.copy(birthDate = event.birthDate)
            }
            is SetupWizardUiEvent.OnHeightChange -> {
                _uiState.value = _uiState.value.copy(height = event.height)
            }
            is SetupWizardUiEvent.OnWeightChange -> {
                _uiState.value = _uiState.value.copy(weight = event.weight)
            }
            is SetupWizardUiEvent.OnActivityLevelChange -> {
                _uiState.value = _uiState.value.copy(activityLevel = event.activityLevel)
            }
            is SetupWizardUiEvent.OnGoalChange -> {
                _uiState.value = _uiState.value.copy(goal = event.goal)
            }
            is SetupWizardUiEvent.OnTargetWeightChange -> {
                _uiState.value = _uiState.value.copy(targetWeight = event.targetWeight)
            }
            SetupWizardUiEvent.OnNextStep -> {
                if (canProceedToNextStep()) {
                    _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep + 1)
                }
            }
            SetupWizardUiEvent.OnPreviousStep -> {
                if (_uiState.value.currentStep > 1) {
                    _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep - 1)
                }
            }
            SetupWizardUiEvent.OnSave -> {
                saveProfile()
            }
        }
    }
    
    /**
     * 检查用户资料是否已存在
     */
    private fun checkIfProfileExists() {
        viewModelScope.launch {
            try {
                val profile = getUserProfileUseCase().first()
                profile?.let {
                    _uiState.value = _uiState.value.copy(
                        gender = it.gender,
                        birthDate = it.birthDate,
                        height = it.heightCm,
                        weight = it.initialWeightKg,
                        activityLevel = it.activityLevel,
                        goal = it.goal,
                        targetWeight = it.targetWeightKg ?: 0f
                    )
                }
            } catch (e: Exception) {
                // 忽略错误，视为新用户
            }
        }
    }
    
    /**
     * 检查是否可以进入下一步
     * 
     * @return 如果当前步骤已填写完整则返回 true
     */
    private fun canProceedToNextStep(): Boolean {
        return when (_uiState.value.currentStep) {
            1 -> _uiState.value.gender != null && _uiState.value.birthDate != null
            2 -> _uiState.value.height > 0 && _uiState.value.weight > 0
            3 -> _uiState.value.activityLevel != null
            4 -> _uiState.value.goal != null
            else -> true
        }
    }
    
    /**
     * 保存用户资料
     */
    private fun saveProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val currentState = _uiState.value
            val profile = UserProfile(
                gender = currentState.gender ?: Gender.MALE,
                birthDate = currentState.birthDate ?: LocalDate.now(),
                heightCm = currentState.height,
                initialWeightKg = currentState.weight,
                activityLevel = currentState.activityLevel ?: ActivityLevel.MODERATE,
                goal = currentState.goal ?: DietGoal.MAINTAIN,
                targetWeightKg = if (currentState.targetWeight > 0) currentState.targetWeight else null
            )
            
            val result = saveUserProfileUseCase(profile)
            if (result.isSuccess) {
                _uiEffect.emit(SetupWizardUiEffect.NavigateToHome)
            } else {
                _uiEffect.emit(SetupWizardUiEffect.ShowError("保存失败，请重试"))
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}

/**
 * 设置向导 UI 状态
 * 
 * @param currentStep 当前步骤（1-5）
 * @param gender 性别
 * @param birthDate 出生日期
 * @param height 身高（厘米）
 * @param weight 体重（公斤）
 * @param activityLevel 活动水平
 * @param goal 饮食目标
 * @param targetWeight 目标体重（公斤）
 * @param isLoading 是否正在加载
 */
data class SetupWizardUiState(
    val currentStep: Int = 1,
    val gender: Gender? = null,
    val birthDate: LocalDate? = null,
    val height: Float = 0f,
    val weight: Float = 0f,
    val activityLevel: ActivityLevel? = null,
    val goal: DietGoal? = null,
    val targetWeight: Float = 0f,
    val isLoading: Boolean = false
)

/**
 * 设置向导 UI 事件
 */
sealed class SetupWizardUiEvent {
    data class OnGenderChange(val gender: Gender) : SetupWizardUiEvent()
    data class OnBirthDateChange(val birthDate: LocalDate) : SetupWizardUiEvent()
    data class OnHeightChange(val height: Float) : SetupWizardUiEvent()
    data class OnWeightChange(val weight: Float) : SetupWizardUiEvent()
    data class OnActivityLevelChange(val activityLevel: ActivityLevel) : SetupWizardUiEvent()
    data class OnGoalChange(val goal: DietGoal) : SetupWizardUiEvent()
    data class OnTargetWeightChange(val targetWeight: Float) : SetupWizardUiEvent()
    object OnNextStep : SetupWizardUiEvent()
    object OnPreviousStep : SetupWizardUiEvent()
    object OnSave : SetupWizardUiEvent()
}

/**
 * 设置向导 UI 效果（单次事件）
 */
sealed class SetupWizardUiEffect {
    object NavigateToHome : SetupWizardUiEffect()
    data class ShowError(val message: String) : SetupWizardUiEffect()
}
