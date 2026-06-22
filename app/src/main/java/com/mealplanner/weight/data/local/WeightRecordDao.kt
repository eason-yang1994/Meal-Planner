package com.mealplanner.weight.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 体重记录 DAO 接口
 * 
 * 定义对体重记录表的数据库操作
 */
@Dao
interface WeightRecordDao {
    
    /**
     * 插入或更新体重记录
     * 
     * @param record 体重记录实体
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WeightRecordEntity)
    
    /**
     * 获取所有体重记录，按日期降序排列
     * 
     * @return 体重记录 Flow 列表
     */
    @Query("SELECT * FROM weight_records ORDER BY date DESC")
    fun getAllOrderedByDate(): Flow<List<WeightRecordEntity>>
    
    /**
     * 获取指定日期范围内的体重记录
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 体重记录列表
     */
    @Query("SELECT * FROM weight_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getByDateRange(startDate: LocalDate, endDate: LocalDate): List<WeightRecordEntity>
    
    /**
     * 删除体重记录
     * 
     * @param record 要删除的体重记录实体
     */
    @Delete
    suspend fun delete(record: WeightRecordEntity)
    
    /**
     * 获取最新的体重记录
     * 
     * @return 最新的体重记录实体，如果不存在则返回 null
     */
    @Query("SELECT * FROM weight_records ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): WeightRecordEntity?
    
    /**
     * 根据 ID 获取体重记录
     * 
     * @param id 记录 ID
     * @return 体重记录实体，如果不存在则返回 null
     */
    @Query("SELECT * FROM weight_records WHERE id = :id")
    suspend fun getById(id: String): WeightRecordEntity?
}
