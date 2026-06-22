package com.mealplanner.userprofile.domain.usecase

import com.mealplanner.userprofile.domain.model.UserProfile
import com.mealplanner.userprofile.domain.repository.UserProfileRepository
import javax.inject.Inject

/**
 * 保存用户资料用例
 */
class SaveUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    
    /**
     * 保存用户资料
     * 
     * @param profile 用户资料
     * @return 操作结果
     */
    suspend operator fun invoke(profile: UserProfile): Result<Unit> {
        return userProfileRepository.saveUserProfile(profile)
    }
}
