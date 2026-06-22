package com.mealplanner.inventory.domain.repository

import com.mealplanner.inventory.domain.model.InventoryItem
import kotlinx.coroutines.flow.Flow

/**
 * 库存仓库接口
 * 
 * 定义库存相关的数据操作
 */
interface InventoryRepository {
    
    /**
     * 添加库存项
     * 
     * @param item 库存项
     * @return 操作结果
     */
    suspend fun addItem(item: InventoryItem): Result<Unit>
    
    /**
     * 更新库存项
     * 
     * @param item 库存项
     * @return 操作结果
     */
    suspend fun updateItem(item: InventoryItem): Result<Unit>
    
    /**
     * 删除库存项
     * 
     * @param id 库存项ID
     * @return 操作结果
     */
    suspend fun deleteItem(id: String): Result<Unit>
    
    /**
     * 获取所有未消耗的库存项
     * 
     * @return 库存项流
     */
    fun getAllItems(): Flow<List<InventoryItem>>
    
    /**
     * 获取按保质期排序的库存项
     * 
     * @return 库存项流
     */
    fun getItemsByExpiry(): Flow<List<InventoryItem>>
    
    /**
     * 标记库存项为已消耗/未消耗
     * 
     * @param id 库存项ID
     * @param consumed 是否已消耗
     * @return 操作结果
     */
    suspend fun markAsConsumed(id: String, consumed: Boolean): Result<Unit>
    
    /**
     * 获取即将过期的库存项
     * 
     * @param days 天数（从今天起）
     * @return 库存项流
     */
    fun getExpiringItems(days: Int): Flow<List<InventoryItem>>
}
