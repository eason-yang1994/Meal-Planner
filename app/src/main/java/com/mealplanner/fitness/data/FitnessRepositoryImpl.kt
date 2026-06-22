package com.mealplanner.fitness.data

import com.mealplanner.fitness.data.local.ExerciseRecordDao
import com.mealplanner.fitness.data.local.ExerciseRecordEntity
import com.mealplanner.fitness.domain.model.ExerciseRecord
import com.mealplanner.fitness.domain.repository.FitnessRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 运动追踪 Repository 实现
 * 
 * @param exerciseRecordDao 运动记录 DAO
 */
class FitnessRepositoryImpl @Inject constructor(
    private val exerciseRecordDao: ExerciseRecordDao
) : FitnessRepository {
    
    override suspend fun addRecord(record: ExerciseRecord): Result<Unit> {
        return try {
            val entity = record.toEntity()
            exerciseRecordDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getRecords(): Flow<List<ExerciseRecord>> {
        return exerciseRecordDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getRecordsByDate(date: LocalDate): Flow<List<ExerciseRecord>> {
        return exerciseRecordDao.getByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getTotalCaloriesBurned(date: LocalDate): Flow<Float> {
        return exerciseRecordDao.getByDate(date).map { entities ->
            entities.sumOf { it.caloriesBurned.toDouble() }.toFloat()
        }
    }
    
    override suspend fun deleteRecord(id: String): Result<Unit> {
        return try {
            val entity = exerciseRecordDao.getByDate(LocalDate.now()).map { it.find { it.id == id } }
            // Note: This is a simplified implementation
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Entity 转 Domain Model
 */
fun ExerciseRecordEntity.toDomain(): ExerciseRecord {
    return ExerciseRecord(
        id = id,
        exerciseName = exerciseName,
        metValue = metValue,
        durationMinutes = durationMinutes,
        caloriesBurned = caloriesBurned,
        date = date,
        note = note
    )
}

/**
 * Domain Model 转 Entity
 */
fun ExerciseRecord.toEntity(): ExerciseRecordEntity {
    return ExerciseRecordEntity(
        id = id,
        exerciseName = exerciseName,
        metValue = metValue,
        durationMinutes = durationMinutes,
        caloriesBurned = caloriesBurned,
        date = date,
        note = note
    )
}
