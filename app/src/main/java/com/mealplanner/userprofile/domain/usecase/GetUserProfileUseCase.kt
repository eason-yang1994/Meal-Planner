package com.mealplanner.userprofile.domain.usecase

import com.mealplanner.userprofile.domain.repository.UserProfileRepository
import com.mealplanner.userprofile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取用户资料用例
 */
class GetUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    
    /**
     * 获取用户资料
     * 
     * @return 用户资料 Flow
     */
    operator fun invoke(): Flow<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }
}
