package com.mealplanner.llm.data.network

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * LLM API Service
 * 
 * 定义与 OpenAI 兼容 API 的接口
 */
interface LlmApiService {
    
    /**
     * 聊天补全接口
     * 
     * @param request 请求体
     * @return 响应体
     */
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Body request: LlmRequest): LlmResponse
}

/**
 * LLM 请求体
 * 
 * @param model 模型名称
 * @param messages 消息列表
 * @param temperature 温度参数（控制随机性）
 */
data class LlmRequest(
    val model: String,
    val messages: List<LlmMessage>,
    val temperature: Float = 0.7f
)

/**
 * LLM 消息
 * 
 * @param role 角色（system/user/assistant）
 * @param content 内容
 */
data class LlmMessage(
    val role: String,
    val content: String
)

/**
 * LLM 响应体
 * 
 * @param choices 选择列表
 */
data class LlmResponse(
    val choices: List<LlmChoice>
)

/**
 * LLM 选择
 * 
 * @param message 消息
 */
data class LlmChoice(
    val message: LlmMessage
)
