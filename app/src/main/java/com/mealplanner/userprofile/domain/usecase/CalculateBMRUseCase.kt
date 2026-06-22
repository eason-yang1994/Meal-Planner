package com.mealplanner.userprofile.domain.usecase

import com.mealplanner.userprofile.domain.model.UserProfile
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * 计算 BMR（基础代谢率）用例
 * 
 * 使用 Mifflin-St Jeor 公式：
 * - 男性: BMR = 10 × 体重(kg) + 6.25 × 身高(cm) - 5 × 年龄 + 5
 * - 女性: BMR = 10 × 体重(kg) + 6.25 × 身高(cm) - 5 × 年龄 - 161
 */
class CalculateBMRUseCase @Inject constructor() {
    
    /**
     * 计算 BMR
     * 
     * @param profile 用户资料
     * @return BMR 值（大卡/天）
     */
    operator fun invoke(profile: UserProfile): Float {
        val age = calculateAge(profile.birthDate)
        val weight = profile.initialWeightKg
        val height = profile.heightCm
        
        return when (profile.gender) {
            com.mealplanner.userprofile.domain.model.Gender.MALE -> {
                10f * weight + 6.25f * height - 5f * age + 5f
            }
            com.mealplanner.userprofile.domain.model.Gender.FEMALE -> {
                10f * weight + 6.25f * height - 5f * age - 161f
            }
        }
    }
    
    /**
     * 计算年龄
     * 
     * @param birthDate 出生日期
     * @return 年龄（岁）
     */
    private fun calculateAge(birthDate: LocalDate): Int {
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now()).toInt()
    }
}
