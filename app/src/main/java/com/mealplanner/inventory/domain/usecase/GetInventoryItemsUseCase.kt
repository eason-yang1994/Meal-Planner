package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * 获取库存项用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class GetInventoryItemsUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 获取所有未消耗的库存项
     * 
     * @return 库存项流
     */
    operator fun invoke(): Flow<List<com.mealplanner.inventory.domain.model.InventoryItem>> {
        return inventoryRepository.getAllItems()
    }
    
    /**
     * 获取按保质期排序的库存项
     * 
     * @return 库存项流
     */
    fun getByExpiry(): Flow<List<com.mealplanner.inventory.domain.model.InventoryItem>> {
        return inventoryRepository.getItemsByExpiry()
    }
}
