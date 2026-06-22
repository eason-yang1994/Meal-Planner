package com.mealplanner.reports.domain.model

import java.time.LocalDate

/**
 * 日报模型
 * 
 * @param date 日期
 * @param totalCalories 总摄入热量
 * @param totalProtein 总蛋白质摄入（克）
 * @param totalCarbs 总碳水化合物摄入（克）
 * @param totalFat 总脂肪摄入（克）
 * @param exerciseCaloriesBurned 运动消耗热量
 * @param weight 体重（公斤，可选）
 * @param bmi BMI（可选）
 */
data class DailyReport(
    val date: LocalDate,
    val totalCalories: Float,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val exerciseCaloriesBurned: Float,
    val weight: Float? = null,
    val bmi: Float? = null
)
