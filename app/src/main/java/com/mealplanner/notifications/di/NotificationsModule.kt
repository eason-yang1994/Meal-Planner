package com.mealplanner.notifications.di

import com.mealplanner.notifications.data.NotificationRepositoryImpl
import com.mealplanner.notifications.data.local.NotificationDao
import com.mealplanner.notifications.domain.repository.NotificationRepository
import com.mealplanner.notifications.domain.usecase.CancelNotificationUseCase
import com.mealplanner.notifications.domain.usecase.GetPendingNotificationsUseCase
import com.mealplanner.notifications.domain.usecase.ScheduleNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 通知模块
 * 
 * 提供通知相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {
    
    /**
     * 提供 NotificationRepository
     */
    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationDao: NotificationDao
    ): NotificationRepository {
        return NotificationRepositoryImpl(notificationDao)
    }
    
    /**
     * 提供 ScheduleNotificationUseCase
     */
    @Provides
    fun provideScheduleNotificationUseCase(
        notificationRepository: NotificationRepository
    ): ScheduleNotificationUseCase {
        return ScheduleNotificationUseCase(notificationRepository)
    }
    
    /**
     * 提供 CancelNotificationUseCase
     */
    @Provides
    fun provideCancelNotificationUseCase(
        notificationRepository: NotificationRepository
    ): CancelNotificationUseCase {
        return CancelNotificationUseCase(notificationRepository)
    }
    
    /**
     * 提供 GetPendingNotificationsUseCase
     */
    @Provides
    fun provideGetPendingNotificationsUseCase(
        notificationRepository: NotificationRepository
    ): GetPendingNotificationsUseCase {
        return GetPendingNotificationsUseCase(notificationRepository)
    }
}
