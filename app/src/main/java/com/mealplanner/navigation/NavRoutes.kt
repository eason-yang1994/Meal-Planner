package com.mealplanner.navigation

/**
 * 导航路由定义
 */
object NavRoutes {
    // 主页
    const val HOME = "home"
    
    // 用户设置
    const val SETUP_WIZARD = "setup-wizard"
    const val PROFILE = "profile"
    
    // 体重追踪
    const val WEIGHT = "weight"
    const val WEIGHT_ADD = "weight/add"
    const val WEIGHT_CHART = "weight/chart"
    
    // 食物库存
    const val INVENTORY = "inventory"
    const val INVENTORY_ADD = "inventory/add"
    const val INVENTORY_SCAN = "inventory/scan"
    const val INVENTORY_IMPORT = "inventory/import"
    
    // 三餐规划
    const val MEAL_PLAN = "meal-plan"
    const val MEAL_DETAIL = "meal-plan/{date}/{type}"
    const val MEAL_MODIFY = "meal-plan/{date}/{type}/modify"
    
    // 购物清单
    const val SHOPPING = "shopping"
    
    // 运动追踪
    const val FITNESS = "fitness"
    const val FITNESS_RECORD = "fitness/record"
    const val FITNESS_CONNECT = "fitness/connect"
    
    // 报告
    const val REPORTS = "reports"
    const val REPORT_DETAIL = "reports/{type}/{period}"
    
    // 设置
    const val SETTINGS = "settings"
    const val LLM_SETTINGS = "settings/llm"
    const val NOTIFICATION_SETTINGS = "settings/notifications"
    const val DATA_MANAGEMENT = "settings/data"
    
    /**
     * 构建餐详情路由
     */
    fun mealDetail(date: Long, type: String): String {
        return "meal-plan/$date/$type"
    }
    
    /**
     * 构建餐修改路由
     */
    fun mealModify(date: Long, type: String): String {
        return "meal-plan/$date/$type/modify"
    }
    
    /**
     * 构建报告详情路由
     */
    fun reportDetail(type: String, period: String): String {
        return "reports/$type/$period"
    }
}