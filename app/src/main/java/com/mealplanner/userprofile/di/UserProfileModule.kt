package com.mealplanner.userprofile.di

import com.mealplanner.userprofile.data.UserProfileRepositoryImpl
import com.mealplanner.userprofile.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 用户资料模块
 * 
 * 提供用户资料相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UserProfileModule {
    
    /**
     * 绑定用户资料仓库实现
     * 
     * @param impl UserProfileRepositoryImpl 实现
     * @return UserProfileRepository 接口
     */
    @Binds
    abstract fun bindRepository(impl: UserProfileRepositoryImpl): UserProfileRepository
}
