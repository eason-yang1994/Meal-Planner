package com.mealplanner.weight.domain.usecase

import com.mealplanner.weight.domain.model.WeightRecord
import com.mealplanner.weight.domain.repository.WeightRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * 添加体重记录用例
 */
class AddWeightRecordUseCase @Inject constructor(
    private val weightRepository: WeightRepository
) {
    /**
     * 添加体重记录
     * 
     * @param weightKg 体重（公斤）
     * @param note 备注（可选）
     * @return 操作结果
     */
    suspend operator fun invoke(weightKg: Float, note: String? = null): Result<Unit> {
        val record = WeightRecord(
            id = UUID.randomUUID().toString(),
            date = LocalDate.now(),
            weightKg = weightKg,
            note = note,
            createdAt = LocalDateTime.now()
        )
        return weightRepository.addRecord(record)
    }
}
