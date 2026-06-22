package com.mealplanner.weight.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 体重记录 Room 实体
 * 
 * @param id 主键
 * @param date 记录日期
 * @param weightKg 体重（公斤）
 * @param note 备注（可选）
 * @param createdAt 创建时间
 */
@Entity(tableName = "weight_records")
@TypeConverters(com.mealplanner.core.database.TypeConverters::class)
data class WeightRecordEntity(
    @PrimaryKey
    val id: String,
    val date: LocalDate,
    val weightKg: Float,
    val note: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * 转换为领域模型
     * 
     * @return 领域模型
     */
    fun toDomainModel(): com.mealplanner.weight.domain.model.WeightRecord {
        return com.mealplanner.weight.domain.model.WeightRecord(
            id = id,
            date = date,
            weightKg = weightKg,
            note = note,
            createdAt = createdAt
        )
    }
    
    companion object {
        /**
         * 从领域模型创建实体
         * 
         * @param domainModel 领域模型
         * @return Room 实体
         */
        fun fromDomainModel(domainModel: com.mealplanner.weight.domain.model.WeightRecord): WeightRecordEntity {
            return WeightRecordEntity(
                id = domainModel.id,
                date = domainModel.date,
                weightKg = domainModel.weightKg,
                note = domainModel.note,
                createdAt = domainModel.createdAt
            )
        }
    }
}
