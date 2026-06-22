package com.mealplanner.reports.domain.usecase

import com.mealplanner.reports.domain.repository.ReportRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 生成日报用例
 * 
 * @param reportRepository 报告仓库
 */
class GenerateDailyReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /**
     * 生成指定日期的日报
     * 
     * @param date 日期
     * @return 日报流
     */
    operator fun invoke(date: LocalDate): Flow<com.mealplanner.reports.domain.model.DailyReport> {
        return reportRepository.generateDailyReport(date)
    }
}
