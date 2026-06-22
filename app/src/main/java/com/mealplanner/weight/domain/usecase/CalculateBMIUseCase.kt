package com.mealplanner.weight.domain.usecase

import javax.inject.Inject

/**
 * 计算 BMI（身体质量指数）用例
 * 
 * BMI = 体重(kg) / 身高(m)²
 */
class CalculateBMIUseCase @Inject constructor() {
    /**
     * 计算 BMI
     * 
     * @param weightKg 体重（公斤）
     * @param heightCm 身高（厘米）
     * @return BMI 值
     */
    operator fun invoke(weightKg: Float, heightCm: Float): Float {
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }
}
