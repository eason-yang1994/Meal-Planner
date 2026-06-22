package com.mealplanner.weight.data

import com.mealplanner.weight.data.local.WeightRecordDao
import com.mealplanner.weight.data.local.WeightRecordEntity
import com.mealplanner.weight.domain.model.WeightRecord
import com.mealplanner.weight.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 体重仓库实现
 * 
 * @param weightRecordDao 体重记录 DAO
 */
@Singleton
class WeightRepositoryImpl @Inject constructor(
    private val weightRecordDao: WeightRecordDao
) : WeightRepository {
    
    override suspend fun addRecord(record: WeightRecord): Result<Unit> {
        return try {
            val entity = WeightRecordEntity.fromDomainModel(record)
            weightRecordDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getRecords(): Flow<List<WeightRecord>> {
        return weightRecordDao.getAllOrderedByDate().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getRecordsInRange(start: LocalDate, end: LocalDate): Flow<List<WeightRecord>> {
        // 注意：这里需要在 DAO 中添加对应的 Flow 查询方法
        // 暂时返回所有记录，后续优化
        return weightRecordDao.getAllOrderedByDate().map { entities ->
            entities
                .map { it.toDomainModel() }
                .filter { it.date in start..end }
        }
    }
    
    override fun getLatestRecord(): Flow<WeightRecord?> {
        return kotlinx.coroutines.flow.flow {
            val entity = weightRecordDao.getLatest()
            emit(entity?.toDomainModel())
        }
    }
    
    override suspend fun deleteRecord(id: String): Result<Unit> {
        return try {
            val entity = weightRecordDao.getById(id)
            if (entity != null) {
                weightRecordDao.delete(entity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
