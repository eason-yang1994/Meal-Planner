package com.mealplanner.core.di

import android.content.Context
import androidx.room.Room
import com.mealplanner.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库 DI Module
 * 
 * 提供数据库和 DAO 实例
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "meal_planner.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    // 后续添加 DAO 提供的方法
    // @Provides
    // fun provideUserProfileDao(database: AppDatabase): UserProfileDao {
    //     return database.userProfileDao()
    // }
}