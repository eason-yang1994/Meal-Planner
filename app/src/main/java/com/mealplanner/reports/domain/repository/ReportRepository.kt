package com.mealplanner.reports.domain.repository

import com.mealplanner.reports.domain.model.DailyReport
import com.mealplanner.reports.domain.model.MonthlyReport
import com.mealplanner.reports.domain.model.WeeklyReport
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 报告 Repository 接口
 * 
 * 定义报告生成的数据操作
 */
interface ReportRepository {
    
    /**
     * 生成日报
     * 
     * @param date 日期
     * @return 日报流
     */
    fun generateDailyReport(date: LocalDate): Flow<DailyReport>
    
    /**
     * 生成周报
     * 
     * @param weekStartDate 周开始日期
     * @return 周报流
     */
    fun generateWeeklyReport(weekStartDate: LocalDate): Flow<WeeklyReport>
    
    /**
     * 生成月报
     * 
     * @param month 月份（格式：yyyy-MM）
     * @return 月报流
     */
    fun generateMonthlyReport(month: String): Flow<MonthlyReport>
}
