package com.mealplanner.userprofile.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 用户资料领域模型
 * 
 * @param id 唯一标识，默认为 "default"
 * @param gender 性别
 * @param birthDate 出生日期
 * @param heightCm 身高（厘米）
 * @param initialWeightKg 初始体重（公斤）
 * @param activityLevel 活动水平
 * @param goal 饮食目标
 * @param targetWeightKg 目标体重（公斤，可选）
 * @param createdAt 创建时间
 * @param lastExportedAt 最后导出时间（可选）
 */
data class UserProfile(
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
