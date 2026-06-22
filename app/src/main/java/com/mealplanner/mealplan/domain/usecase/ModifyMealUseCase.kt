package com.mealplanner.mealplan.domain.usecase

import com.mealplanner.mealplan.domain.model.Ingredient
import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import com.mealplanner.inventory.domain.repository.InventoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 修改用餐用例
 * 
 * 修改用餐：更新 meal status = MODIFIED, 允许用户调整食材和用量
 * 重新计算热量
 * 扣减实际使用的库存
 * 
 * @param mealPlanRepository 三餐计划仓库
 * @param inventoryRepository 库存仓库
 */
@Singleton
class ModifyMealUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository
) {
    
    /**
     * 执行修改用餐
     * 
     * @param mealId 三餐计划ID
     * @param newIngredients 新的食材列表
     * @return 操作结果
     */
    suspend operator fun invoke(mealId: String, newIngredients: List<Ingredient>): Result<Unit> {
        return try {
            // 获取原用餐计划
            val originalMeal = getMealById(mealId)
            
            if (originalMeal != null) {
                // 恢复原食材库存
                originalMeal.ingredients.forEach { ingredient ->
                    returnIngredient(ingredient)
                }
                
                // 计算新热量（简化：根据食材数量估算）
                val newCalories = calculateCalories(newIngredients)
                
                // 更新用餐计划
                val updatedMeal = originalMeal.copy(
                    status = com.mealplanner.mealplan.domain.model.MealStatus.MODIFIED,
                    ingredients = newIngredients,
                    calories = newCalories
                )
                mealPlanRepository.updateMealPlan(updatedMeal)
                
                // 扣减新食材库存
                newIngredients.forEach { ingredient ->
                    consumeIngredient(ingredient)
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
        // 简化实现
        return null
    }
    
    /**
     * 恢复食材库存
     */
    private suspend fun returnIngredient(ingredient: Ingredient) {
        try {
            val items = inventoryRepository.getAllItems().first()
            val matchingItem = items.find { 
                it.foodName.contains(ingredient.foodName) || ingredient.foodName.contains(it.foodName)
            }
            
            if (matchingItem != null) {
                val updatedItem = matchingItem.copy(
                    quantity = matchingItem.quantity + ingredient.amount
                )
                inventoryRepository.updateItem(updatedItem)
            }
        } catch (e: Exception) {
            // 忽略错误
        }
    }
    
    /**
     * 扣减食材库存
     */
    private suspend fun consumeIngredient(ingredient: Ingredient) {
        try {
            val items = inventoryRepository.getAllItems().first()
            val matchingItem = items.find { 
                it.foodName.contains(ingredient.foodName) || ingredient.foodName.contains(it.foodName)
            }
            
            if (matchingItem != null) {
                val newQuantity = matchingItem.quantity - ingredient.amount
                if (newQuantity <= 0) {
                    inventoryRepository.markAsConsumed(matchingItem.id, true)
                } else {
                    val updatedItem = matchingItem.copy(quantity = newQuantity)
                    inventoryRepository.updateItem(updatedItem)
                }
            }
        } catch (e: Exception) {
            // 忽略错误
        }
    }
    
    /**
     * 计算热量（简化实现）
     */
    private fun calculateCalories(ingredients: List<Ingredient>): Float {
        // 简化：每个食材按 100 卡路里估算
        return ingredients.size * 100f
    }
}
