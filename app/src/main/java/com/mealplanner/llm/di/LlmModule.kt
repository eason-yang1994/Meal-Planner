package com.mealplanner.llm.di

import com.mealplanner.llm.data.LlmRepositoryImpl
import com.mealplanner.llm.data.network.LlmApiService
import com.mealplanner.llm.domain.repository.LlmRepository
import com.mealplanner.llm.domain.usecase.ChatWithLlmUseCase
import com.mealplanner.llm.domain.usecase.GetLlmConfigUseCase
import com.mealplanner.llm.domain.usecase.SaveLlmConfigUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * LLM 模块
 * 
 * 提供 LLM 相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object LlmModule {
    
    /**
     * 提供 LlmApiService
     * 
     * 注意：实际实现中需要根据 LlmConfig 动态创建
     */
    @Provides
    @Singleton
    fun provideLlmApiService(): LlmApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                // TODO: Add Authorization header based on config
                chain.proceed(chain.request())
            }
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")  // Default, should be configurable
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        return retrofit.create(LlmApiService::class.java)
    }
    
    /**
     * 提供 LlmRepository
     */
    @Provides
    @Singleton
    fun provideLlmRepository(): LlmRepository {
        return LlmRepositoryImpl()
    }
    
    /**
     * 提供 ChatWithLlmUseCase
     */
    @Provides
    fun provideChatWithLlmUseCase(
        llmRepository: LlmRepository
    ): ChatWithLlmUseCase {
        return ChatWithLlmUseCase(llmRepository)
    }
    
    /**
     * 提供 SaveLlmConfigUseCase
     */
    @Provides
    fun provideSaveLlmConfigUseCase(
        llmRepository: LlmRepository
    ): SaveLlmConfigUseCase {
        return SaveLlmConfigUseCase(llmRepository)
    }
    
    /**
     * 提供 GetLlmConfigUseCase
     */
    @Provides
    fun provideGetLlmConfigUseCase(
        llmRepository: LlmRepository
    ): GetLlmConfigUseCase {
        return GetLlmConfigUseCase(llmRepository)
    }
}
