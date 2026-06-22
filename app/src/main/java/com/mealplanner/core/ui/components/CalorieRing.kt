package com.mealplanner.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mealplanner.core.ui.theme.Color as AppColor

/**
 * 热量圆环组件
 * 
 * 使用 Compose Canvas 实现圆环进度条
 * 显示当前摄入热量占目标热量的比例
 * 
 * @param currentCalories 当前摄入热量
 * @param targetCalories 目标热量
 * @param modifier 修饰符
 * @param ringColor 圆环颜色（默认使用主色）
 * @param backgroundColor 背景圆环颜色
 */
@Composable
fun CalorieRing(
    currentCalories: Int,
    targetCalories: Int,
    modifier: Modifier = Modifier,
    ringColor: Color = AppColor.PrimaryPurple,
    backgroundColor: Color = AppColor.CalorieRingBackground
) {
    val progress = if (targetCalories > 0) {
        (currentCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1.5f)
    } else {
        0f
    }
    
    val strokeWidth = 12.dp
    
    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // 绘制圆环
        Canvas(modifier = Modifier.size(200.dp)) {
            val canvasSize = size.minDimension
            val radius = (canvasSize - strokeWidth.toPx()) / 2
            val center = Offset(size.width / 2, size.height / 2)
            
            // 绘制背景圆环
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(
                    (size.width - radius * 2 - strokeWidth.toPx()) / 2,
                    (size.height - radius * 2 - strokeWidth.toPx()) / 2
                ),
                size = Size(radius * 2 + strokeWidth.toPx(), radius * 2 + strokeWidth.toPx()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            
            // 绘制进度圆环
            val sweepAngle = progress * 360f
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    (size.width - radius * 2 - strokeWidth.toPx()) / 2,
                    (size.height - radius * 2 - strokeWidth.toPx()) / 2
                ),
                size = Size(radius * 2 + strokeWidth.toPx(), radius * 2 + strokeWidth.toPx()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        
        // 中心文本
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$currentCalories",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            
            Text(
                text = "/ $targetCalories kcal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (progress > 1.0f) {
                Text(
                    text = "已超标",
                    style = MaterialTheme.typography.labelSmall,
                    color = AppColor.AccentRed
                )
            }
        }
    }
}

/**
 * 小型热量圆环（用于列表项）
 * 
 * @param progress 进度 (0.0 - 1.0)
 * @param modifier 修饰符
 */
@Composable
fun SmallCalorieRing(
    progress: Float,
    modifier: Modifier = Modifier,
    ringColor: Color = AppColor.PrimaryPurple
) {
    val strokeWidth = 6.dp
    
    Canvas(modifier = modifier.size(40.dp)) {
        val canvasSize = size.minDimension
        val radius = (canvasSize - strokeWidth.toPx()) / 2
        
        // 背景圆环
        drawArc(
            color = AppColor.CalorieRingBackground,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(
                (size.width - radius * 2 - strokeWidth.toPx()) / 2,
                (size.height - radius * 2 - strokeWidth.toPx()) / 2
            ),
            size = Size(radius * 2 + strokeWidth.toPx(), radius * 2 + strokeWidth.toPx()),
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        
        // 进度圆环
        val sweepAngle = (progress.coerceIn(0f, 1.5f)) * 360f
        drawArc(
            color = ringColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(
                (size.width - radius * 2 - strokeWidth.toPx()) / 2,
                (size.height - radius * 2 - strokeWidth.toPx()) / 2
            ),
            size = Size(radius * 2 + strokeWidth.toPx(), radius * 2 + strokeWidth.toPx()),
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}