package com.mealplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mealplanner.home.presentation.HomeScreen
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import com.mealplanner.userprofile.presentation.setup.SetupWizardScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 应用导航 Host
 * 
 * 定义应用的导航图
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // 检查用户资料是否存在，决定起始目的地
    var startDestination by remember { mutableStateOf<String?>(null) }
    
    // 检查用户是否已设置
    val viewModel: AppNavHostViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(uiState.isProfileChecked) {
        if (uiState.isProfileChecked) {
            startDestination = if (uiState.isProfileExists) {
                NavRoutes.HOME
            } else {
                NavRoutes.SETUP_WIZARD
            }
        }
    }
    
    // 等待检查完成
    if (startDestination == null) {
        PlaceholderScreen(
            title = "三餐规划",
            subtitle = "正在加载..."
        )
        return
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination!!,
        modifier = modifier
    ) {
        // 设置向导
        composable(NavRoutes.SETUP_WIZARD) {
            SetupWizardScreen(navController = navController)
        }
        
        // 首页
        composable(NavRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        
        // 体重追踪
        composable(NavRoutes.WEIGHT) {
            WeightScreen(navController = navController)
        }
        
        // 后续添加其他页面的路由
        // composable(NavRoutes.INVENTORY) { InventoryScreen(navController) }
        // composable(NavRoutes.MEAL_PLAN) { MealPlanScreen(navController) }
    }
}

/**
 * 占位屏组件
 */
@Composable
fun PlaceholderScreen(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            androidx.compose.material3.Text(
                text = title,
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme.primary
            )
            
            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.height(androidx.compose.ui.unit.dp(16))
            )
            
            androidx.compose.material3.Text(
                text = subtitle,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}