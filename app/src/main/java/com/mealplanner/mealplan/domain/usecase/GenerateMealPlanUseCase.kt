package com.mealplanner.mealplan.domain.usecase

import com.mealplanner.mealplan.domain.model.Ingredient
import com.mealplanner.mealplan.domain.model.MealPlan
import com.mealplanner.mealplan.domain.model.MealType
import com.mealplanner.mealplan.domain.repository.MealPlanRepository
import com.mealplanner.userprofile.domain.model.UserProfile
import com.mealplanner.userprofile.domain.usecase.CalculateTDEEUseCase
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader

/**
 * 生成三餐计划用例（规则引擎版本）
 * 
 * 根据库存 + 目标热量 + 用户资料生成三餐建议
 * 
 * @param mealPlanRepository 三餐计划仓库
 * @param inventoryRepository 库存仓库
 * @param calculateTDEEUseCase 计算TDEE用例
 */
@Singleton
class GenerateMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository,
    private val calculateTDEEUseCase: CalculateTDEEUseCase,
    private val application: android.app.Application
) {
    
    /**
     * 执行生成三餐计划
     * 
     * @param userProfile 用户资料
     * @param date 日期
     * @return 生成的三餐计划列表
     */
    suspend operator fun invoke(userProfile: UserProfile, date: LocalDate = LocalDate.now()): Result<List<MealPlan>> {
        return try {
            // 1. 计算目标热量
            val tdee = calculateTDEEUseCase(userProfile)
            val targetCalories = tdee.getOrNull() ?: 2000f
            
            // 2. 获取库存
            val inventoryItems = inventoryRepository.getAllItems().first()
            
            // 3. 获取最近用餐记录（避免重复）
            val recentMeals = mutableListOf<MealPlan>()
            
            // 4. 加载食谱数据
            val recipes = loadRecipesFromAssets()
            
            // 5. 按热量分配生成三餐
            val mealPlans = mutableListOf<MealPlan>()
            
            val mealDistribution = mapOf(
                MealType.BREAKFAST to 0.3f,
                MealType.LUNCH to 0.4f,
                MealType.DINNER to 0.3f
            )
            
            // 加餐（8%）
            val snackTarget = targetCalories * 0.08f
            
            mealDistribution.forEach { (mealType, ratio) ->
                val targetForMeal = targetCalories * ratio
                
                // 推荐食谱
                val suggestedRecipe = suggestRecipe(
                    recipes = recipes,
                    mealType = mealType,
                    inventoryItems = inventoryItems,
                    targetCalories = targetForMeal
                )
                
                if (suggestedRecipe != null) {
                    val mealPlan = MealPlan(
                        id = java.util.UUID.randomUUID().toString(),
                        date = date,
                        mealType = mealType,
                        dishName = suggestedRecipe.name,
                        calories = suggestedRecipe.calories,
                        ingredients = suggestedRecipe.ingredients,
                        status = com.mealplanner.mealplan.domain.model.MealStatus.PENDING
                    )
                    mealPlans.add(mealPlan)
                }
            }
            
            // 保存生成的三餐计划
            mealPlans.forEach { meal ->
                mealPlanRepository.addMealPlan(meal)
            }
            
            Result.success(mealPlans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 从 assets 加载食谱数据
     */
    private fun loadRecipesFromAssets(): List<RecipeData> {
        return try {
            val inputStream = application.assets.open("recipes.json")
            val reader = InputStreamReader(inputStream)
            val jsonArray = JSONArray(reader.readText())
            val recipes = mutableListOf<RecipeData>()
            
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val recipe = RecipeData(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    category = obj.getString("category"),
                    calories = obj.getDouble("calories_per_serving").toFloat(),
                    ingredients = parseIngredients(obj.getJSONArray("ingredients"))
                )
                recipes.add(recipe)
            }
            
            recipes
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 解析食材JSON数组
     */
    private fun parseIngredients(jsonArray: JSONArray): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            ingredients.add(
                Ingredient(
                    foodName = obj.getString("name"),
                    amount = obj.getDouble("amount").toFloat(),
                    unit = obj.getString("unit")
                )
            )
        }
        return ingredients
    }
    
    /**
     * 推荐食谱
     * 
     * 算法：
     * - 匹配库存中的食材（食材匹配度评分）
     * - 优先使用临期食材（临期加分）
     * - 按目标热量筛选
     */
    private fun suggestRecipe(
        recipes: List<RecipeData>,
        mealType: MealType,
        inventoryItems: List<com.mealplanner.inventory.domain.model.InventoryItem>,
        targetCalories: Float
    ): RecipeData? {
        val inventoryFoodNames = inventoryItems.map { it.foodName }
        
        // 筛选符合条件的食谱
        val scoredRecipes = recipes.map { recipe ->
            var score = 0f
            
            // 热量匹配度（越接近目标越好）
            val calorieDiff = kotlin.math.abs(recipe.calories - targetCalories)
            score += (1.0f - calorieDiff / targetCalories) * 50
            
            // 食材匹配度
            val matchedIngredients = recipe.ingredients.count { ingredient ->
                inventoryFoodNames.any { it.contains(ingredient.foodName) || ingredient.foodName.contains(it) }
            }
            score += (matchedIngredients.toFloat() / recipe.ingredients.size) * 50
            
            // 临期加分（如果食谱的主要食材在库存中且临期）
            val hasExpiringIngredient = recipe.ingredients.any { ingredient ->
                inventoryItems.any { 
                    (it.foodName.contains(ingredient.foodName) || ingredient.foodName.contains(it.foodName)) &&
                    java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), it.expiryDate) <= 3
                }
            }
            if (hasExpiringIngredient) {
                score += 20f
            }
            
            Pair(recipe, score)
        }
        
        // 按分数排序，返回最高分的食谱
        return scoredRecipes.maxByOrNull { it.second }?.first
    }
    
    /**
     * 食谱数据（内部类）
     */
    private data class RecipeData(
        val id: String,
        val name: String,
        val category: String,
        val calories: Float,
        val ingredients: List<Ingredient>
    )
}
