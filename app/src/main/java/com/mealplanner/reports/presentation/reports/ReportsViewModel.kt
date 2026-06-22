package com.mealplanner.reports.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.reports.domain.model.DailyReport
import com.mealplanner.reports.domain.model.MonthlyReport
import com.mealplanner.reports.domain.model.WeeklyReport
import com.mealplanner.reports.domain.usecase.ExportReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateDailyReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateMonthlyReportUseCase
import com.mealplanner.reports.domain.usecase.GenerateWeeklyReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 报告 ViewModel
 * 
 * @param generateDailyReportUseCase 生成日报用例
 * @param generateWeeklyReportUseCase 生成周报用例
 * @param generateMonthlyReportUseCase 生成月报用例
 * @param exportReportUseCase 导出报告用例
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val generateDailyReportUseCase: GenerateDailyReportUseCase,
    private val generateWeeklyReportUseCase: GenerateWeeklyReportUseCase,
    private val generateMonthlyReportUseCase: GenerateMonthlyReportUseCase,
    private val exportReportUseCase: ExportReportUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()
    
    init {
        loadDailyReport(LocalDate.now())
    }
    
    /**
     * 处理 UI 事件
     */
    fun onEvent(event: ReportsUiEvent) {
        when (event) {
            is ReportsUiEvent.OnTabChange -> changeTab(event.tab)
            is ReportsUiEvent.OnExportReport -> exportReport(event.format)
        }
    }
    
    /**
     * 切换标签页
     */
    private fun changeTab(tab: ReportTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        
        when (tab) {
            ReportTab.DAILY -> loadDailyReport(LocalDate.now())
            ReportTab.WEEKLY -> loadWeeklyReport(getCurrentWeekStart())
            ReportTab.MONTHLY -> loadMonthlyReport(getCurrentMonth())
        }
    }
    
    /**
     * 加载日报
     */
    private fun loadDailyReport(date: LocalDate) {
        viewModelScope.launch {
            generateDailyReportUseCase(date).collectLatest { report ->
                _uiState.update { it.copy(dailyReport = report) }
            }
        }
    }
    
    /**
     * 加载周报
     */
    private fun loadWeeklyReport(weekStartDate: LocalDate) {
        viewModelScope.launch {
            generateWeeklyReportUseCase(weekStartDate).collectLatest { report ->
                _uiState.update { it.copy(weeklyReport = report) }
            }
        }
    }
    
    /**
     * 加载月报
     */
    private fun loadMonthlyReport(month: String) {
        viewModelScope.launch {
            generateMonthlyReportUseCase(month).collectLatest { report ->
                _uiState.update { it.copy(monthlyReport = report) }
            }
        }
    }
    
    /**
     * 导出报告
     */
    private fun exportReport(format: ExportFormat) {
        viewModelScope.launch {
            val report = when (_uiState.value.selectedTab) {
                ReportTab.DAILY -> _uiState.value.dailyReport
                ReportTab.WEEKLY -> _uiState.value.weeklyReport
                ReportTab.MONTHLY -> _uiState.value.monthlyReport
            }
            
            if (report != null) {
                exportReportUseCase(report, format)
            }
        }
    }
    
    /**
     * 获取当前周开始日期
     */
    private fun getCurrentWeekStart(): LocalDate {
        val today = LocalDate.now()
        return today.minusDays(today.dayOfWeek.value - 1L)
    }
    
    /**
     * 获取当前月份
     */
    private fun getCurrentMonth(): String {
        val today = LocalDate.now()
        return "${today.year}-${String.format("%02d", today.monthValue)}"
    }
}

/**
 * 报告标签页枚举
 */
enum class ReportTab {
    DAILY,   // 日报
    WEEKLY,  // 周报
    MONTHLY  // 月报
}

/**
 * 报告 UI 状态
 * 
 * @param selectedTab 选中的标签页
 * @param dailyReport 日报
 * @param weeklyReport 周报
 * @param monthlyReport 月报
 */
data class ReportsUiState(
    val selectedTab: ReportTab = ReportTab.DAILY,
    val dailyReport: DailyReport? = null,
    val weeklyReport: WeeklyReport? = null,
    val monthlyReport: MonthlyReport? = null
)

/**
 * 报告 UI 事件
 */
sealed class ReportsUiEvent {
    data class OnTabChange(val tab: ReportTab) : ReportsUiEvent()
    data class OnExportReport(val format: ExportFormat) : ReportsUiEvent()
}
