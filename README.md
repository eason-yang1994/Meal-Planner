# Meal Planner - 三餐规划 Android App

三餐规划是一款帮助用户管理每日饮食、追踪体重变化、智能规划三餐的 Android 应用。

## 功能特性

- 🍽️ 智能三餐规划（基于库存 + 规则引擎 / LLM）
- ⚖️ 体重追踪与 BMI 计算
- 📦 食物库存管理（条码扫描 + 账单 OCR 导入）
- 🛒 自动生成购物清单
- 🏃 运动记录与热量消耗计算
- 📊 每日/每周/每月报告
- 🔔 智能通知提醒

## 技术栈

- **语言**: Kotlin 2.0.0
- **UI框架**: Jetpack Compose (Material3)
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **本地数据库**: Room
- **图表**: Vico
- **网络**: Retrofit + OkHttp
- **OCR**: ML Kit
- **条码扫描**: ML Kit + CameraX

## 项目结构

采用功能模块分包，每个模块包含完整的 Presentation-Domain-Data 三层：

```
app/src/main/java/com/mealplanner/
├── core/           # 核心模块（跨模块共享）
├── userprofile/    # 用户设置模块
├── weight/         # 体重追踪模块
├── inventory/      # 食物库存模块
├── mealplan/       # 三餐规划模块
├── shopping/       # 购物清单模块
├── fitness/        # 运动追踪模块
├── reports/        # 报告生成模块
├── notifications/  # 通知系统模块
└── integrations/   # 外部集成模块
```

## 开始使用

### 环境要求

- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- Android SDK 34
- Gradle 8.5+

### 构建步骤

1. 克隆项目
```bash
git clone https://github.com/yourusername/meal-planner-android.git
```

2. 打开 Android Studio，选择 `Open an Existing Project`

3. 等待 Gradle 同步完成

4. 运行应用
```bash
./gradlew assembleDebug
```

## 开发计划

- **Phase 0**: 数据准备（食材营养数据、食谱数据）
- **Phase 1**: 项目基建（Week 1）
- **Phase 2**: 核心功能（Week 2-4）
- **Phase 3**: 增强功能（Week 5-7）
- **Phase 4**: 集成测试与发布（Week 8-10）

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

MIT License

## 联系方式

如有问题，请提交 Issue 或联系开发团队。
