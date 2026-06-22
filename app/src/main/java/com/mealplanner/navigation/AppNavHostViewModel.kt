package com.mealplanner.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * AppNavHost ViewModel
 * 
 * 用于检查用户资料是否存在
 */
@HiltViewModel
class AppNavHostViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppNavHostUiState())
    val uiState: StateFlow<AppNavHostUiState> = _uiState.asStateFlow()
    
    init {
        checkUserProfile()
    }
    
    /**
     * 检查用户资料是否存在
     */
    private fun checkUserProfile() {
        viewModelScope.launch {
            try {
                val profile = getUserProfileUseCase().first()
                _uiState.value = _uiState.value.copy(
                    isProfileExists = profile != null,
                    isProfileChecked = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProfileExists = false,
                    isProfileChecked = true
                )
            }
        }
    }
}

/**
 * AppNavHost UI 状态
 */
data class AppNavHostUiState(
    val isProfileExists: Boolean = false,
    val isProfileChecked: Boolean = false
)
