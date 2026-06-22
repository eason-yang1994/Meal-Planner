package com.mealplanner.notifications.domain.repository

import com.mealplanner.notifications.data.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 通知 Repository 接口
 * 
 * 定义通知的数据操作
 */
interface NotificationRepository {
    
    /**
     * 调度通知
     * 
     * @param notification 通知实体
     * @return 操作结果
     */
    suspend fun scheduleNotification(notification: NotificationEntity): Result<Unit>
    
    /**
     * 取消通知
     * 
     * @param id 通知ID
     * @return 操作结果
     */
    suspend fun cancelNotification(id: String): Result<Unit>
    
    /**
     * 获取待发送通知
     * 
     * @return 待发送通知流
     */
    fun getPendingNotifications(): Flow<List<NotificationEntity>>
    
    /**
     * 标记为已发送
     * 
     * @param id 通知ID
     * @return 操作结果
     */
    suspend fun markAsSent(id: String): Result<Unit>
}
