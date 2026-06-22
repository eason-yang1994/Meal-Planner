package com.mealplanner.userprofile.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 用户资料 Room 实体
 * 
 * @param id 主键，默认为 "default"
 * @param gender 性别（存储为 String）
 * @param birthDate 出生日期
 * @param heightCm 身高（厘米）
 * @param initialWeightKg 初始体重（公斤）
 * @param activityLevel 活动水平（存储为 String）
 * @param goal 饮食目标（存储为 String）
 * @param targetWeightKg 目标体重（公斤，可选）
 * @param createdAt 创建时间
 * @param lastExportedAt 最后导出时间（可选）
 */
@Entity(tableName = "user_profile")
@TypeConverters(Converters::class)
data class UserProfileEntity(
    @PrimaryKey
    val id: String = "default",
    val gender: Gender,
    val birthDate: LocalDate,
    val heightCm: Float,
    val initialWeightKg: Float,
    val activityLevel: ActivityLevel,
    val goal: DietGoal,
    val targetWeightKg: Float? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastExportedAt: LocalDateTime? = null
)
