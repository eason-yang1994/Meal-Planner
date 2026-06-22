package com.mealplanner.userprofile.data

import com.mealplanner.userprofile.data.local.UserProfileDao
import com.mealplanner.userprofile.data.local.UserProfileEntity
import com.mealplanner.userprofile.domain.model.UserProfile
import com.mealplanner.userprofile.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户资料仓库实现
 * 
 * @param userProfileDao 用户资料 DAO
 */
@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {
    
    override fun getUserProfile(): Flow<UserProfile?> {
        return userProfileDao.observeProfile().map { entity ->
            entity?.toDomainModel()
        }
    }
    
    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val entity = profile.toEntity()
            userProfileDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isProfileExists(): Boolean {
        return userProfileDao.count() > 0
    }
    
    /**
     * 将领域模型转换为 Room 实体
     * 
     * @return Room 实体
     */
    private fun UserProfile.toEntity(): UserProfileEntity {
        return UserProfileEntity(
            id = this.id,
            gender = this.gender,
            birthDate = this.birthDate,
            heightCm = this.heightCm,
            initialWeightKg = this.initialWeightKg,
            activityLevel = this.activityLevel,
            goal = this.goal,
            targetWeightKg = this.targetWeightKg,
            createdAt = this.createdAt,
            lastExportedAt = this.lastExportedAt
        )
    }
    
    /**
     * 将 Room 实体转换为领域模型
     * 
     * @return 领域模型
     */
    private fun UserProfileEntity.toDomainModel(): UserProfile {
        return UserProfile(
            id = this.id,
            gender = this.gender,
            birthDate = this.birthDate,
            heightCm = this.heightCm,
            initialWeightKg = this.initialWeightKg,
            activityLevel = this.activityLevel,
            goal = this.goal,
            targetWeightKg = this.targetWeightKg,
            createdAt = this.createdAt,
            lastExportedAt = this.lastExportedAt
        )
    }
}
