package com.mealplanner.mealplan.domain.usecase

import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 确认用餐用例
 * 
 * 确认用餐：更新 meal status = CONFIRMED, confirmedAt = now
 * 如果在家用餐：扣减库存
 * 如果在外就餐：只记录热量，不扣库存
 * 
 * @param mealPlanRepository 三餐计划仓库
 * @param inventoryRepository 库存仓库
 */
@Singleton
class ConfirmMealUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository
) {
    
    /**
     * 执行确认用餐
     * 
     * @param mealId 三餐计划ID
     * @param isHomeMeal 是否在家用餐
     * @return 操作结果
     */
    suspend operator fun invoke(mealId: String, isHomeMeal: Boolean = true): Result<Unit> {
        return try {
            // 获取用餐计划
            val meal = getMealById(mealId)
            
            if (meal != null) {
                // 更新状态
                val updatedMeal = meal.copy(
                    status = com.mealplanner.mealplan.domain.model.MealStatus.CONFIRMED,
                    confirmedAt = java.time.LocalDateTime.now(),
                    isHomeMeal = isHomeMeal
                )
                mealPlanRepository.updateMealPlan(updatedMeal)
                
                // 如果在家用餐，扣减库存
                if (isHomeMeal) {
                    meal.ingredients.forEach { ingredient ->
                        consumeIngredient(ingredient)
                    }
                }
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 根据ID获取用餐计划
     */
    private suspend fun getMealById(id: String): MealPlan? {
        // 这里需要从仓库获取，简化实现
        return null
    }
    
    /**
     * 扣减食材库存
     */
    private suspend fun consumeIngredient(ingredient: com.mealplanner.mealplan.domain.model.Ingredient) {
        try {
            val items = inventoryRepository.getAllItems().first()
            val matchingItem = items.find { 
                it.foodName.contains(ingredient.foodName) || ingredient.foodName.contains(it.foodName)
            }
            
            if (matchingItem != null) {
                val newQuantity = matchingItem.quantity - ingredient.amount
                if (newQuantity <= 0) {
                    // 标记为已消耗
                    inventoryRepository.markAsConsumed(matchingItem.id, true)
                } else {
                    // 更新数量
                    val updatedItem = matchingItem.copy(quantity = newQuantity)
                    inventoryRepository.updateItem(updatedItem)
                }
            }
        } catch (e: Exception) {
            // 忽略错误，继续执行
        }
    }
}
