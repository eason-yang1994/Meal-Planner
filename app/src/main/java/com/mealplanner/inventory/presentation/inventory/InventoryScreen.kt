package com.mealplanner.inventory.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mealplanner.inventory.domain.model.FoodCategory
import com.mealplanner.inventory.domain.model.InventoryItem
import com.mealplanner.inventory.domain.model.StorageLocation
import com.mealplanner.inventory.domain.model.StockSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * 库存页面
 * 
 * @param navController 导航控制器
 * @param viewModel ViewModel
 */
@Composable
fun InventoryScreen(
    navController: androidx.navigation.NavController,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("食物库存") }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = { viewModel.onEvent(InventoryUiEvent.OnShowAddDialog) }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 临期提醒 Banner
            if (uiState.expiringItems.isNotEmpty()) {
                ExpiringBanner(items = uiState.expiringItems)
            }
            
            // 分类筛选
            CategoryFilter(
                selectedCategory = uiState.selectedCategory,
                onCategoryChange = { category ->
                    viewModel.onEvent(InventoryUiEvent.OnCategoryFilterChange(category))
                }
            )
            
            // 食材列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.items) { item ->
                    InventoryItemCard(
                        item = item,
                        onConsumedClick = { id ->
                            viewModel.onEvent(InventoryUiEvent.OnMarkConsumed(id, true))
                        },
                        onDeleteClick = { id ->
                            viewModel.onEvent(InventoryUiEvent.OnDeleteItem(id))
                        }
                    )
                }
            }
        }
        
        // 添加对话框
        if (uiState.showAddDialog) {
            AddInventoryDialog(
                onDismiss = { viewModel.onEvent(InventoryUiEvent.OnDismissAddDialog) },
                onConfirm = { item ->
                    viewModel.onEvent(InventoryUiEvent.OnAddItem(item))
                }
            )
        }
    }
}

/**
 * 临期提醒 Banner
 */
@Composable
fun ExpiringBanner(items: List<InventoryItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "⚠️ 即将过期提醒",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFFF9800)
            )
            Text(
                text = "有 ${items.size} 件食材即将过期",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * 分类筛选栏
 */
@Composable
fun CategoryFilter(
    selectedCategory: FoodCategory?,
    onCategoryChange: (FoodCategory?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 全部按钮
        Button(
            onClick = { onCategoryChange(null) },
            modifier = Modifier.padding(4.dp)
        ) {
            Text("全部")
        }
        
        // 分类下拉菜单
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // 显示当前选中的分类
            OutlinedTextField(
                value = selectedCategory?.getDisplayName() ?: "按分类筛选",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                FoodCategory.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.getDisplayName()) },
                        onClick = {
                            onCategoryChange(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * 库存项卡片
 */
@Composable
fun InventoryItemCard(
    item: InventoryItem,
    onConsumedClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    val daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), item.expiryDate).toInt()
    val expiryColor = when {
        daysUntilExpiry < 0 -> Color(0xFFF44336) // 已过期 - 红色
        daysUntilExpiry <= 3 -> Color(0xFFFF9800) // 3天内 - 橙色
        else -> Color(0xFF4CAF50) // 正常 - 绿色
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.foodName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${item.quantity}${item.unit}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "分类: ${item.category.getDisplayName()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "位置: ${item.storageLocation.getDisplayName()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "保质期: ${item.expiryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}",
                style = MaterialTheme.typography.bodySmall,
                color = expiryColor
            )
        }
    }
}

/**
 * 添加库存对话框
 */
@Composable
fun AddInventoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (InventoryItem) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("g") }
    var category by remember { mutableStateOf(FoodCategory.VEGETABLE) }
    var storageLocation by remember { mutableStateOf(StorageLocation.REFRIGERATED) }
    var expiryDays by remember { mutableStateOf("7") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新增食材") },
        text = {
            Column {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("食材名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("数量") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("单位") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val item = InventoryItem(
                        id = java.util.UUID.randomUUID().toString(),
                        foodName = foodName,
                        category = category,
                        quantity = quantity.toFloatOrNull() ?: 0f,
                        unit = unit,
                        purchaseDate = LocalDate.now(),
                        expiryDate = LocalDate.now().plusDays(expiryDays.toLongOrNull() ?: 7L),
                        storageLocation = storageLocation,
                        createdFrom = StockSource.MANUAL
                    )
                    onConfirm(item)
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
