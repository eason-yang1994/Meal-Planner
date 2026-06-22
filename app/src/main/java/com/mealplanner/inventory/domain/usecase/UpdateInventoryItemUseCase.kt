package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 更新库存项用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class UpdateInventoryItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 执行更新库存项
     * 
     * @param item 库存项
     * @return 操作结果
     */
    suspend operator fun invoke(item: com.mealplanner.inventory.domain.model.InventoryItem): Result<Unit> {
        return inventoryRepository.updateItem(item)
    }
}
