package com.mealplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mealplanner.core.ui.theme.MealPlannerTheme
import com.mealplanner.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主 Activity
 * 
 * 使用 @AndroidEntryPoint 注解启用 Hilt 注入
 * 设置 Compose 内容
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MealPlannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}