package com.mealplanner.inventory.di

import com.mealplanner.inventory.data.InventoryRepositoryImpl
import com.mealplanner.inventory.data.local.InventoryDao
import com.mealplanner.inventory.data.local.InventoryEntity
import com.mealplanner.inventory.domain.repository.InventoryRepository
import com.mealplanner.inventory.domain.usecase.AddInventoryItemUseCase
import com.mealplanner.inventory.domain.usecase.DeleteInventoryItemUseCase
import com.mealplanner.inventory.domain.usecase.GetExpiringItemsUseCase
import com.mealplanner.inventory.domain.usecase.GetInventoryItemsUseCase
import com.mealplanner.inventory.domain.usecase.MarkItemConsumedUseCase
import com.mealplanner.inventory.domain.usecase.UpdateInventoryItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 库存模块 Hilt DI 配置
 */
@Module
@InstallIn(SingletonComponent::class)
object InventoryModule {
    
    /**
     * 提供库存仓库
     */
    @Provides
    @Singleton
    fun provideInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository {
        return inventoryRepositoryImpl
    }
}

/**
 * UseCase 模块
 */
@Module
@InstallIn(ViewModelComponent::class)
object InventoryUseCaseModule {
    
    @Provides
    fun provideAddInventoryItemUseCase(
        repository: InventoryRepository
    ): AddInventoryItemUseCase {
        return AddInventoryItemUseCase(repository)
    }
    
    @Provides
    fun provideGetInventoryItemsUseCase(
        repository: InventoryRepository
    ): GetInventoryItemsUseCase {
        return GetInventoryItemsUseCase(repository)
    }
    
    @Provides
    fun provideUpdateInventoryItemUseCase(
        repository: InventoryRepository
    ): UpdateInventoryItemUseCase {
        return UpdateInventoryItemUseCase(repository)
    }
    
    @Provides
    fun provideDeleteInventoryItemUseCase(
        repository: InventoryRepository
    ): DeleteInventoryItemUseCase {
        return DeleteInventoryItemUseCase(repository)
    }
    
    @Provides
    fun provideGetExpiringItemsUseCase(
        repository: InventoryRepository
    ): GetExpiringItemsUseCase {
        return GetExpiringItemsUseCase(repository)
    }
    
    @Provides
    fun provideMarkItemConsumedUseCase(
        repository: InventoryRepository
    ): MarkItemConsumedUseCase {
        return MarkItemConsumedUseCase(repository)
    }
}
