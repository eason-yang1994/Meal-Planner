package com.mealplanner.inventory.domain.model

import java.time.LocalDate

/**
 * 库存物品领域模型
 * 
 * @param id 唯一标识
 * @param foodName 食材名称
 * @param category 食材分类
 * @param quantity 数量
 * @param unit 单位
 * @param purchaseDate 购买日期
 * @param expiryDate 保质期到期日
 * @param storageLocation 存放位置
 * @param createdFrom 创建来源
 * @param isConsumed 是否已消耗
 * @param note 备注
 */
data class InventoryItem(
    val id: String,
    val foodName: String,
    val category: FoodCategory,
    val quantity: Float,
    val unit: String,
    val purchaseDate: LocalDate,
    val expiryDate: LocalDate,
    val storageLocation: StorageLocation,
    val createdFrom: StockSource,
    val isConsumed: Boolean = false,
    val note: String? = null
)
