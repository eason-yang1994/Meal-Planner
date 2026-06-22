package com.mealplanner.notifications.data

import com.mealplanner.notifications.data.local.NotificationDao
import com.mealplanner.notifications.data.local.NotificationEntity
import com.mealplanner.notifications.domain.repository.NotificationRepository
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 通知 Repository 实现
 * 
 * @param notificationDao 通知 DAO
 */
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    
    override suspend fun scheduleNotification(notification: NotificationEntity): Result<Unit> {
        return try {
            notificationDao.insert(notification)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun cancelNotification(id: String): Result<Unit> {
        return try {
            val notification = notificationDao.getPendingNotifications(LocalDateTime.now())
                .map { it.find { it.id == id } }
            // TODO: Implement proper delete
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getPendingNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getPendingNotifications(LocalDateTime.now())
    }
    
    override suspend fun markAsSent(id: String): Result<Unit> {
        return try {
            notificationDao.markAsSent(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
