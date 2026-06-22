package com.mealplanner.inventory.domain.model

/**
 * 存放位置枚举
 */
enum class StorageLocation {
    /**
     * 冷藏
     */
    REFRIGERATED,
    
    /**
     * 冷冻
     */
    FROZEN,
    
    /**
     *  pantry（食品储藏室）
     */
    PANTRY,
    
    /**
     * 台面（常温）
     */
    COUNTER;
    
    /**
     * 获取中文显示名称
     * 
     * @return 中文名称
     */
    fun getDisplayName(): String {
        return when (this) {
            REFRIGERATED -> "冷藏"
            FROZEN -> "冷冻"
            PANTRY -> "Pantry"
            COUNTER -> "台面"
        }
    }
}
