package com.mealplanner.llm.domain.usecase

import com.mealplanner.llm.domain.model.LlmConfig
import com.mealplanner.llm.domain.repository.LlmRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * 获取 LLM 配置用例
 * 
 * @param llmRepository LLM 仓库
 */
class GetLlmConfigUseCase @Inject constructor(
    private val llmRepository: LlmRepository
) {
    /**
     * 获取配置
     * 
     * @return LLM 配置流
     */
    operator fun invoke(): Flow<LlmConfig?> {
        return llmRepository.getConfig()
    }
}
