package com.mealplanner.userprofile.domain.usecase

import com.mealplanner.userprofile.domain.model.ActivityLevel
import javax.inject.Inject

/**
 * 计算 TDEE（每日总能量消耗）用例
 * 
 * TDEE = BMR × 活动系数
 */
class CalculateTDEEUseCase @Inject constructor() {
    
    /**
     * 计算 TDEE
     * 
     * @param bmr BMR 值（大卡/天）
     * @param activityLevel 活动水平
     * @return TDEE 值（大卡/天）
     */
    operator fun invoke(bmr: Float, activityLevel: ActivityLevel): Float {
        return bmr * activityLevel.factor
    }
}
