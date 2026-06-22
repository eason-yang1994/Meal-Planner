package com.mealplanner.mealplan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 三餐计划数据访问对象
 * 
 * 定义三餐计划表的数据库操作
 */
@Dao
interface MealPlanDao {
    
    /**
     * 插入三餐计划
     * 
     * @param meal 三餐计划
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: MealPlanEntity)
    
    /**
     * 更新三餐计划
     * 
     * @param meal 三餐计划
     */
    @Update
    suspend fun update(meal: MealPlanEntity)
    
    /**
     * 删除三餐计划
     * 
     * @param meal 三餐计划
     */
    @Delete
    suspend fun delete(meal: MealPlanEntity)
    
    /**
     * 根据日期获取三餐计划
     * 
     * @param date 日期
     * @return 三餐计划流
     */
    @Query("SELECT * FROM meal_plans WHERE date = :date ORDER BY mealType")
    fun getByDate(date: LocalDate): Flow<List<MealPlanEntity>>
    
    /**
     * 根据日期和餐类型获取三餐计划
     * 
     * @param date 日期
     * @param mealType 餐类型
     * @return 三餐计划
     */
    @Query("SELECT * FROM meal_plans WHERE date = :date AND mealType = :mealType")
    suspend fun getByDateAndMealType(date: LocalDate, mealType: String): MealPlanEntity?
    
    /**
     * 获取已确认的三餐计划
     * 
     * @return 三餐计划流
     */
    @Query("SELECT * FROM meal_plans WHERE status = 'CONFIRMED' ORDER BY date DESC, mealType")
    fun getConfirmedMeals(): Flow<List<MealPlanEntity>>
    
    /**
     * 获取最近的三餐计划
     * 
     * @param days 天数
     * @return 三餐计划流
     */
    @Query("SELECT * FROM meal_plans WHERE date >= :startDate ORDER BY date DESC, mealType")
    fun getRecentMeals(startDate: LocalDate): Flow<List<MealPlanEntity>>
    
    /**
     * 根据ID获取三餐计划
     * 
     * @param id ID
     * @return 三餐计划
     */
    @Query("SELECT * FROM meal_plans WHERE id = :id")
    suspend fun getById(id: String): MealPlanEntity?
}
