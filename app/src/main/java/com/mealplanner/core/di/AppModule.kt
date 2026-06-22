package com.mealplanner.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 * 应用全局 DI Module
 * 
 * 提供全局依赖：DataStore、OkHttp、Retrofit 等
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * 提供 DataStore 实例
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
    
    /**
     * 提供 OkHttpClient 实例
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                okhttp3.logging.HttpLoggingInterceptor().apply {
                    level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }
    
    /**
     * 提供 Retrofit 实例
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // 后续修改为实际 API 地址
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(okhttp3.MediaType.get("application/json")))
            .build()
    }
}

// DataStore 扩展
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "meal_planner_prefs")