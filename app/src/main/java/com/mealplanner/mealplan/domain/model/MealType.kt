package com.mealplanner.mealplan.domain.model

/**
 * 餐类型枚举
 */
enum class MealType {
    /**
     * 早餐
     */
    BREAKFAST,
    
    /**
     * 午餐
     */
    LUNCH,
    
    /**
     * 晚餐
     */
    DINNER,
    
    /**
     * 加餐
     */
    SNACK;
    
    /**
     * 获取中文显示名称
     * 
     * @return 中文名称
     */
    fun getDisplayName(): String {
        return when (this) {
            BREAKFAST -> "早餐"
            LUNCH -> "午餐"
            DINNER -> "晚餐"
            SNACK -> "加餐"
        }
    }
}
