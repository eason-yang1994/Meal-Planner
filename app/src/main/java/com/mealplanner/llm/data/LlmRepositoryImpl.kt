package com.mealplanner.llm.data

import com.mealplanner.llm.data.network.LlmApiService
import com.mealplanner.llm.domain.model.LlmConfig
import com.mealplanner.llm.domain.repository.LlmRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * LLM Repository 实现
 * 
 * 使用 Retrofit + OkHttp 调用 OpenAI 兼容 API
 * 
 * @param config LLM 配置（可选，如果未配置则使用默认值）
 */
class LlmRepositoryImpl @Inject constructor(
    private val config: LlmConfig? = null
) : LlmRepository {
    
    private var apiService: LlmApiService? = null
    
    init {
        if (config != null) {
            apiService = createApiService(config)
        }
    }
    
    /**
     * 创建 API Service
     */
    private fun createApiService(config: LlmConfig): LlmApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${config.apiKey}")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        return retrofit.create(LlmApiService::class.java)
    }
    
    override suspend fun chatCompletion(prompt: String): Result<String> {
        return try {
            val service = apiService ?: return Result.failure(
                IllegalStateException("LLM 未配置")
            )
            
            val request = com.mealplanner.llm.data.network.LlmRequest(
                model = config?.model ?: "gpt-3.5-turbo",
                messages = listOf(
                    com.mealplanner.llm.data.network.LlmMessage(
                        role = "user",
                        content = prompt
                    )
                )
            )
            
            val response = service.chatCompletion(request)
            val content = response.choices.firstOrNull()?.message?.content
                ?: return Result.failure(IllegalStateException("LLM 响应为空"))
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveConfig(config: LlmConfig): Result<Unit> {
        return try {
            // TODO: Save to DataStore
            apiService = createApiService(config)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getConfig(): Flow<LlmConfig?> {
        // TODO: Load from DataStore
        return flowOf(config)
    }
}
