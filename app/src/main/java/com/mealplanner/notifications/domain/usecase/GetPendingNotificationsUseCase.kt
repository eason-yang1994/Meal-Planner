package com.mealplanner.notifications.domain.usecase

import com.mealplanner.notifications.data.local.NotificationEntity
import com.mealplanner.notifications.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 获取待发送通知用例
 * 
 * @param notificationRepository 通知仓库
 */
class GetPendingNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    /**
     * 获取待发送通知
     * 
     * @return 待发送通知流
     */
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return notificationRepository.getPendingNotifications()
    }
}
