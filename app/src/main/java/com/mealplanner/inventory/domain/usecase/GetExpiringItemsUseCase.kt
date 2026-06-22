package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * 获取即将过期库存项用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class GetExpiringItemsUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 获取即将过期的库存项
     * 
     * @param days 天数（从今天起）
     * @return 库存项流
     */
    operator fun invoke(days: Int = 3): Flow<List<com.mealplanner.inventory.domain.model.InventoryItem>> {
        return inventoryRepository.getExpiringItems(days)
    }
}
