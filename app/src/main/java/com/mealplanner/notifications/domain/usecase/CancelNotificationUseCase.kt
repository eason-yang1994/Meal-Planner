package com.mealplanner.notifications.domain.usecase

import com.mealplanner.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * 取消通知用例
 * 
 * @param notificationRepository 通知仓库
 */
class CancelNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    /**
     * 取消通知
     * 
     * @param id 通知ID
     * @return 操作结果
     */
    suspend operator fun invoke(id: String): Result<Unit> {
        return notificationRepository.cancelNotification(id)
    }
}
