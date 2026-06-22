package com.mealplanner.mealplan.data

import com.mealplanner.mealplan.data.local.MealPlanDao
import com.mealplanner.mealplan.data.local.MealPlanEntity
import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 三餐计划仓库实现
 * 
 * @param mealPlanDao 三餐计划数据访问对象
 */
@Singleton
class MealPlanRepositoryImpl @Inject constructor(
    private val mealPlanDao: MealPlanDao
) : MealPlanRepository {
    
    override suspend fun addMealPlan(meal: MealPlan): Result<Unit> {
        return try {
            val entity = MealPlanEntity.fromDomainModel(meal)
            mealPlanDao.insert(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateMealPlan(meal: MealPlan): Result<Unit> {
        return try {
            val entity = MealPlanEntity.fromDomainModel(meal)
            mealPlanDao.update(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteMealPlan(id: String): Result<Unit> {
        return try {
            val entity = mealPlanDao.getById(id)
            if (entity != null) {
                mealPlanDao.delete(entity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getMealPlansForDate(date: LocalDate): Flow<List<MealPlan>> {
        return mealPlanDao.getByDate(date).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getRecentMeals(days: Int): Flow<List<MealPlan>> {
        val startDate = LocalDate.now().minusDays(days.toLong())
        return mealPlanDao.getRecentMeals(startDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun confirmMealPlan(id: String): Result<Unit> {
        return try {
            val entity = mealPlanDao.getById(id)
            if (entity != null) {
                val updatedEntity = entity.copy(
                    status = "CONFIRMED",
                    confirmedAt = java.time.LocalDateTime.now()
                )
                mealPlanDao.update(updatedEntity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
