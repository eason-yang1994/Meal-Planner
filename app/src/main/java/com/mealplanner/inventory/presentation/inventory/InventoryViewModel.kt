package com.mealplanner.inventory.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealplanner.inventory.domain.model.FoodCategory
import com.mealplanner.inventory.domain.model.InventoryItem
import com.mealplanner.inventory.domain.usecase.AddInventoryItemUseCase
import com.mealplanner.inventory.domain.usecase.DeleteInventoryItemUseCase
import com.mealplanner.inventory.domain.usecase.GetExpiringItemsUseCase
import com.mealplanner.inventory.domain.usecase.GetInventoryItemsUseCase
import com.mealplanner.inventory.domain.usecase.MarkItemConsumedUseCase
import com.mealplanner.inventory.domain.usecase.UpdateInventoryItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * 库存页面 ViewModel
 * 
 * @param getInventoryItemsUseCase 获取库存项用例
 * @param addInventoryItemUseCase 添加库存项用例
 * @param updateInventoryItemUseCase 更新库存项用例
 * @param deleteInventoryItemUseCase 删除库存项用例
 * @param getExpiringItemsUseCase 获取即将过期项用例
 * @param markItemConsumedUseCase 标记已消耗用例
 */
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getInventoryItemsUseCase: GetInventoryItemsUseCase,
    private val addInventoryItemUseCase: AddInventoryItemUseCase,
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val deleteInventoryItemUseCase: DeleteInventoryItemUseCase,
    private val getExpiringItemsUseCase: GetExpiringItemsUseCase,
    private val markItemConsumedUseCase: MarkItemConsumedUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()
    
    init {
        loadInventoryItems()
        loadExpiringItems()
    }
    
    /**
     * 处理 UI 事件
     * 
     * @param event UI 事件
     */
    fun onEvent(event: InventoryUiEvent) {
        when (event) {
            is InventoryUiEvent.OnAddItem -> addItem(event.item)
            is InventoryUiEvent.OnUpdateItem -> updateItem(event.item)
            is InventoryUiEvent.OnDeleteItem -> deleteItem(event.id)
            is InventoryUiEvent.OnMarkConsumed -> markConsumed(event.id, event.consumed)
            is InventoryUiEvent.OnShowAddDialog -> showAddDialog()
            is InventoryUiEvent.OnDismissAddDialog -> dismissAddDialog()
            is InventoryUiEvent.OnCategoryFilterChange -> updateCategoryFilter(event.category)
        }
    }
    
    /**
     * 加载库存项
     */
    private fun loadInventoryItems() {
        getInventoryItemsUseCase()
            .onEach { items ->
                _uiState.value = _uiState.value.copy(
                    items = filterItemsByCategory(items, _uiState.value.selectedCategory)
                )
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * 加载即将过期的库存项
     */
    private fun loadExpiringItems() {
        getExpiringItemsUseCase(3)
            .onEach { items ->
                _uiState.value = _uiState.value.copy(
                    expiringItems = items
                )
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
            .launchIn(viewModelScope)
    }
    
    /**
     * 添加库存项
     * 
     * @param item 库存项
     */
    private fun addItem(item: InventoryItem) {
        viewModelScope.launch {
            val result = addInventoryItemUseCase(item)
            if (result.isSuccess) {
                dismissAddDialog()
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 更新库存项
     * 
     * @param item 库存项
     */
    private fun updateItem(item: InventoryItem) {
        viewModelScope.launch {
            val result = updateInventoryItemUseCase(item)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 删除库存项
     * 
     * @param id 库存项ID
     */
    private fun deleteItem(id: String) {
        viewModelScope.launch {
            val result = deleteInventoryItemUseCase(id)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 标记库存项已消耗
     * 
     * @param id 库存项ID
     * @param consumed 是否已消耗
     */
    private fun markConsumed(id: String, consumed: Boolean) {
        viewModelScope.launch {
            val result = markItemConsumedUseCase(id, consumed)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }
    
    /**
     * 显示添加对话框
     */
    private fun showAddDialog() {
        _uiState.value = _uiState.value.copy(
            showAddDialog = true
        )
    }
    
    /**
     * 关闭添加对话框
     */
    private fun dismissAddDialog() {
        _uiState.value = _uiState.value.copy(
            showAddDialog = false
        )
    }
    
    /**
     * 更新分类筛选
     * 
     * @param category 分类
     */
    private fun updateCategoryFilter(category: FoodCategory?) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            items = filterItemsByCategory(_uiState.value.items, category)
        )
    }
    
    /**
     * 根据分类筛选库存项
     * 
     * @param items 库存项列表
     * @param category 分类
     * @return 筛选后的列表
     */
    private fun filterItemsByCategory(items: List<InventoryItem>, category: FoodCategory?): List<InventoryItem> {
        return if (category == null) {
            items
        } else {
            items.filter { it.category == category }
        }
    }
}

/**
 * 库存页面 UI 状态
 * 
 * @param items 库存项列表
 * @param expiringItems 即将过期的库存项
 * @param showAddDialog 是否显示添加对话框
 * @param selectedCategory 选中的分类筛选
 * @param errorMessage 错误信息
 */
data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val expiringItems: List<InventoryItem> = emptyList(),
    val showAddDialog: Boolean = false,
    val selectedCategory: FoodCategory? = null,
    val errorMessage: String? = null
)

/**
 * 库存页面 UI 事件
 */
sealed class InventoryUiEvent {
    /**
     * 添加库存项
     */
    data class OnAddItem(val item: InventoryItem) : InventoryUiEvent()
    
    /**
     * 更新库存项
     */
    data class OnUpdateItem(val item: InventoryItem) : InventoryUiEvent()
    
    /**
     * 删除库存项
     */
    data class OnDeleteItem(val id: String) : InventoryUiEvent()
    
    /**
     * 标记已消耗
     */
    data class OnMarkConsumed(val id: String, val consumed: Boolean) : InventoryUiEvent()
    
    /**
     * 显示添加对话框
     */
    object OnShowAddDialog : InventoryUiEvent()
    
    /**
     * 关闭添加对话框
     */
    object OnDismissAddDialog : InventoryUiEvent()
    
    /**
     * 分类筛选变化
     */
    data class OnCategoryFilterChange(val category: FoodCategory?) : InventoryUiEvent()
}
