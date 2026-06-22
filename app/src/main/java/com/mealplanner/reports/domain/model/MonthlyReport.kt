package com.mealplanner.reports.domain.model

/**
 * 月报模型
 * 
 * @param month 月份（格式：yyyy-MM）
 * @param averageCalories 平均每日摄入热量
 * @param weightChange 体重变化（公斤）
 * @param totalExerciseCalories 总运动消耗热量
 */
data class MonthlyReport(
    val month: String,
    val averageCalories: Float,
    val weightChange: Float,
    val totalExerciseCalories: Float
)
