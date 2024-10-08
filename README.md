<div align="center">
  <img src="https://pan.imgbed.link/file/106516" alt="DDL to Java Code Generator Plugin" />
</div>

# DDL to Java Code Generator Plugin

> DDL 转 Java 代码生成器插件
>
> 作者: 香香

## 介绍



这个 IntelliJ IDEA 插件可以从 SQL 文件自动生成 Java 代码。支持生成 Java 实体类（Entity Classes）、控制器类（Controller Classes）和数据传输对象（DTOs）等常规代码。插件简化了从数据库表结构到 Java 代码的转换过程，使开发者能够快速创建符合项目需求的代码文件。


## 使用方法

1. **打开 SQL 文件**：
   在 IntelliJ IDEA 中打开您想要生成代码的 `.sql` 文件。

2. **执行插件**：
    - 点击菜单栏中的插件按钮或从工具栏中找到插件图标以启动代码生成。

3. **选择生成的代码类型**：
    - 插件会弹出一个对话框，允许您选择生成的代码类型：
        - **Controller**：生成控制器类代码。
        - **Model**：生成与数据库表对应的实体类。
        - **DTO**：生成数据传输对象（DTO）类，并按表名分类存放。
        - **VO**：生成数据视图对象，并按照表名进行分类。
        - **Mapper**：生成持久层类代码（基于 MyBatisPlus）。
        - **Mapper.xml**：生成持久层类代码（基于 MyBatisPlus）。
        - **Service**：生成逻辑层以及实现类类代码。
        - **跨域配置**：配置跨域请求，允许指定来源进行跨域访问。
        - **pom.xml文件**：Maven 构建配置文件，定义项目依赖和插件。
          ...

4. **生成代码**：
    - 根据您的选择，插件将自动生成所选类型的代码并将其保存到项目的 `generator` 目录下。

## UI

<div align="center">
  <img src="https://pan.imgbed.link/file/104699" alt="功能示例" />
</div>


## 后续将迭代支持

- **常用枚举**：定义系统中常用的枚举类型，例如状态码或角色类型。
- **全局异常类**：处理全局异常，并返回统一格式的错误信息。
- **通用工具类**：提供通用的工具方法，例如字符串处理、日期转换等。
- ...


