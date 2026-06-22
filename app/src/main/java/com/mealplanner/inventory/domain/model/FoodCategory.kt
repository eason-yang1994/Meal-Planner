package com.mealplanner.inventory.domain.model

/**
 * 食材分类枚举
 * 
 * 对应 JSON 数据中的 category 字段
 */
enum class FoodCategory {
    /**
     * 谷物
     */
    GRAIN,
    
    /**
     * 肉禽
     */
    MEAT,
    
    /**
     * 蔬菜
     */
    VEGETABLE,
    
    /**
     * 水果
     */
    FRUIT,
    
    /**
     * 乳制品
     */
    DAIRY,
    
    /**
     * 豆类/坚果
     */
    LEGUME,
    
    /**
     * 海鲜
     */
    SEAFOOD,
    
    /**
     * 调味品
     */
    CONDIMENT,
    
    /**
     * 零食
     */
    SNACK,
    
    /**
     * 饮料
     */
    BEVERAGE;
    
    /**
     * 获取中文显示名称
     * 
     * @return 中文名称
     */
    fun getDisplayName(): String {
        return when (this) {
            GRAIN -> "谷物"
            MEAT -> "肉禽"
            VEGETABLE -> "蔬菜"
            FRUIT -> "水果"
            DAIRY -> "乳制品"
            LEGUME -> "豆类/坚果"
            SEAFOOD -> "海鲜"
            CONDIMENT -> "调味品"
            SNACK -> "零食"
            BEVERAGE -> "饮料"
        }
    }
}
