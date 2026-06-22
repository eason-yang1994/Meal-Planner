package com.mealplanner.inventory.domain.model

/**
 * 库存来源枚举
 */
enum class StockSource {
    /**
     * 手动录入
     */
    MANUAL,
    
    /**
     * 购物清单
     */
    SHOPPING,
    
    /**
     * 账单扫描
     */
    BILL_SCAN,
    
    /**
     * 条码扫描
     */
    BARCODE;
    
    /**
     * 获取中文显示名称
     * 
     * @return 中文名称
     */
    fun getDisplayName(): String {
        return when (this) {
            MANUAL -> "手动录入"
            SHOPPING -> "购物清单"
            BILL_SCAN -> "账单扫描"
            BARCODE -> "条码扫描"
        }
    }
}
