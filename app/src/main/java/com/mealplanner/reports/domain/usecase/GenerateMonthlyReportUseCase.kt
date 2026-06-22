package com.mealplanner.reports.domain.usecase

import com.mealplanner.reports.domain.repository.ReportRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 生成月报用例
 * 
 * @param reportRepository 报告仓库
 */
class GenerateMonthlyReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /**
     * 生成指定月的月报
     * 
     * @param month 月份（格式：yyyy-MM）
     * @return 月报流
     */
    operator fun invoke(month: String): Flow<com.mealplanner.reports.domain.model.MonthlyReport> {
        return reportRepository.generateMonthlyReport(month)
    }
}
