package com.mealplanner.inventory.domain.usecase

import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 标记库存项已消耗用例
 * 
 * @param inventoryRepository 库存仓库
 */
@Singleton
class MarkItemConsumedUseCase @Inject constructor(
    private val inventoryRepository: InventoryRepository
) {
    
    /**
     * 执行标记库存项已消耗
     * 
     * @param id 库存项ID
     * @param consumed 是否已消耗
     * @return 操作结果
     */
    suspend operator fun invoke(id: String, consumed: Boolean = true): Result<Unit> {
        return inventoryRepository.markAsConsumed(id, consumed)
    }
}
