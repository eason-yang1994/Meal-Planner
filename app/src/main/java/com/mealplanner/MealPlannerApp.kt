package com.mealplanner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 三餐规划 Application 类
 * 
 * 使用 @HiltAndroidApp 注解启用 Hilt 依赖注入
 */
@HiltAndroidApp
class MealPlannerApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 初始化工作可以在这里进行
    }
}