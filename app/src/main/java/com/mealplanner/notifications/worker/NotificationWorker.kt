package com.mealplanner.notifications.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mealplanner.notifications.data.local.NotificationEntity
import com.mealplanner.notifications.data.local.NotificationRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDateTime

/**
 * 通知 Worker
 * 
 * 检查待发送通知，使用 NotificationManagerCompat 发送系统通知
 * 使用 WorkManager 定期运行（每天检查 3 次）
 * 
 * @param context 应用上下文
 * @param params Worker 参数
 */
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    companion object {
        const val CHANNEL_ID = "meal_planner_notifications"
        const val CHANNEL_NAME = "三餐规划通知"
        const val WORK_NAME = "notification_worker"
    }
    
    override suspend fun doWork(): Result {
        return try {
            // 1. 获取待发送通知
            val repository = NotificationRepositoryImpl(
                notificationDao = TODO() // TODO: Inject via Hilt
            )
            
            val pendingNotifications = mutableListOf<NotificationEntity>()
            // TODO: Get pending notifications from repository
            
            // 2. 发送通知
            pendingNotifications.forEach { notification ->
                sendNotification(notification)
                // 标记为已发送
                // repository.markAsSent(notification.id)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    /**
     * 发送系统通知
     */
    private fun sendNotification(notification: NotificationEntity) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // 创建通知渠道（Android 8.0+）
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        
        // 构建通知
        val androidNotification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setAutoCancel(true)
            .build()
        
        // 发送通知
        notificationManager.notify(notification.id.hashCode(), androidNotification)
    }
}
