package com.mealplanner.llm.domain.usecase

import com.mealplanner.llm.domain.model.LlmConfig
import com.mealplanner.llm.domain.repository.LlmRepository
import javax.inject.Inject

/**
 * 保存 LLM 配置用例
 * 
 * @param llmRepository LLM 仓库
 */
class SaveLlmConfigUseCase @Inject constructor(
    private val llmRepository: LlmRepository
) {
    /**
     * 保存配置
     * 
     * @param config LLM 配置
     * @return 操作结果
     */
    suspend operator fun invoke(config: LlmConfig): Result<Unit> {
        return llmRepository.saveConfig(config)
    }
}
