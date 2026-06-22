package com.mealplanner.mealplan.domain.model

/**
 * 食材领域模型
 * 
 * @param foodName 食材名称
 * @param amount 数量
 * @param unit 单位
 */
data class Ingredient(
    val foodName: String,
    val amount: Float,
    val unit: String
)
