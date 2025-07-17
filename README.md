# Assign251_2 项目

## 项目概述
这是一个基于Maven的Java项目，使用JUnit进行测试，并集成了代码覆盖率工具JaCoCo。

## 技术栈
- Java 8
- Maven
- Log4j (日志框架)
- Apache Velocity (模板引擎)
- JUnit Jupiter (测试框架)
- JaCoCo (代码覆盖率工具)

## 项目结构
```
├── src
│   ├── main
│   │   ├── java        # 主要源代码目录
│   │   └── resources   # 资源文件目录
│   └── test
│       └── java        # 测试代码目录
├── coverage            # 代码覆盖率报告目录
└── pom.xml            # Maven配置文件
```

## 依赖
- log4j:1.2.17 - 日志记录
- velocity-engine-core:2.3 - 模板引擎
- junit-jupiter-engine:5.11.2 - 单元测试
- junit-jupiter-params:5.11.2 - 参数化测试

## 构建配置
项目使用Maven进行构建，主要配置包括：
- Java 8 编译目标
- JaCoCo代码覆盖率分析

## 构建和测试
1. 构建项目：
```bash
mvn clean install
```

2. 运行测试：
```bash
mvn test
```

3. 生成代码覆盖率报告：
```bash
mvn jacoco:report
```
覆盖率报告将生成在 `target/site/jacoco` 目录下。