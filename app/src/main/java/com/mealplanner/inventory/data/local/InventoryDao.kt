package com.mealplanner.inventory.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * 库存数据访问对象
 * 
 * 定义库存表的数据库操作
 */
@Dao
interface InventoryDao {
    
    /**
     * 插入库存项
     * 
     * @param item 库存项
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryEntity)
    
    /**
     * 插入多个库存项
     * 
     * @param items 库存项列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryEntity>)
    
    /**
     * 更新库存项
     * 
     * @param item 库存项
     */
    @Update
    suspend fun update(item: InventoryEntity)
    
    /**
     * 删除库存项
     * 
     * @param item 库存项
     */
    @Delete
    suspend fun delete(item: InventoryEntity)
    
    /**
     * 获取所有未消耗的库存项，按保质期升序排列
     * 
     * @return 库存项流
     */
    @Query("SELECT * FROM inventory_items WHERE isConsumed = 0 ORDER BY expiryDate ASC")
    fun getAllOrderedByExpiry(): Flow<List<InventoryEntity>>
    
    /**
     * 根据分类获取库存项
     * 
     * @param category 分类名称
     * @return 库存项流
     */
    @Query("SELECT * FROM inventory_items WHERE category = :category AND isConsumed = 0 ORDER BY expiryDate ASC")
    fun getByCategory(category: String): Flow<List<InventoryEntity>>
    
    /**
     * 获取已消耗的库存项
     * 
     * @return 库存项流
     */
    @Query("SELECT * FROM inventory_items WHERE isConsumed = 1 ORDER BY expiryDate DESC")
    fun getConsumed(): Flow<List<InventoryEntity>>
    
    /**
     * 获取未消耗的库存项
     * 
     * @return 库存项流
     */
    @Query("SELECT * FROM inventory_items WHERE isConsumed = 0")
    fun getNotConsumed(): Flow<List<InventoryEntity>>
    
    /**
     * 根据存放位置获取库存项
     * 
     * @param storageLocation 存放位置名称
     * @return 库存项流
     */
    @Query("SELECT * FROM inventory_items WHERE storageLocation = :storageLocation AND isConsumed = 0 ORDER BY expiryDate ASC")
    fun getByStorageLocation(storageLocation: String): Flow<List<InventoryEntity>>
    
    /**
     * 标记库存项为已消耗
     * 
     * @param id 库存项ID
     * @param consumed 是否已消耗
     */
    @Query("UPDATE inventory_items SET isConsumed = :consumed WHERE id = :id")
    suspend fun updateIsConsumed(id: String, consumed: Boolean)
    
    /**
     * 根据ID获取库存项
     * 
     * @param id 库存项ID
     * @return 库存项
     */
    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getById(id: String): InventoryEntity?
    
    /**
     * 根据食材名称查询库存项
     * 
     * @param foodName 食材名称
     * @return 库存项列表
     */
    @Query("SELECT * FROM inventory_items WHERE foodName = :foodName AND isConsumed = 0 ORDER BY expiryDate ASC")
    suspend fun findByFoodName(foodName: String): List<InventoryEntity>
    
    /**
     * 获取即将过期的库存项
     * 
     * @param expiryDate 到期日期上限
     * @return 库存项列表
     */
    @Query("SELECT * FROM inventory_items WHERE expiryDate <= :expiryDate AND isConsumed = 0 ORDER BY expiryDate ASC")
    suspend fun getExpiringItems(expiryDate: LocalDate): List<InventoryEntity>
}
