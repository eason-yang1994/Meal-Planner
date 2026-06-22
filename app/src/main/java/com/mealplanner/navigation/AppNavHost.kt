package com.mealplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mealplanner.navigation.NavRoutes.HOME

/**
 * 应用导航 Host
 * 
 * 定义应用的导航图，目前只有占位屏
 */
@Composable
fun AppNavHost(
    navController: NavHostController = androidx.navigation.compose.rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HOME,
        modifier = modifier
    ) {
        composable(HOME) {
            PlaceholderScreen(
                title = "三餐规划",
                subtitle = "欢迎使用三餐规划应用\n功能正在开发中..."
            )
        }
        
        // 后续添加其他页面的路由
        // composable(NavRoutes.WEIGHT) { WeightScreen() }
        // composable(NavRoutes.INVENTORY) { InventoryScreen() }
        // composable(NavRoutes.MEAL_PLAN) { MealPlanScreen() }
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