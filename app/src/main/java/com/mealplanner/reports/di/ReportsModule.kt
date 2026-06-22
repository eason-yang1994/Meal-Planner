package com.mealplanner.reports.di

import com.mealplanner.reports.data.ReportRepositoryImpl
import com.mealplanner.reports.domain.repository.ReportRepository
import com.mealplanner.reports.domain.usecase.ExportReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateDailyReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateMonthlyReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateWeeklyReportUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 报告模块
 * 
 * 提供报告相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object ReportsModule {
    
    /**
     * 提供 ReportRepository
     */
    @Provides
    @Singleton
    fun provideReportRepository(): ReportRepository {
        return ReportRepositoryImpl(
            mealPlanRepository = TODO(),
            weightRepository = TODO(),
            inventoryRepository = TODO()
        )
    }
    
    /**
     * 提供 GenerateDailyReportUseCase
     */
    @Provides
    fun provideGenerateDailyReportUseCase(
        reportRepository: ReportRepository
    ): GenerateDailyReportUseCase {
        return GenerateDailyReportUseCase(reportRepository)
    }
    
    /**
     * 提供 GenerateWeeklyReportUseCase
     */
    @Provides
    fun provideGenerateWeeklyReportUseCase(
        reportRepository: ReportRepository
    ): GenerateWeeklyReportUseCase {
        return GenerateWeeklyReportUseCase(reportRepository)
    }
    
    /**
     * 提供 GenerateMonthlyReportUseCase
     */
    @Provides
    fun provideGenerateMonthlyReportUseCase(
        reportRepository: ReportRepository
    ): GenerateMonthlyReportUseCase {
        return GenerateMonthlyReportUseCase(reportRepository)
    }
    
    /**
     * 提供 ExportReportUseCase
     */
    @Provides
    fun provideExportReportUseCase(): ExportReportUseCase {
        return ExportReportUseCase()
    }
}
