package com.mealplanner.userprofile.domain.repository

import com.mealplanner.userprofile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * 用户资料仓库接口
 * 
 * 定义用户资料的数据操作
 */
interface UserProfileRepository {
    
    /**
     * 获取用户资料
     * 
     * @return 用户资料 Flow，如果不存在则返回 null
     */
    fun getUserProfile(): Flow<UserProfile?>
    
    /**
     * 保存用户资料
     * 
     * @param profile 用户资料
     * @return 操作结果
     */
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
    
    /**
     * 检查用户资料是否存在
     * 
     * @return 如果用户资料存在则返回 true，否则返回 false
     */
    suspend fun isProfileExists(): Boolean
}
