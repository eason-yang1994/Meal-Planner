package com.mealplanner.fitness.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * 运动记录 Entity
 * 
 * @param id 唯一标识
 * @param exerciseName 运动名称
 * @param metValue MET值（代谢当量）
 * @param durationMinutes 运动时长（分钟）
 * @param caloriesBurned 消耗热量（千卡）
 * @param date 运动日期
 * @param note 备注（可选）
 */
@Entity(tableName = "exercise_records")
data class ExerciseRecordEntity(
    @PrimaryKey
    val id: String,
    val exerciseName: String,
    val metValue: Float,
    val durationMinutes: Int,
    val caloriesBurned: Float,
    val date: LocalDate,
    val note: String? = null
)
