package com.mealplanner.weight.di

import com.mealplanner.weight.data.WeightRepositoryImpl
import com.mealplanner.weight.domain.repository.WeightRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 体重模块
 * 
 * 提供体重相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class WeightModule {
    
    /**
     * 绑定体重仓库实现
     * 
     * @param impl WeightRepositoryImpl 实现
     * @return WeightRepository 接口
     */
    @Binds
    abstract fun bindRepository(impl: WeightRepositoryImpl): WeightRepository
}
