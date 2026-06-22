package com.mealplanner.fitness.domain.usecase

import com.mealplanner.fitness.domain.repository.FitnessRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取运动记录用例
 * 
 * @param fitnessRepository 运动追踪仓库
 */
class GetExerciseRecordsUseCase @Inject constructor(
    private val fitnessRepository: FitnessRepository
) {
    /**
     * 获取所有运动记录
     * 
     * @return 运动记录流
     */
    operator fun invoke(): Flow<List<com.mealplanner.fitness.domain.model.ExerciseRecord>> {
        return fitnessRepository.getRecords()
    }
    
    /**
     * 根据日期获取运动记录
     * 
     * @param date 日期
     * @return 运动记录流
     */
    fun getByDate(date: LocalDate): Flow<List<com.mealplanner.fitness.domain.model.ExerciseRecord>> {
        return fitnessRepository.getRecordsByDate(date)
    }
}
