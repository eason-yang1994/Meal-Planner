package com.mealplanner.fitness.domain.repository

import com.mealplanner.fitness.domain.model.ExerciseRecord
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 运动追踪 Repository 接口
 * 
 * 定义运动记录的数据操作
 */
interface FitnessRepository {
    
    /**
     * 添加运动记录
     * 
     * @param record 运动记录
     * @return 操作结果
     */
    suspend fun addRecord(record: ExerciseRecord): Result<Unit>
    
    /**
     * 获取所有运动记录
     * 
     * @return 运动记录流
     */
    fun getRecords(): Flow<List<ExerciseRecord>>
    
    /**
     * 根据日期获取运动记录
     * 
     * @param date 日期
     * @return 运动记录流
     */
    fun getRecordsByDate(date: LocalDate): Flow<List<ExerciseRecord>>
    
    /**
     * 获取指定日期的总消耗热量
     * 
     * @param date 日期
     * @return 总消耗热量流
     */
    fun getTotalCaloriesBurned(date: LocalDate): Flow<Float>
    
    /**
     * 删除运动记录
     * 
     * @param id 记录ID
     * @return 操作结果
     */
    suspend fun deleteRecord(id: String): Result<Unit>
}
