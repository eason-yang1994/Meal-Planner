package com.mealplanner.inventory.data

import com.mealplanner.inventory.data.local.InventoryDao
import com.mealplanner.inventory.data.local.InventoryEntity
import com.mealplanner.inventory.domain.model.InventoryItem
import com.mealplanner.inventory.domain.repository.InventoryRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 库存仓库实现
 * 
 * @param inventoryDao 库存数据访问对象
 */
@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val inventoryDao: InventoryDao
) : InventoryRepository {
    
    override suspend fun addItem(item: InventoryItem): Result<Unit> {
        return try {
            val entity = InventoryEntity.fromDomainModel(item)
            inventoryDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateItem(item: InventoryItem): Result<Unit> {
        return try {
            val entity = InventoryEntity.fromDomainModel(item)
            inventoryDao.update(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteItem(id: String): Result<Unit> {
        return try {
            val entity = inventoryDao.getById(id)
            if (entity != null) {
                inventoryDao.delete(entity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getAllItems(): Flow<List<InventoryItem>> {
        return inventoryDao.getNotConsumed().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getItemsByExpiry(): Flow<List<InventoryItem>> {
        return inventoryDao.getAllOrderedByExpiry().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun markAsConsumed(id: String, consumed: Boolean): Result<Unit> {
        return try {
            inventoryDao.updateIsConsumed(id, consumed)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getExpiringItems(days: Int): Flow<List<InventoryItem>> {
        val expiryDate = LocalDate.now().plusDays(days.toLong())
        return kotlinx.coroutines.flow.flow {
            val entities = inventoryDao.getExpiringItems(expiryDate)
            emit(entities.map { it.toDomainModel() })
        }
    }
}
