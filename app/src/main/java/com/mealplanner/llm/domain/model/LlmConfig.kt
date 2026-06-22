package com.mealplanner.llm.domain.model

/**
 * LLM 配置
 * 
 * @param provider 提供商（OPENAI/GEMINI/CLAUDE/CUSTOM）
 * @param apiKey API 密钥
 * @param baseUrl 基础 URL
 * @param model 模型名称
 */
data class LlmConfig(
    val provider: String,
    val apiKey: String,
    val baseUrl: String,
    val model: String
) {
    companion object {
        const val PROVIDER_OPENAI = "OPENAI"
        const val PROVIDER_GEMINI = "GEMINI"
        const val PROVIDER_CLAUDE = "CLAUDE"
        const val PROVIDER_CUSTOM = "CUSTOM"
    }
}
