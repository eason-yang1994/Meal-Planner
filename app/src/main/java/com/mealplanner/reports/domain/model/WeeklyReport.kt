package com.mealplanner.reports.domain.model

import java.time.LocalDate

/**
 * 周报模型
 * 
 * @param weekStartDate 周开始日期
 * @param averageCalories 平均每日摄入热量
 * @param averageWeight 平均体重
 * @param totalExerciseCalories 总运动消耗热量
 * @param bestDay 最佳日期（摄入最接近目标的日期）
 */
data class WeeklyReport(
    val weekStartDate: LocalDate,
    val averageCalories: Float,
    val averageWeight: Float,
    val totalExerciseCalories: Float,
    val bestDay: LocalDate? = null
)
