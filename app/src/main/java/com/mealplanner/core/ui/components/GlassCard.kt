package com.mealplanner.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 玻璃拟态卡片组件
 * 
 * 实现毛玻璃效果：
 * - 半透明白色背景
 * - 边框
 * - 阴影
 * - 16dp 圆角
 * 
 * @param modifier 修饰符
 * @param containerColor 容器颜色（默认半透明白色）
 * @param content 内容
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0x80FFFFFF),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .padding(16.dp),
        content = content
    )
}

/**
 * 带边框的玻璃拟态卡片
 * 
 * @param modifier 修饰符
 * @param containerColor 容器颜色
 * @param borderColor 边框颜色
 * @param content 内容
 */
@Composable
fun GlassCardWithBorder(
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0x80FFFFFF),
    borderColor: Color = Color(0x40FFFFFF),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .padding(16.dp)
    ) {
        content()
    }
}

/**
 *  Glass Card with blur effect (for API 31+)
 *  
 *  @param modifier Modifier
 *  @param blurRadius blur radius in dp
 *  @param content content
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    blurRadius: Float = 16f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .blur(blurRadius.dp)
            .background(Color(0x80FFFFFF))
            .padding(16.dp),
        content = content
    )
}