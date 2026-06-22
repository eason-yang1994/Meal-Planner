package com.mealplanner.weight.domain.repository

import com.mealplanner.weight.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * 体重仓库接口
 * 
 * 定义体重记录的数据操作
 */
interface WeightRepository {
    
    /**
     * 添加体重记录
     * 
     * @param record 体重记录
     * @return 操作结果
     */
    suspend fun addRecord(record: WeightRecord): Result<Unit>
    
    /**
     * 获取所有体重记录
     * 
     * @return 体重记录 Flow 列表
     */
    fun getRecords(): Flow<List<WeightRecord>>
    
    /**
     * 获取指定日期范围内的体重记录
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 体重记录 Flow 列表
     */
    fun getRecordsInRange(start: LocalDate, end: LocalDate): Flow<List<WeightRecord>>
    
    /**
     * 获取最新的体重记录
     * 
     * @return 最新的体重记录 Flow
     */
    fun getLatestRecord(): Flow<WeightRecord?>
    
    /**
     * 删除体重记录
     * 
     * @param id 记录 ID
     * @return 操作结果
     */
    suspend fun deleteRecord(id: String): Result<Unit>
}
