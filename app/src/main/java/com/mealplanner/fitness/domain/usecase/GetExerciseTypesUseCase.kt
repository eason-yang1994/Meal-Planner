package com.mealplanner.fitness.domain.usecase

import com.mealplanner.fitness.domain.model.ExerciseType
import javax.inject.Inject

/**
 * 获取运动类型列表用例
 * 
 * 返回预定义的32种运动类型
 */
class GetExerciseTypesUseCase @Inject constructor() {
    
    /**
     * 获取所有运动类型
     * 
     * @return 运动类型列表
     */
    operator fun invoke(): List<ExerciseType> {
        return com.mealplanner.fitness.domain.model.ExerciseTypes.getAll()
    }
}
