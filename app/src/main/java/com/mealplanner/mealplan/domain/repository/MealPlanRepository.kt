package com.mealplanner.mealplan.domain.repository

import com.mealplanner.mealplan.domain.model.MealPlan
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 三餐计划仓库接口
 * 
 * 定义三餐计划相关的数据操作
 */
interface MealPlanRepository {
    
    /**
     * 添加三餐计划
     * 
     * @param meal 三餐计划
     * @return 操作结果
     */
    suspend fun addMealPlan(meal: MealPlan): Result<Unit>
    
    /**
     * 更新三餐计划
     * 
     * @param meal 三餐计划
     * @return 操作结果
     */
    suspend fun updateMealPlan(meal: MealPlan): Result<Unit>
    
    /**
     * 删除三餐计划
     * 
     * @param id 三餐计划ID
     * @return 操作结果
     */
    suspend fun deleteMealPlan(id: String): Result<Unit>
    
    /**
     * 获取指定日期的三餐计划
     * 
     * @param date 日期
     * @return 三餐计划流
     */
    fun getMealPlansForDate(date: LocalDate): Flow<List<MealPlan>>
    
    /**
     * 获取最近的三餐计划
     * 
     * @param days 天数
     * @return 三餐计划流
     */
    fun getRecentMeals(days: Int): Flow<List<MealPlan>>
    
    /**
     * 确认三餐计划
     * 
     * @param id 三餐计划ID
     * @return 操作结果
     */
    suspend fun confirmMealPlan(id: String): Result<Unit>
}
