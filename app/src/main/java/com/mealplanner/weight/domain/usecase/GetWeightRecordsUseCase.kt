package com.mealplanner.weight.domain.usecase

import com.mealplanner.weight.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取体重记录用例
 */
class GetWeightRecordsUseCase @Inject constructor(
    private val weightRepository: WeightRepository
) {
    /**
     * 获取所有体重记录
     * 
     * @return 体重记录 Flow 列表
     */
    operator fun invoke(): Flow<List<com.mealplanner.weight.domain.model.WeightRecord>> {
        return weightRepository.getRecords()
    }
    
    /**
     * 获取指定日期范围内的体重记录
     * 
     * @param start 开始日期
     * @param end 结束日期
     * @return 体重记录 Flow 列表
     */
    fun getInRange(start: java.time.LocalDate, end: java.time.LocalDate): Flow<List<com.mealplanner.weight.domain.model.WeightRecord>> {
        return weightRepository.getRecordsInRange(start, end)
    }
    
    /**
     * 获取最新的体重记录
     * 
     * @return 最新的体重记录 Flow
     */
    fun getLatest(): Flow<com.mealplanner.weight.domain.model.WeightRecord?> {
        return weightRepository.getLatestRecord()
    }
}
