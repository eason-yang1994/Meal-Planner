package com.mealplanner.inventory.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mealplanner.core.database.TypeConverters
import java.time.LocalDate

/**
 * 库存物品 Room 实体
 * 
 * @param id 唯一标识
 * @param foodName 食材名称
 * @param category 食材分类
 * @param quantity 数量
 * @param unit 单位
 * @param purchaseDate 购买日期
 * @param expiryDate 保质期到期日
 * @param storageLocation 存放位置
 * @param createdFrom 创建来源
 * @param isConsumed 是否已消耗
 * @param note 备注
 */
@Entity(tableName = "inventory_items")
@TypeConverters(TypeConverters::class)
data class InventoryEntity(
    @PrimaryKey
    val id: String,
    val foodName: String,
    val category: String,
    val quantity: Float,
    val unit: String,
    val purchaseDate: LocalDate,
    val expiryDate: LocalDate,
    val storageLocation: String,
    val createdFrom: String,
    val isConsumed: Boolean = false,
    val note: String? = null
) {
    /**
     * 转换为领域模型
     * 
     * @return 领域模型 InventoryItem
     */
    fun toDomainModel(): com.mealplanner.inventory.domain.model.InventoryItem {
        return com.mealplanner.inventory.domain.model.InventoryItem(
            id = id,
            foodName = foodName,
            category = com.mealplanner.inventory.domain.model.FoodCategory.valueOf(category),
            quantity = quantity,
            unit = unit,
            purchaseDate = purchaseDate,
            expiryDate = expiryDate,
            storageLocation = com.mealplanner.inventory.domain.model.StorageLocation.valueOf(storageLocation),
            createdFrom = com.mealplanner.inventory.domain.model.StockSource.valueOf(createdFrom),
            isConsumed = isConsumed,
            note = note
        )
    }
    
    companion object {
        /**
         * 从领域模型创建实体
         * 
         * @param domainModel 领域模型
         * @return Room 实体
         */
        fun fromDomainModel(domainModel: com.mealplanner.inventory.domain.model.InventoryItem): InventoryEntity {
            return InventoryEntity(
                id = domainModel.id,
                foodName = domainModel.foodName,
                category = domainModel.category.name,
                quantity = domainModel.quantity,
                unit = domainModel.unit,
                purchaseDate = domainModel.purchaseDate,
                expiryDate = domainModel.expiryDate,
                storageLocation = domainModel.storageLocation.name,
                createdFrom = domainModel.createdFrom.name,
                isConsumed = domainModel.isConsumed,
                note = domainModel.note
            )
        }
    }
}
