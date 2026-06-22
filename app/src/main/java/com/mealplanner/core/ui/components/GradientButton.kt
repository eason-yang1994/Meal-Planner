package com.mealplanner.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mealplanner.core.ui.theme.Color as AppColor

/**
 * 渐变按钮组件
 * 
 * 使用渐变色背景的按钮
 * 
 * @param text 按钮文本
 * @param onClick 点击事件
 * @param modifier 修饰符
 * @param gradientColors 渐变颜色列表（默认使用主色到辅助色）
 * @param enabled 是否启用
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(AppColor.GradientStart, AppColor.GradientEnd),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Gray
        ),
        contentPadding = PaddingValues(),
        enabled = enabled
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) gradientColors else listOf(Color.Gray, Color.Gray)
                    )
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (enabled) Color.White else Color.LightGray
            )
        }
    }
}

/**
 * 小号渐变按钮
 * 
 * @param text 按钮文本
 * @param onClick 点击事件
 * @param modifier 修饰符
 */
@Composable
fun SmallGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(AppColor.GradientStart, AppColor.GradientEnd)
                    )
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

/**
 * 圆形渐变按钮（图标按钮）
 * 
 * @param onClick 点击事件
 * @param modifier 修饰符
 * @param content 内容（通常是图标）
 */
@Composable
fun CircleGradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .size(56.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(AppColor.GradientStart, AppColor.GradientEnd)
                )
            )
            .clickable { onClick() },
        contentAlignment = androidx.compose.ui.Alignment.Center,
        content = { content() }
    )
}

// 需要导入 clickable
import androidx.compose.foundation.clickable

/**
 * 圆形渐变按钮（图标按钮）
 * 
 * @param onClick 点击事件
 * @param modifier 修饰符
 * @param content 内容（通常是图标）
 */
@Composable
fun CircleGradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(AppColor.GradientStart, AppColor.GradientEnd)
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
        content = { content() }
    )
}