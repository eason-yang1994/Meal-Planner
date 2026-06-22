package com.mealplanner.userprofile.domain.model

/**
 * 活动水平枚举
 * 
 * @param label 显示标签
 * @param factor 活动系数
 */
enum class ActivityLevel(val label: String, val factor: Float) {
    SEDENTARY("久坐", 1.2f),
    LIGHT("轻度", 1.375f),
    MODERATE("中度", 1.55f),
    HEAVY("高度", 1.725f),
    EXTREME("极度", 1.9f)
}
