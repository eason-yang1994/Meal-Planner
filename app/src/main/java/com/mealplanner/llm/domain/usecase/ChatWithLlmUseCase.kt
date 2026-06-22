package com.mealplanner.llm.domain.usecase

import com.mealplanner.llm.domain.repository.LlmRepository
import javax.inject.Inject

/**
 * 与 LLM 聊天用例
 * 
 * @param llmRepository LLM 仓库
 */
class ChatWithLlmUseCase @Inject constructor(
    private val llmRepository: LlmRepository
) {
    /**
     * 执行聊天
     * 
     * @param prompt 提示词
     * @return 操作结果（包含 LLM 响应文本）
     */
    suspend operator fun invoke(prompt: String): Result<String> {
        return llmRepository.chatCompletion(prompt)
    }
}
