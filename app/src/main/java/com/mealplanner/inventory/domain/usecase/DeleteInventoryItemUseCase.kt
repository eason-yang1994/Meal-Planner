package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 删除库存项用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class DeleteInventoryItemUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 执行删除库存项
     * 
     * @param id 库存项ID
     * @return 操作结果
     */
    suspend operator fun invoke(id: String): Result<Unit> {
        return inventoryRepository.deleteItem(id)
    }
}
