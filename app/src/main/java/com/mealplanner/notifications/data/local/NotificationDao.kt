package com.mealplanner.notifications.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

/**
 * 通知 DAO
 * 
 * 提供通知的数据访问接口
 */
@Dao
interface NotificationDao {
    
    /**
     * 插入通知
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity)
    
    /**
     * 更新通知
     */
    @Update
    suspend fun update(notification: NotificationEntity)
    
    /**
     * 删除通知
     */
    @Delete
    suspend fun delete(notification: NotificationEntity)
    
    /**
     * 获取待发送通知
     */
    @Query("SELECT * FROM notifications WHERE isSent = 0 AND scheduledTime <= :currentTime ORDER BY scheduledTime ASC")
    fun getPendingNotifications(currentTime: LocalDateTime): Flow<List<NotificationEntity>>
    
    /**
     * 获取已发送通知
     */
    @Query("SELECT * FROM notifications WHERE isSent = 1 ORDER BY scheduledTime DESC")
    fun getSentNotifications(): Flow<List<NotificationEntity>>
    
    /**
     * 标记为已发送
     */
    @Query("UPDATE notifications SET isSent = 1 WHERE id = :id")
    suspend fun markAsSent(id: String)
}
