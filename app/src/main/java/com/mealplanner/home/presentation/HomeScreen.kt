package com.mealplanner.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mealplanner.core.ui.components.GlassCard
import com.mealplanner.navigation.NavRoutes
import kotlinx.coroutines.flow.first

/**
 * 首页
 * 
 * @param navController 导航控制器
 * @param viewModel 视图模型
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val homeNavController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }
    
    // 底部导航项
    val bottomNavItems = listOf(
        BottomNavItem("首页", androidx.compose.material.icons.Icons.Default.Home),
        BottomNavItem("饮食", androidx.compose.material.icons.Icons.Default.Restaurant),
        BottomNavItem("运动", androidx.compose.material.icons.Icons.Default.FitnessCenter),
        BottomNavItem("报告", androidx.compose.material.icons.Icons.Default.BarChart),
        BottomNavItem("我的", androidx.compose.material.icons.Icons.Default.Person)
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // 根据选中的标签页显示内容
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> HomeContent(uiState = uiState)
                1 -> PlaceholderContent(title = "饮食", subtitle = "三餐规划功能开发中...")
                2 -> PlaceholderContent(title = "运动", subtitle = "运动追踪功能开发中...")
                3 -> PlaceholderContent(title = "报告", subtitle = "报告生成功能开发中...")
                4 -> PlaceholderContent(title = "我的", subtitle = "个人设置功能开发中...")
            }
        }
    }
}

/**
 * 首页内容
 */
@Composable
fun HomeContent(uiState: HomeUiState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "今天",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 热量圆环占位（后续 Task-013 完善）
        GlassCard(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "热量圆环\n（待实现）",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 用户信息卡片
        if (uiState.isProfileLoaded && uiState.userProfile != null) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "用户信息",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "性别: ${if (uiState.userProfile.gender == com.mealplanner.userprofile.domain.model.Gender.MALE) "男" else "女"}")
                    Text(text = "身高: ${uiState.userProfile.heightCm} cm")
                    Text(text = "初始体重: ${uiState.userProfile.initialWeightKg} kg")
                }
            }
        }
    }
}

/**
 * 占位内容
 */
@Composable
fun PlaceholderContent(title: String, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * 底部导航项数据类
 */
data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
