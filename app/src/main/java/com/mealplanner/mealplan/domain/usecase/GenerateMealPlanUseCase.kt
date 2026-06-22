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
 * 生成三餐计划用例（规则引擎版本 + LLM 版本）
 * 
 * 根据库存 + 目标热量 + 用户资料生成三餐建议
 * 如果配置了 LLM，则使用 LLM 生成；否则使用规则引擎
 * 
 * @param mealPlanRepository 三餐计划仓库
 * @param inventoryRepository 库存仓库
 * @param calculateTDEEUseCase 计算TDEE用例
 * @param chatWithLlmUseCase 与 LLM 聊天用例（可选）
 */
@Singleton
class GenerateMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository,
    private val inventoryRepository: com.mealplanner.inventory.domain.repository.InventoryRepository,
    private val calculateTDEEUseCase: CalculateTDEEUseCase,
    @ApplicationContext private val context: android.content.Context,
    private val chatWithLlmUseCase: com.mealplanner.llm.domain.usecase.ChatWithLlmUseCase? = null
) {
    
    /**
     * 执行生成三餐计划
     * 
     * @param userProfile 用户资料
     * @param date 日期
     * @param useLlm 是否使用 LLM（如果配置了 LLM 且此参数为 true）
     * @return 生成的三餐计划列表
     */
    suspend operator fun invoke(userProfile: UserProfile, date: LocalDate = LocalDate.now(), useLlm: Boolean = false): Result<List<MealPlan>> {
        // 如果配置了 LLM 且 useLlm 为 true，则使用 LLM 生成
        if (useLlm && chatWithLlmUseCase != null) {
            return generateWithLlm(userProfile, date)
        }
        
        // 否则使用规则引擎生成
        return generateWithRules(userProfile, date)
    }
    
    /**
     * 使用规则引擎生成三餐计划
     */
    private suspend fun generateWithRules(userProfile: UserProfile, date: LocalDate): Result<List<MealPlan>> {
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
     * 使用 LLM 生成三餐计划
     * 
     * 构建 prompt：用户资料 + 库存 + 近期用餐 + 目标热量
     * 调用 LlmRepository.chatCompletion()
     * 解析 LLM 返回的 JSON 格式用餐建议
     */
    private suspend fun generateWithLlm(userProfile: UserProfile, date: LocalDate): Result<List<MealPlan>> {
        return try {
            // 1. 计算目标热量
            val tdee = calculateTDEEUseCase(userProfile)
            val targetCalories = tdee.getOrNull() ?: 2000f
            
            // 2. 获取库存
            val inventoryItems = inventoryRepository.getAllItems().first()
            val inventoryNames = inventoryItems.joinToString(", ") { it.foodName }
            
            // 3. 构建 prompt
            val prompt = buildPrompt(userProfile, inventoryNames, targetCalories)
            
            // 4. 调用 LLM
            val llmResponse = chatWithLlmUseCase!!(prompt)
            val responseText = llmResponse.getOrNull() ?: return Result.failure(
                llmResponse.exceptionOrNull() ?: IllegalStateException("LLM 响应为空")
            )
            
            // 5. 解析 LLM 返回的 JSON
            val mealPlans = parseLlmResponse(responseText, date)
            
            // 6. 保存生成的三餐计划
            mealPlans.forEach { meal ->
                mealPlanRepository.addMealPlan(meal)
            }
            
            Result.success(mealPlans)
        } catch (e: Exception) {
            // LLM 失败，降级到规则引擎
            generateWithRules(userProfile, date)
        }
    }
    
    /**
     * 构建 LLM Prompt
     */
    private fun buildPrompt(userProfile: UserProfile, inventory: String, targetCalories: Float): String {
        return """
            你是一个专业的营养师。请根据以下信息为用户生成一日三餐建议。
            
            用户信息：
            - 性别：${if (userProfile.gender.name == "MALE") "男" else "女"}
            - 年龄：${userProfile.age}
            - 身高：${userProfile.heightCm} cm
            - 体重：${userProfile.currentWeight} kg
            - 目标：${userProfile.goal}
            - 目标热量：${targetCalories.toInt()} 千卡
            
            当前库存：$inventory
            
            请返回 JSON 格式，包含早餐、午餐、晚餐的建议，每个餐包含：
            - dishName: 菜名
            - calories: 热量（千卡）
            - ingredients: 食材列表（包含 name, amount, unit）
            
            格式示例：
            {
                "breakfast": {...},
                "lunch": {...},
                "dinner": {...}
            }
        """.trimIndent()
    }
    
    /**
     * 解析 LLM 响应
     */
    private fun parseLlmResponse(response: String, date: LocalDate): List<MealPlan> {
        return try {
            val json = org.json.JSONObject(response)
            val mealPlans = mutableListOf<MealPlan>()
            
            val mealTypeMap = mapOf(
                "breakfast" to MealType.BREAKFAST,
                "lunch" to MealType.LUNCH,
                "dinner" to MealType.DINNER
            )
            
            mealTypeMap.forEach { (key, mealType) ->
                if (json.has(key)) {
                    val obj = json.getJSONObject(key)
                    val mealPlan = MealPlan(
                        id = java.util.UUID.randomUUID().toString(),
                        date = date,
                        mealType = mealType,
                        dishName = obj.getString("dishName"),
                        calories = obj.getDouble("calories").toFloat(),
                        ingredients = parseIngredientsFromJson(obj.getJSONArray("ingredients")),
                        status = com.mealplanner.mealplan.domain.model.MealStatus.PENDING
                    )
                    mealPlans.add(mealPlan)
                }
            }
            
            mealPlans
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * 解析食材 JSON 数组（LLM 返回）
     */
    private fun parseIngredientsFromJson(jsonArray: org.json.JSONArray): List<Ingredient> {
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
     * 从 assets 加载食谱数据
     */
    private fun loadRecipesFromAssets(): List<RecipeData> {
        return try {
            val inputStream = context.assets.open("recipes.json")
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
