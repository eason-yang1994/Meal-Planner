package com.mealplanner.mealplan.domain.usecase

import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * 获取最近用餐记录用例
 * 
 * @param mealPlanRepository 三餐计划仓库
 */
@Singleton
class GetRecentMealsUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    
    /**
     * 执行获取最近的用餐记录
     * 
     * @param days 天数（默认7天）
     * @return 用餐记录流
     */
    operator fun invoke(days: Int = 7): Flow<List<com.mealplanner.mealplan.domain.model.MealPlan>> {
        return mealPlanRepository.getRecentMeals(days)
    }
}
