package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 添加库存项用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class AddInventoryItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 执行添加库存项
     * 
     * @param item 库存项
     * @return 操作结果
     */
    suspend operator fun invoke(item: com.mealplanner.inventory.domain.model.InventoryItem): Result<Unit> {
        return inventoryRepository.addItem(item)
    }
}
