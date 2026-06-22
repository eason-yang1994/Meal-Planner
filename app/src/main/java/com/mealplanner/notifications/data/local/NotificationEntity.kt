package com.mealplanner.notifications.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 通知 Entity
 * 
 * @param id 唯一标识
 * @param type 通知类型
 * @param title 标题
 * @param message 消息内容
 * @param scheduledTime 计划发送时间
 * @param isSent 是否已发送
 * @param createdAt 创建时间
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey
    val id: String,
    val type: String,  // NotificationType enum name
    val title: String,
    val message: String,
    val scheduledTime: LocalDateTime,
    val isSent: Boolean = false,
    val createdAt: LocalDateTime
)

/**
 * 通知类型枚举
 */
enum class NotificationType {
    MEAL_REMINDER,        // 用餐提醒
    SHOPPING_REMINDER,    // 购物提醒
    EXPIRY_ALERT,         // 保质期预警
    WEIGHT_REMINDER,      // 体重记录提醒
    EXERCISE_REMINDER,    // 运动提醒
    GOAL_ACHIEVED,        // 目标达成
    STREAK_REMINDER,      // 连续记录提醒
    BIOMETRIC_REMINDER,   // 生物指标提醒
    EXPORT_REMINDER,      // 数据导出提醒
    INVENTORY_LOW_ALERT,  // 库存不足预警
    RECIPE_SUGGESTION     // 菜谱推荐
}
