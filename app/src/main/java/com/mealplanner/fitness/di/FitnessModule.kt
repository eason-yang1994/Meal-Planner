package com.mealplanner.fitness.di

import com.mealplanner.fitness.data.FitnessRepositoryImpl
import com.mealplanner.fitness.data.local.ExerciseRecordDao
import com.mealplanner.fitness.domain.repository.FitnessRepository
import com.mealplanner.fitness.domain.usecase.AddExerciseRecordUseCase
import com.mealplanner.fitness.domain.usecase.CalculateCaloriesBurnedUseCase
import com.mealplanner.fitness.domain.usecase.GetExerciseRecordsUseCase
import com.mealplanner.fitness.domain.usecase.GetExerciseTypesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 运动追踪模块
 * 
 * 提供运动追踪相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object FitnessModule {
    
    /**
     * 提供 FitnessRepository
     */
    @Provides
    @Singleton
    fun provideFitnessRepository(
        exerciseRecordDao: ExerciseRecordDao
    ): FitnessRepository {
        return FitnessRepositoryImpl(exerciseRecordDao)
    }
    
    /**
     * 提供 AddExerciseRecordUseCase
     */
    @Provides
    fun provideAddExerciseRecordUseCase(
        fitnessRepository: FitnessRepository
    ): AddExerciseRecordUseCase {
        return AddExerciseRecordUseCase(fitnessRepository)
    }
    
    /**
     * 提供 GetExerciseRecordsUseCase
     */
    @Provides
    fun provideGetExerciseRecordsUseCase(
        fitnessRepository: FitnessRepository
    ): GetExerciseRecordsUseCase {
        return GetExerciseRecordsUseCase(fitnessRepository)
    }
    
    /**
     * 提供 CalculateCaloriesBurnedUseCase
     */
    @Provides
    fun provideCalculateCaloriesBurnedUseCase(): CalculateCaloriesBurnedUseCase {
        return CalculateCaloriesBurnedUseCase()
    }
    
    /**
     * 提供 GetExerciseTypesUseCase
     */
    @Provides
    fun provideGetExerciseTypesUseCase(): GetExerciseTypesUseCase {
        return GetExerciseTypesUseCase()
    }
}
