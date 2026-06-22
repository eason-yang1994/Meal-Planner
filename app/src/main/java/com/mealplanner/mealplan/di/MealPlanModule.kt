package com.mealplanner.mealplan.di

import com.mealplanner.mealplan.data.MealPlanRepositoryImpl
import com.mealplanner.mealplan.data.local.MealPlanDao
import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import com.mealplanner.mealplan.domain.usecase.ConfirmMealUseCase
import com.mealplanner.mealplan.domain.usecase.GenerateMealPlanUseCase
import com.mealplanner.mealplan.domain.usecase.GetMealPlansForDateUseCase
import com.mealplanner.mealplan.domain.usecase.GetRecentMealsUseCase
import com.mealplanner.mealplan.domain.usecase.ModifyMealUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 三餐规划模块 Hilt DI 配置
 */
@Module
@InstallIn(SingletonComponent::class)
object MealPlanModule {
    
    /**
     * 提供三餐计划仓库
     */
    @Provides
    @Singleton
    fun provideMealPlanRepository(
        mealPlanRepositoryImpl: MealPlanRepositoryImpl
    ): MealPlanRepository {
        return mealPlanRepositoryImpl
    }
}

/**
 * UseCase 模块
 */
@Module
@InstallIn(ViewModelComponent::class)
object MealPlanUseCaseModule {
    
    @Provides
    fun provideGenerateMealPlanUseCase(
        repository: MealPlanRepository,
        inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository,
        calculateTDEEUseCase: com.mealplanner.userprofile.domain.usecase.CalculateTDEEUseCase,
        application: android.app.Application
    ): GenerateMealPlanUseCase {
        return GenerateMealPlanUseCase(
            repository,
            inventoryRepository,
            calculateTDEEUseCase,
            application
        )
    }
    
    @Provides
    fun provideConfirmMealUseCase(
        repository: MealPlanRepository,
        inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository
    ): ConfirmMealUseCase {
        return ConfirmMealUseCase(repository, inventoryRepository)
    }
    
    @Provides
    fun provideModifyMealUseCase(
        repository: MealPlanRepository,
        inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository
    ): ModifyMealUseCase {
        return ModifyMealUseCase(repository, inventoryRepository)
    }
    
    @Provides
    fun provideGetMealPlansForDateUseCase(
        repository: MealPlanRepository
    ): GetMealPlansForDateUseCase {
        return GetMealPlansForDateUseCase(repository)
    }
    
    @Provides
    fun provideGetRecentMealsUseCase(
        repository: MealPlanRepository
    ): GetRecentMealsUseCase {
        return GetRecentMealsUseCase(repository)
    }
}
