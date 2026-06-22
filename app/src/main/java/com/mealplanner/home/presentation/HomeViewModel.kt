package com.mealplanner.home.presentation

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
 * 首页 ViewModel
 * 
 * 管理首页的状态
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
    /**
     * 加载用户资料
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                _uiState.value = _uiState.value.copy(
                    userProfile = profile,
                    isProfileLoaded = true
                )
            }
        }
    }
}

/**
 * 首页 UI 状态
 * 
 * @param userProfile 用户资料（可选）
 * @param isProfileLoaded 用户资料是否已加载
 */
data class HomeUiState(
    val userProfile: com.mealplanner.userprofile.domain.model.UserProfile? = null,
    val isProfileLoaded: Boolean = false
)
