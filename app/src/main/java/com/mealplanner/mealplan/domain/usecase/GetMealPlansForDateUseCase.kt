package com.mealplanner.mealplan.domain.usecase

import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

/**
 * 获取指定日期三餐计划用例
 * 
 * @param mealPlanRepository 三餐计划仓库
 */
@Singleton
class GetMealPlansForDateUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    
    /**
     * 执行获取指定日期的三餐计划
     * 
     * @param date 日期
     * @return 三餐计划流
     */
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<List<com.mealplanner.mealplan.domain.model.MealPlan>> {
        return mealPlanRepository.getMealPlansForDate(date)
    }
}
