package com.mealplanner.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.mealplanner.core.ui.theme.Color as AppColor

/**
 * 三餐规划应用的 Material3 主题
 * 
 * 支持深色模式
 */

// 浅色主题配色
private val LightColorScheme = lightColorScheme(
    primary = AppColor.PrimaryPurple,
    onPrimary = AppColor.TextOnPrimary,
    primaryContainer = AppColor.PrimaryPurpleLight,
    onPrimaryContainer = AppColor.TextOnPrimary,
    
    secondary = AppColor.SecondaryPink,
    onSecondary = AppColor.TextOnPrimary,
    secondaryContainer = AppColor.SecondaryPinkLight,
    onSecondaryContainer = AppColor.TextOnPrimary,
    
    tertiary = AppColor.AccentOrange,
    onTertiary = AppColor.TextOnPrimary,
    tertiaryContainer = AppColor.AccentOrange.copy(alpha = 0.2f),
    onTertiaryContainer = AppColor.AccentOrange,
    
    background = AppColor.BackgroundLight,
    onBackground = AppColor.TextPrimary,
    
    surface = AppColor.SurfaceLight,
    onSurface = AppColor.TextPrimary,
    surfaceVariant = AppColor.BackgroundLight,
    onSurfaceVariant = AppColor.TextSecondary,
    
    error = AppColor.Error,
    onError = AppColor.TextOnPrimary,
    errorContainer = AppColor.Error.copy(alpha = 0.1f),
    onErrorContainer = AppColor.Error
)

// 深色主题配色
private val DarkColorScheme = darkColorScheme(
    primary = AppColor.PrimaryPurpleLight,
    onPrimary = AppColor.TextOnPrimary,
    primaryContainer = AppColor.PrimaryPurpleDark,
    onPrimaryContainer = AppColor.TextOnPrimary,
    
    secondary = AppColor.SecondaryPinkLight,
    onSecondary = AppColor.TextOnPrimary,
    secondaryContainer = AppColor.SecondaryPinkDark,
    onSecondaryContainer = AppColor.TextOnPrimary,
    
    tertiary = AppColor.AccentOrange,
    onTertiary = AppColor.TextOnPrimary,
    tertiaryContainer = AppColor.AccentOrange.copy(alpha = 0.3f),
    onTertiaryContainer = AppColor.AccentOrange,
    
    background = AppColor.BackgroundDark,
    onBackground = AppColor.TextPrimaryDark,
    
    surface = AppColor.SurfaceDark,
    onSurface = AppColor.TextPrimaryDark,
    surfaceVariant = AppColor.SurfaceDark.copy(alpha = 0.8f),
    onSurfaceVariant = AppColor.TextSecondaryDark,
    
    error = AppColor.Error,
    onError = AppColor.TextOnPrimary,
    errorContainer = AppColor.Error.copy(alpha = 0.2f),
    onErrorContainer = AppColor.Error
)

/**
 * 三餐规划应用主题
 * 
 * @param darkTheme 是否使用深色主题，默认跟随系统
 * @param content 内容
 */
@Composable
fun MealPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}