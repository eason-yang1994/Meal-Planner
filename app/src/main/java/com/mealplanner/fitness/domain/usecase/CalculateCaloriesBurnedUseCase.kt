package com.mealplanner.fitness.domain.usecase

import javax.inject.Inject

/**
 * 计算运动消耗热量用例
 * 
 * 使用 MET 公式计算：
 * calories = MET × weightKg × durationMinutes / 60
 * 
 * MET (Metabolic Equivalent of Task): 代谢当量
 * - 1 MET = 安静坐着时的能量消耗（约 1 kcal/kg/h）
 * - 跑步约 7 MET，意味着消耗是安静时的 7 倍
 */
class CalculateCaloriesBurnedUseCase @Inject constructor() {
    
    /**
     * 计算运动消耗热量
     * 
     * @param metValue MET值
     * @param durationMinutes 运动时长（分钟）
     * @param weightKg 体重（公斤）
     * @return 消耗热量（千卡）
     */
    operator fun invoke(
        metValue: Float,
        durationMinutes: Int,
        weightKg: Float
    ): Float {
        return metValue * weightKg * durationMinutes / 60f
    }
}
