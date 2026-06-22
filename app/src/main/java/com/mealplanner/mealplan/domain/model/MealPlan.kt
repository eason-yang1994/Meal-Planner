package com.mealplanner.mealplan.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 三餐计划领域模型
 * 
 * @param id 唯一标识
 * @param date 日期
 * @param mealType 餐类型
 * @param dishName 菜名
 * @param calories 热量
 * @param ingredients 食材列表
 * @param status 状态
 * @param isHomeMeal 是否在家用餐
 * @param confirmedAt 确认时间
 */
data class MealPlan(
    val id: String,
    val date: LocalDate,
    val mealType: MealType,
    val dishName: String,
    val calories: Float,
    val ingredients: List<Ingredient>,
    val status: MealStatus = MealStatus.PENDING,
    val isHomeMeal: Boolean = true,
    val confirmedAt: LocalDateTime? = null
)
