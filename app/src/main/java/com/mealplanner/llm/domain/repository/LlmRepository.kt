package com.mealplanner.llm.domain.repository

import com.mealplanner.llm.domain.model.LlmConfig
import kotlinx.coroutines.flow.Flow

/**
 * LLM Repository 接口
 * 
 * 定义 LLM 配置和数据操作
 */
interface LlmRepository {
    
    /**
     * 聊天补全
     * 
     * @param prompt 提示词
     * @return 操作结果（包含 LLM 响应文本）
     */
    suspend fun chatCompletion(prompt: String): Result<String>
    
    /**
     * 保存配置
     * 
     * @param config LLM 配置
     * @return 操作结果
     */
    suspend fun saveConfig(config: LlmConfig): Result<Unit>
    
    /**
     * 获取配置
     * 
     * @return LLM 配置流
     */
    fun getConfig(): Flow<LlmConfig?>
}
