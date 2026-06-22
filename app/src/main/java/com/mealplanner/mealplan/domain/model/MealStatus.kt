package com.mealplanner.mealplan.domain.model

/**
 * 用餐状态枚举
 */
enum class MealStatus {
    /**
     * 待确认
     */
    PENDING,
    
    /**
     * 已确认
     */
    CONFIRMED,
    
    /**
     * 已修改
     */
    MODIFIED,
    
    /**
     * 外卖
     */
    TAKEOUT;
    
    /**
     * 获取中文显示名称
     * 
     * @return 中文名称
     */
    fun getDisplayName(): String {
        return when (this) {
            PENDING -> "待确认"
            CONFIRMED -> "已确认"
            MODIFIED -> "已修改"
            TAKEOUT -> "外卖"
        }
    }
}
