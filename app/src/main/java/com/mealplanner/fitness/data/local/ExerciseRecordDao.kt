package com.mealplanner.fitness.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 运动记录 DAO
 * 
 * 提供运动记录的数据访问接口
 */
@Dao
interface ExerciseRecordDao {
    
    /**
     * 插入运动记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: ExerciseRecordEntity)
    
    /**
     * 获取所有运动记录
     */
    @Query("SELECT * FROM exercise_records ORDER BY date DESC")
    fun getAll(): Flow<List<ExerciseRecordEntity>>
    
    /**
     * 根据日期获取运动记录
     */
    @Query("SELECT * FROM exercise_records WHERE date = :date ORDER BY date DESC")
    fun getByDate(date: LocalDate): Flow<List<ExerciseRecordEntity>>
    
    /**
     * 根据日期范围获取运动记录
     */
    @Query("SELECT * FROM exercise_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<ExerciseRecordEntity>>
    
    /**
     * 删除运动记录
     */
    @Delete
    suspend fun delete(record: ExerciseRecordEntity)
    
    /**
     * 获取指定日期的总消耗热量
     */
    @Query("SELECT SUM(caloriesBurned) FROM exercise_records WHERE date = :date")
    suspend fun getTotalCaloriesBurned(date: LocalDate): Float?
}
