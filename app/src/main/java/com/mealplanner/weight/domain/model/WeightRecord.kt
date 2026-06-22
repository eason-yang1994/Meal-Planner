package com.mealplanner.weight.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 体重记录领域模型
 * 
 * @param id 唯一标识
 * @param date 记录日期
 * @param weightKg 体重（公斤）
 * @param note 备注（可选）
 * @param createdAt 创建时间
 */
data class WeightRecord(
    val id: String,
    val date: LocalDate,
    val weightKg: Float,
    val note: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
