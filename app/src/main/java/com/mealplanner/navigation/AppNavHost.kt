package com.mealplanner.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mealplanner.home.presentation.HomeScreen
import com.mealplanner.profile.presentation.ProfileScreen
import com.mealplanner.userprofile.domain.usecase.GetUserProfileUseCase
import com.mealplanner.userprofile.presentation.setup.SetupWizardScreen
import kotlinx.coroutines.flow.first

/**
 * 底部导航项
 */
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * 应用导航 Host
 * 
 * 定义应用的导航图，包含底部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    
    // 底部导航项
    val bottomNavItems = listOf(
        BottomNavItem(NavRoutes.HOME, Icons.Default.Home, "首页"),
        BottomNavItem("meal_plan", Icons.Default.Restaurant, "饮食"),
        BottomNavItem("fitness", Icons.Default.FitnessCenter, "运动"),
        BottomNavItem("reports", Icons.Default.List, "报告"),
        BottomNavItem("profile", Icons.Default.Person, "我的")
    )
    
    // 需要显示底部导航的路由
    val routesWithBottomNav = listOf(NavRoutes.HOME, "meal_plan", "fitness", "reports", "profile")
    
    // 当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // 是否显示底部导航
    val showBottomNav = currentRoute in routesWithBottomNav
    
    // 等待检查完成
    if (startDestination == null) {
        if (uiState.isProfileChecked) {
            startDestination = if (uiState.isProfileExists) {
                NavRoutes.HOME
            } else {
                NavRoutes.SETUP_WIZARD
            }
        } else {
            // 还在检查中，显示加载界面
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
            return
        }
    }
    
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    // 避免重复导航到同一目的地
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // 确保在顶部启动目的地
                                    launchSingleTop = true
                                    // 恢复状态
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            modifier = modifier.padding(paddingValues)
        ) {
            // 设置向导
            composable(NavRoutes.SETUP_WIZARD) {
                SetupWizardScreen(navController = navController)
            }
            
            // 首页
            composable(NavRoutes.HOME) {
                HomeScreen(navController = navController)
            }
            
            // 饮食（三餐规划）
            composable("meal_plan") {
                com.mealplanner.mealplan.presentation.mealplan.MealPlanScreen(
                    navController = navController
                )
            }
            
            // 运动
            composable("fitness") {
                com.mealplanner.fitness.presentation.fitness.FitnessScreen()
            }
            
            // 报告
            composable("reports") {
                com.mealplanner.reports.presentation.reports.ReportsScreen()
            }
            
            // 我的（个人资料）
            composable("profile") {
                ProfileScreen()
            }
        }
    }
}
