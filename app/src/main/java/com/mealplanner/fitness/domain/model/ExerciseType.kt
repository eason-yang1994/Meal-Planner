package com.mealplanner.fitness.domain.model

/**
 * 运动类型
 * 
 * @param name 运动名称
 * @param metValue MET值（代谢当量）
 * @param category 运动类别
 */
data class ExerciseType(
    val name: String,
    val metValue: Float,
    val category: ExerciseCategory
)

/**
 * 运动类别枚举
 */
enum class ExerciseCategory {
    AEROBIC,      // 有氧
    STRENGTH,     // 力量
    FLEXIBILITY,  // 柔韧性
    SPORTS,       // 球类运动
    DAILY         // 日常活动
}

/**
 * 预定义运动类型列表
 * 
 * 包含32种常见运动，参考：https://en.wikipedia.org/wiki/Metabolic_equivalent_of_task
 */
object ExerciseTypes {
    fun getAll(): List<ExerciseType> = listOf(
        // 有氧运动 (AEROBIC)
        ExerciseType("跑步", 7.0f, ExerciseCategory.AEROBIC),
        ExerciseType("快走", 4.3f, ExerciseCategory.AEROBIC),
        ExerciseType("慢跑", 6.0f, ExerciseCategory.AEROBIC),
        ExerciseType("骑行", 6.8f, ExerciseCategory.AEROBIC),
        ExerciseType("游泳", 6.0f, ExerciseCategory.AEROBIC),
        ExerciseType("自由泳", 7.0f, ExerciseCategory.AEROBIC),
        ExerciseType("蛙泳", 5.3f, ExerciseCategory.AEROBIC),
        ExerciseType("跳绳", 10.0f, ExerciseCategory.AEROBIC),
        ExerciseType("有氧操", 5.0f, ExerciseCategory.AEROBIC),
        ExerciseType("爬楼梯", 8.0f, ExerciseCategory.AEROBIC),
        ExerciseType("椭圆机", 5.0f, ExerciseCategory.AEROBIC),
        ExerciseType("划船机", 7.0f, ExerciseCategory.AEROBIC),
        
        // 力量训练 (STRENGTH)
        ExerciseType("力量训练", 5.0f, ExerciseCategory.STRENGTH),
        ExerciseType("举重", 6.0f, ExerciseCategory.STRENGTH),
        ExerciseType("器械训练", 5.5f, ExerciseCategory.STRENGTH),
        ExerciseType("自重训练", 4.0f, ExerciseCategory.STRENGTH),
        ExerciseType("俯卧撑", 3.8f, ExerciseCategory.STRENGTH),
        ExerciseType("深蹲", 5.0f, ExerciseCategory.STRENGTH),
        ExerciseType("平板支撑", 3.5f, ExerciseCategory.STRENGTH),
        
        // 柔韧性训练 (FLEXIBILITY)
        ExerciseType("瑜伽", 3.0f, ExerciseCategory.FLEXIBILITY),
        ExerciseType("普拉提", 3.5f, ExerciseCategory.FLEXIBILITY),
        ExerciseType("拉伸", 2.5f, ExerciseCategory.FLEXIBILITY),
        ExerciseType("太极", 3.0f, ExerciseCategory.FLEXIBILITY),
        
        // 球类运动 (SPORTS)
        ExerciseType("篮球", 6.5f, ExerciseCategory.SPORTS),
        ExerciseType("足球", 7.0f, ExerciseCategory.SPORTS),
        ExerciseType("羽毛球", 5.5f, ExerciseCategory.SPORTS),
        ExerciseType("乒乓球", 4.0f, ExerciseCategory.SPORTS),
        ExerciseType("网球", 7.3f, ExerciseCategory.SPORTS),
        ExerciseType("排球", 4.0f, ExerciseCategory.SPORTS),
        
        // 日常活动 (DAILY)
        ExerciseType("散步", 2.5f, ExerciseCategory.DAILY),
        ExerciseType("站立", 1.5f, ExerciseCategory.DAILY),
        ExerciseType("做家务", 3.3f, ExerciseCategory.DAILY),
        ExerciseType("购物", 2.3f, ExerciseCategory.DAILY),
        ExerciseType("跳舞", 5.0f, ExerciseCategory.DAILY)
    )
}
