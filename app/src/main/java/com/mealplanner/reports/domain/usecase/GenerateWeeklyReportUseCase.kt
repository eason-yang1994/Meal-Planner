package com.mealplanner.reports.domain.usecase

import com.mealplanner.reports.domain.repository.ReportRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 生成周报用例
 * 
 * @param reportRepository 报告仓库
 */
class GenerateWeeklyReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /**
     * 生成指定周的周报
     * 
     * @param weekStartDate 周开始日期
     * @return 周报流
     */
    operator fun invoke(weekStartDate: LocalDate): Flow<com.mealplanner.reports.domain.model.WeeklyReport> {
        return reportRepository.generateWeeklyReport(weekStartDate)
    }
}
