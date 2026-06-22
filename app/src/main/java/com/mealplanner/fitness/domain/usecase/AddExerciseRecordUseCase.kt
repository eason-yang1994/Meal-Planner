package com.mealplanner.fitness.domain.usecase

import com.mealplanner.fitness.domain.model.ExerciseRecord
import com.mealplanner.fitness.domain.repository.FitnessRepository
import javax.inject.Inject

/**
 * 添加运动记录用例
 * 
 * @param fitnessRepository 运动追踪仓库
 */
class AddExerciseRecordUseCase @Inject constructor(
    private val fitnessRepository: FitnessRepository
) {
    /**
     * 执行添加运动记录
     * 
     * @param record 运动记录
     * @return 操作结果
     */
    suspend operator fun invoke(record: ExerciseRecord): Result<Unit> {
        return fitnessRepository.addRecord(record)
    }
}
