package com.mealplanner.reports.presentation.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

/**
 * 报告页面
 * 
 * @param viewModel ReportsViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { ReportTab.values().size })
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("报告") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab 切换
            TabRow(
                selectedTabIndex = uiState.selectedTab.ordinal
            ) {
                ReportTab.values().forEachIndexed { index, tab ->
                    Tab(
                        selected = uiState.selectedTab.ordinal == index,
                        onClick = {
                            viewModel.onEvent(ReportsUiEvent.OnTabChange(tab))
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(tab.name) }
                    )
                }
            }
            
            // 报告内容
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (ReportTab.values()[page]) {
                    ReportTab.DAILY -> DailyReportContent(uiState.dailyReport)
                    ReportTab.WEEKLY -> WeeklyReportContent(uiState.weeklyReport)
                    ReportTab.MONTHLY -> MonthlyReportContent(uiState.monthlyReport)
                }
            }
        }
    }
}

/**
 * 日报内容
 */
@Composable
fun DailyReportContent(report: com.mealplanner.reports.domain.model.DailyReport?) {
    if (report == null) {
        EmptyReportContent()
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 热量摄入 vs 消耗
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "热量摄入 vs 消耗",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "摄入: ${String.format("%.1f", report.totalCalories)} 千卡",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "消耗: ${String.format("%.1f", report.exerciseCaloriesBurned)} 千卡",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // 营养素
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "营养素",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "蛋白质: ${String.format("%.1f", report.totalProtein)} g",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "碳水化合物: ${String.format("%.1f", report.totalCarbs)} g",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "脂肪: ${String.format("%.1f", report.totalFat)} g",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // 体重
        if (report.weight != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "体重",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${String.format("%.1f", report.weight)} kg",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (report.bmi != null) {
                        Text(
                            text = "BMI: ${String.format("%.1f", report.bmi)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

/**
 * 周报内容
 */
@Composable
fun WeeklyReportContent(report: com.mealplanner.reports.domain.model.WeeklyReport?) {
    if (report == null) {
        EmptyReportContent()
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "周报功能开发中...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 月报内容
 */
@Composable
fun MonthlyReportContent(report: com.mealplanner.reports.domain.model.MonthlyReport?) {
    if (report == null) {
        EmptyReportContent()
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "月报功能开发中...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 空报告内容
 */
@Composable
fun EmptyReportContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "暂无报告数据",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
