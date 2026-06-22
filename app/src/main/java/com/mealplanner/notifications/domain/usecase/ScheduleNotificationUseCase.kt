package com.mealplanner.notifications.domain.usecase

import com.mealplanner.notifications.data.local.NotificationEntity
import com.mealplanner.notifications.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * 调度通知用例
 * 
 * @param notificationRepository 通知仓库
 */
class ScheduleNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    /**
     * 调度通知
     * 
     * @param notification 通知实体
     * @return 操作结果
     */
    suspend operator fun invoke(notification: NotificationEntity): Result<Unit> {
        return notificationRepository.scheduleNotification(notification)
    }
}
