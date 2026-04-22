# DDL to Java Code Generator Plugin

<div align="center">
  <img src="https://pan.imgbed.link/file/106516" alt="DDL to Java Code Generator Plugin" />
  <p>
    <img src="https://img.shields.io/badge/IntelliJ%20IDEA-2021.3+-blue.svg" alt="Support IDEA">
    <img src="https://img.shields.io/badge/JDK-1.8%20%2F%2011%20%2F%2017-orange.svg" alt="Support JDK">
    <img src="https://img.shields.io/badge/MyBatis--Plus-Latest-green.svg" alt="Support MP">
  </p>
  <p>🚀 <b>拒绝重复劳动：从 DDL 到全套后端架构，只需一键。</b></p>
</div>

---

## 📖 项目介绍

**DDL to Java Code Generator** 是一款为 Java 开发者量身定制的 IntelliJ IDEA 插件。它通过深度解析 SQL DDL 语句，自动化生成符合工业标准的后端代码。

**为什么选择它？**
在传统的开发流程中，根据数据库表创建 Entity、Mapper、Service 和 Controller 需要耗费大量时间且容易出错。本插件将这一过程缩短至秒级，确保代码风格统一，极大提升了开发效率。

## ✨ 核心特性

- **🚀 全链路生成**：覆盖从 `Controller`、`Service`、`Mapper` 到 `Entity`、`DTO`、`VO` 的完整开发闭环。
- **🔌 MyBatis-Plus 原生适配**：自动集成 MP 特性，生成包含 `BaseMapper` 和 `IService` 的标准架构。
- **📂 智能包结构管理**：根据表名自动创建子包（如 `dto/user`），彻底告别手动创建文件夹的烦恼。
- **🛠️ 基础设施一键配置**：内置常用的跨域配置（CorsConfig）与 `pom.xml` 核心依赖模板。
- **⚡ 零配置成本**：无需 YAML 或 JSON 配置，直接解析 SQL 注释作为 Java 字段注释，实现所见即所得。

## 🛠️ 生成项说明

| 组件层级 | 生成内容 | 核心技术点 |
| :--- | :--- | :--- |
| **Controller** | 基础 CRUD 接口 | RESTful 风格，预留业务入口 |
| **Service** | 接口及实现类 (Impl) | 继承 MyBatis-Plus 的 `IService` |
| **Mapper** | Mapper 接口与 XML | 自动映射 `ResultMap` 与字段 |
| **Model/Entity** | 数据库实体类 | 自动转换下划线命名为驼峰命名 |
| **DTO/VO** | 数据传输与展示对象 | 自动按表名进行分包归类 |
| **Configuration** | 跨域与项目配置 | 解决前后端联调痛点 |

## 🚀 快速开始

1. **选择 SQL 文件**：在 IDEA 中打开 `.sql` 文件。
2. **触发生成**：在编辑器上方工具栏找到插件按钮，或使用快捷键启动。
3. **选择模板**：在 UI 界面勾选所需的代码模块。
4. **即刻可用**：插件将在项目根目录下的 `generator` 文件夹中输出代码，直接拖入项目即可使用。

## 📂 生成目录示例

```text
generator/
├── controller/        # 表现层
├── service/           # 业务接口层
│   └── impl/          # 业务实现层
├── mapper/            # 持久层接口
├── entity/            # 数据库实体
├── dto/               # 数据传输对象（按表名分包）
│   └── user/
├── vo/                # 视图对象
└── config/            # 跨域等基础配置
````

## 🖼️ UI 预览

<div align="center">
  <img src="https://pan.imgbed.link/file/104699" alt="功能示例" />
</div>

## 📅 迭代计划 (Roadmap)

  - [ ] **Common Result**：自动生成统一返回体 `Result<T>`。
  - [ ] **Smart Enum**：解析 SQL `COMMENT` 中的状态值自动生成 Java 枚举。
  - [ ] **Global Exception**：一键植入全局异常处理逻辑。
  - [ ] **Validator**：根据 SQL 字段约束（如 NOT NULL）自动生成 Bean Validation 注解。
  - [ ] **Utils**：集成高频使用的工具类（JWT、Redis、Date）。

## 🤝 贡献与反馈

如果您有任何想法或建议，欢迎通过以下方式联系：

  - **作者**：香香
  - **项目反馈**：[提交 Issue](https://github.com/xiangxiang62/genCodeByDDL/issues)

-----

如果这个插件帮到了你，请给一个 Star ⭐
