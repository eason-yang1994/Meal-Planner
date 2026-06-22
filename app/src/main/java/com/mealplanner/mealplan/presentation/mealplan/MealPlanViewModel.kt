package com.mealplanner.mealplan.presentation.mealplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.model.MealType
import com.mealplanner.mealplan.domain.usecase.ConfirmMealUseCase
import com.mealplanner.mealplan.domain.usecase.GenerateMealPlanUseCase
import com.mealplanner.mealplan.domain.usecase.GetMealPlansForDateUseCase
import com.mealplanner.mealplan.domain.usecase.GetRecentMealsUseCase
import com.mealplanner.mealplan.domain.usecase.ModifyMealUseCase
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * 三餐规划页面 ViewModel
 * 
 * @param getMealPlansForDateUseCase 获取指定日期三餐用例
 * @param generateMealPlanUseCase 生成三餐计划用例
 * @param confirmMealUseCase 确认用餐用例
 * @param modifyMealUseCase 修改用餐用例
 * @param getUserProfileUseCase 获取用户资料用例
 */
@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val getMealPlansForDateUseCase: GetMealPlansForDateUseCase,
    private val generateMealPlanUseCase: GenerateMealPlanUseCase,
    private val confirmMealUseCase: ConfirmMealUseCase,
    private val modifyMealUseCase: ModifyMealUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MealPlanUiState())
    val uiState: StateFlow<MealPlanUiState> = _uiState.asStateFlow()
    
    init {
        loadTodaysMealPlans()
    }
    
    /**
     * 处理 UI 事件
     * 
     * @param event UI 事件
     */
    fun onEvent(event: MealPlanUiEvent) {
        when (event) {
            is MealPlanUiEvent.OnGenerateMealPlan -> generateMealPlan()
            is MealPlanUiEvent.OnConfirmMeal -> confirmMeal(event.mealId, event.isHomeMeal)
            is MealPlanUiEvent.OnModifyMeal -> modifyMeal(event.mealId, event.newIngredients)
            is MealPlanUiEvent.OnTakeout -> confirmMeal(event.mealId, false)
            is MealPlanUiEvent.OnDismissDialog -> dismissDialog()
        }
    }
    
    /**
     * 加载今日三餐计划
     */
    private fun loadTodaysMealPlans() {
        getMealPlansForDateUseCase(LocalDate.now())
            .onEach { meals ->
                _uiState.value = _uiState.value.copy(
                    mealPlansForToday = meals,
                    isLoading = false
                )
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * 生成三餐计划
     */
    private fun generateMealPlan() {
        _uiState.value = _uiState.value.copy(
            isGenerating = true,
            errorMessage = null
        )
        
        viewModelScope.launch {
            try {
                val userProfile = getUserProfileUseCase().first()
                if (userProfile != null) {
                    val result = generateMealPlanUseCase(userProfile, LocalDate.now())
                    if (result.isSuccess) {
                        // 重新加载
                        loadTodaysMealPlans()
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.exceptionOrNull()?.message,
                            isGenerating = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "请先设置用户资料",
                        isGenerating = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message,
                    isGenerating = false
                )
            }
        }
    }
    
    /**
     * 确认用餐
     * 
     * @param mealId 三餐计划ID
     * @param isHomeMeal 是否在家用餐
     */
    private fun confirmMeal(mealId: String, isHomeMeal: Boolean) {
        viewModelScope.launch {
            val result = confirmMealUseCase(mealId, isHomeMeal)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 修改用餐
     * 
     * @param mealId 三餐计划ID
     * @param newIngredients 新的食材列表
     */
    private fun modifyMeal(mealId: String, newIngredients: List<com.mealplanner.mealplan.domain.model.Ingredient>) {
        viewModelScope.launch {
            val result = modifyMealUseCase(mealId, newIngredients)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 关闭对话框
     */
    private fun dismissDialog() {
        _uiState.value = _uiState.value.copy(
            showConfirmDialog = false,
            selectedMeal = null
        )
    }
}

/**
 * 三餐规划页面 UI 状态
 * 
 * @param mealPlansForToday 今日三餐列表
 * @param isGenerating 是否正在生成
 * @param showConfirmDialog 是否显示确认对话框
 * @param selectedMeal 选中的三餐
 * @param isLoading 是否正在加载
 * @param errorMessage 错误信息
 */
data class MealPlanUiState(
    val mealPlansForToday: List<MealPlan> = emptyList(),
    val isGenerating: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val selectedMeal: MealPlan? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * 三餐规划页面 UI 事件
 */
sealed class MealPlanUiEvent {
    /**
     * 生成三餐计划
     */
    object OnGenerateMealPlan : MealPlanUiEvent()
    
    /**
     * 确认用餐
     */
    data class OnConfirmMeal(val mealId: String, val isHomeMeal: Boolean) : MealPlanUiEvent()
    
    /**
     * 修改用餐
     */
    data class OnModifyMeal(val mealId: String, val newIngredients: List<com.mealplanner.mealplan.domain.model.Ingredient>) : MealPlanUiEvent()
    
    /**
     * 外卖
     */
    data class OnTakeout(val mealId: String) : MealPlanUiEvent()
    
    /**
     * 关闭对话框
     */
    object OnDismissDialog : MealPlanUiEvent()
}
