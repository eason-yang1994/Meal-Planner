package com.mealplanner.userprofile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 用户资料 DAO 接口
 * 
 * 定义对用户资料表的数据库操作
 */
@Dao
interface UserProfileDao {
    
    /**
     * 插入或更新用户资料
     * 
     * @param profile 用户资料实体
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfileEntity)
    
    /**
     * 根据 ID 获取用户资料
     * 
     * @param id 用户资料 ID
     * @return 用户资料实体，如果不存在则返回 null
     */
    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getById(id: String = "default"): UserProfileEntity?
    
    /**
     * 更新用户资料
     * 
     * @param profile 用户资料实体
     */
    @Update
    suspend fun update(profile: UserProfileEntity)
    
    /**
     * 获取用户资料的数量
     * 
     * @return 用户资料数量
     */
    @Query("SELECT COUNT(*) FROM user_profile")
    suspend fun count(): Int
    
    /**
     * 观察用户资料变化
     * 
     * @return 用户资料 Flow
     */
    @Query("SELECT * FROM user_profile WHERE id = 'default'")
    fun observeProfile(): Flow<UserProfileEntity?>
}
