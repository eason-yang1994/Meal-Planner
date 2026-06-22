package com.mealplanner.mealplan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mealplanner.core.database.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 三餐计划 Room 实体
 * 
 * @param id 唯一标识
 * @param date 日期
 * @param mealType 餐类型
 * @param dishName 菜名
 * @param calories 热量
 * @param ingredientsJson 食材列表（JSON字符串）
 * @param status 状态
 * @param isHomeMeal 是否在家用餐
 * @param confirmedAt 确认时间
 */
@Entity(tableName = "meal_plans")
@TypeConverters(TypeConverters::class)
data class MealPlanEntity(
    @PrimaryKey
    val id: String,
    val date: LocalDate,
    val mealType: String,
    val dishName: String,
    val calories: Float,
    val ingredientsJson: String,
    val status: String,
    val isHomeMeal: Boolean = true,
    val confirmedAt: LocalDateTime? = null
) {
    /**
     * 转换为领域模型
     * 
     * @return 领域模型 MealPlan
     */
    fun toDomainModel(): com.mealplanner.mealplan.domain.model.MealPlan {
        val gson = Gson()
        val ingredientType = object : TypeToken<List<com.mealplanner.mealplan.domain.model.Ingredient>>() {}.type
        val ingredients: List<com.mealplanner.mealplan.domain.model.Ingredient> = 
            gson.fromJson(ingredientsJson, ingredientType) ?: emptyList()
        
        return com.mealplanner.mealplan.domain.model.MealPlan(
            id = id,
            date = date,
            mealType = com.mealplanner.mealplan.domain.model.MealType.valueOf(mealType),
            dishName = dishName,
            calories = calories,
            ingredients = ingredients,
            status = com.mealplanner.mealplan.domain.model.MealStatus.valueOf(status),
            isHomeMeal = isHomeMeal,
            confirmedAt = confirmedAt
        )
    }
    
    companion object {
        /**
         * 从领域模型创建实体
         * 
         * @param domainModel 领域模型
         * @return Room 实体
         */
        fun fromDomainModel(domainModel: com.mealplanner.mealplan.domain.model.MealPlan): MealPlanEntity {
            val gson = Gson()
            val ingredientsJson = gson.toJson(domainModel.ingredients)
            
            return MealPlanEntity(
                id = domainModel.id,
                date = domainModel.date,
                mealType = domainModel.mealType.name,
                dishName = domainModel.dishName,
                calories = domainModel.calories,
                ingredientsJson = ingredientsJson,
                status = domainModel.status.name,
                isHomeMeal = domainModel.isHomeMeal,
                confirmedAt = domainModel.confirmedAt
            )
        }
    }
}
