package com.mealplanner.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 个人资料 ViewModel
 * 
 * @param getUserProfileUseCase 获取用户资料用例
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
    /**
     * 加载用户资料
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            val profile = getUserProfileUseCase()
            _uiState.update { it.copy(userProfile = profile) }
        }
    }
    
    /**
     * 处理 UI 事件
     */
    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnClearProfile -> clearProfile()
        }
    }
    
    /**
     * 清除用户资料
     */
    private fun clearProfile() {
        viewModelScope.launch {
            // TODO: Clear profile from DataStore/Database
            // This will cause the app to show SetupWizard on next launch
        }
    }
}

/**
 * 个人资料 UI 状态
 * 
 * @param userProfile 用户资料
 */
data class ProfileUiState(
    val userProfile: com.mealplanner.userprofile.domain.model.UserProfile? = null
)

/**
 * 个人资料 UI 事件
 */
sealed class ProfileUiEvent {
    object OnClearProfile : ProfileUiEvent()
}
