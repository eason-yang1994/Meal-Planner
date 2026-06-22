package com.mealplanner.notifications.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.notifications.data.local.NotificationEntity
import com.mealplanner.notifications.domain.usecase.CancelNotificationUseCase
import com.mealplanner.notifications.domain.usecase.GetPendingNotificationsUseCase
import com.mealplanner.notifications.domain.usecase.ScheduleNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 通知 ViewModel
 * 
 * @param scheduleNotificationUseCase 调度通知用例
 * @param cancelNotificationUseCase 取消通知用例
 * @param getPendingNotificationsUseCase 获取待发送通知用例
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase,
    private val cancelNotificationUseCase: CancelNotificationUseCase,
    private val getPendingNotificationsUseCase: GetPendingNotificationsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()
    
    init {
        loadPendingNotifications()
    }
    
    /**
     * 加载待发送通知
     */
    private fun loadPendingNotifications() {
        viewModelScope.launch {
            getPendingNotificationsUseCase().collectLatest { notifications ->
                _uiState.update { it.copy(pendingNotifications = notifications) }
            }
        }
    }
    
    /**
     * 处理 UI 事件
     */
    fun onEvent(event: NotificationsUiEvent) {
        when (event) {
            is NotificationsUiEvent.OnScheduleNotification -> scheduleNotification(event.notification)
            is NotificationsUiEvent.OnCancelNotification -> cancelNotification(event.id)
        }
    }
    
    /**
     * 调度通知
     */
    private fun scheduleNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            scheduleNotificationUseCase(notification)
        }
    }
    
    /**
     * 取消通知
     */
    private fun cancelNotification(id: String) {
        viewModelScope.launch {
            cancelNotificationUseCase(id)
        }
    }
}

/**
 * 通知 UI 状态
 * 
 * @param pendingNotifications 待发送通知列表
 */
data class NotificationsUiState(
    val pendingNotifications: List<NotificationEntity> = emptyList()
)

/**
 * 通知 UI 事件
 */
sealed class NotificationsUiEvent {
    data class OnScheduleNotification(val notification: NotificationEntity) : NotificationsUiEvent()
    data class OnCancelNotification(val id: String) : NotificationsUiEvent()
}
