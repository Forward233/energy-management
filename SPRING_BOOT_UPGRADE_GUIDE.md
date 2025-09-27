# Spring Boot 升级指南

## 升级概述

本项目已成功从 **Spring Boot 2.7.18** 升级到 **Spring Boot 3.2.4**，同时升级了相关的依赖和框架版本。

## 版本升级详情

### 核心框架升级

| 组件 | 原版本 | 新版本 | 说明 |
|------|--------|--------|------|
| Java | JDK 1.8 | JDK 17 | Spring Boot 3.x 要求最低 JDK 17 |
| Spring Boot | 2.7.18 | 3.2.4 | 主要升级 |
| Spring Cloud | 2021.0.8 | 2023.0.0 | 配套 Spring Boot 3.x |
| Spring Cloud Alibaba | 2021.0.5.0 | 2023.0.1.0 | 配套 Spring Cloud 2023.x |
| Spring Framework | 5.3.33 | 6.1.5 | 自动升级 |

### 核心依赖升级

| 依赖 | 原版本 | 新版本 | 主要变化 |
|------|--------|--------|----------|
| Dynamic DataSource | 3.6.1 | 4.3.0 | API 构造函数变更 |
| ShardingSphere | 5.2.0/5.2.1 | 5.4.0 | 移除 Spring Boot Starter，API 重构 |
| JWT | 0.9.1 | 0.12.5 | API 完全重构 |
| MyBatis Plus | 3.5.3.1 | 3.5.5 | 兼容性更新 |
| Druid | 1.2.20 | 1.2.21 | 小版本更新 |
| FastJSON | 2.0.43 | 2.0.47 | 安全性更新 |
| Lombok | 1.18.24 | 1.18.30 | 兼容 JDK 17 |
| Netty | 4.1.90.Final | 4.1.107.Final | 安全性更新 |
| Spring Boot Admin | 2.7.15 | 3.2.3 | 配套升级 |
| POI | 4.1.2 | 5.2.5 | 大版本升级 |
| MinIO | 8.2.2 | 8.5.9 | 功能增强 |

### 构建工具升级

| 工具 | 原版本 | 新版本 |
|------|--------|--------|
| Maven Compiler Plugin | 3.1 | 3.11.0 |

## 重大变更说明

### 1. 命名空间迁移（javax → jakarta）

Spring Boot 3.x 迁移到了 Jakarta EE，所有 `javax.*` 包需要替换为 `jakarta.*`：

```java
// 旧版本
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.annotation.PostConstruct;

// 新版本
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.annotation.PostConstruct;
```

### 2. ShardingSphere 重大变更

#### 依赖变更
```xml
<!-- 旧版本 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
    <version>5.2.0</version>
</dependency>

<!-- 新版本 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core</artifactId>
    <version>5.4.0</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-infra-expr-groovy</artifactId>
    <version>5.4.0</version>
</dependency>
```

#### API 变更
```java
// 旧版本
List<String> dataNodes = new InlineExpressionParser(expression).splitAndEvaluate();

// 新版本
InlineExpressionParser parser = InlineExpressionParserFactory.newInstance();
List<String> dataNodes = parser.splitAndEvaluate(expression);
```

#### 接口变更
```java
// 移除的方法（需要注释掉）
// @Override
// public Properties getProps() {
//     return null;
// }
```

### 3. Dynamic DataSource API 变更

```java
// 旧版本
return new AbstractDataSourceProvider() {
    @Override
    public Map<String, DataSource> loadDataSources() {
        // ...
    }
};

// 新版本
return new AbstractDataSourceProvider(dataSourceCreator) {
    @Override
    public Map<String, DataSource> loadDataSources() {
        // ...
    }
};
```

### 4. JWT API 完全重构

```java
// 旧版本
public static Claims parseToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
}

// 新版本
public static Claims parseToken(String token) {
    return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
               .build().parseSignedClaims(token).getPayload();
}
```

### 5. Spring Security 配置更新

```java
// 旧版本
return httpSecurity
    .headers().frameOptions().disable()
    .and().authorizeRequests()
    .antMatchers("/public/**").permitAll()
    .anyRequest().authenticated()
    .and()...

// 新版本
return httpSecurity
    .headers(headers -> headers.frameOptions().disable())
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/public/**").permitAll()
        .anyRequest().authenticated()
    )...
```

## 配置变更

### 1. 添加迁移助手（可选）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-properties-migrator</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 2. Maven 仓库配置

添加了 Maven Central 仓库以确保依赖下载：

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Maven Central</name>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
</repositories>
```

## 编译和运行要求

### 环境要求
- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 推荐
- **内存**: 建议 4GB+ 堆内存

### 编译命令
```bash
# 清理并编译
mvn clean compile

# 跳过测试的完整构建
mvn clean install -DskipTests

# 运行特定模块
mvn spring-boot:run -pl yunpower-auth
```

## 注意事项

### 1. 配置文件兼容性
大部分 Spring Boot 2.x 的配置文件在 3.x 中仍然兼容，但建议检查以下配置：
- 数据源配置
- Security 配置
- Actuator 端点配置

### 2. 第三方库兼容性
- 确保所有第三方库都支持 Spring Boot 3.x
- 某些库可能需要额外的适配工作

### 3. 性能优化
- Spring Boot 3.x 基于 Spring Framework 6.x，利用了 GraalVM 原生镜像支持
- 启动速度和内存使用有所优化

## 验证升级成功

### 1. 编译验证
```bash
mvn clean compile -DskipTests
```
应该看到 `BUILD SUCCESS`

### 2. 启动验证
```bash
java -jar target/your-application.jar
```
检查应用是否正常启动

### 3. 功能验证
- 数据库连接
- API 接口调用
- 认证授权功能
- 分库分表功能

## 回滚方案

如果升级后出现问题，可以通过以下步骤回滚：

1. **代码回滚**: 使用 Git 回滚到升级前的提交
2. **依赖回滚**: 还原 `pom.xml` 中的版本号
3. **配置回滚**: 还原相关配置文件
4. **重新编译**: 使用 JDK 1.8 重新编译

## 升级后的优势

1. **安全性提升**: 新版本修复了大量安全漏洞
2. **性能优化**: 启动速度和运行时性能都有提升
3. **功能增强**: 支持更多新特性和改进
4. **长期支持**: Spring Boot 3.x 是长期支持版本
5. **生态兼容**: 与最新的 Spring 生态系统兼容

## 问题排查

### 常见问题

1. **编译错误**: 检查 JDK 版本是否为 17+
2. **包找不到**: 检查是否有遗漏的 javax → jakarta 迁移
3. **启动失败**: 检查配置文件和依赖兼容性
4. **功能异常**: 检查 API 调用是否适配新版本

### 获取帮助

- 查看 Spring Boot 官方迁移指南
- 检查各依赖库的官方文档
- 查看项目的 issue 和讨论

---

**升级完成日期**: 2025-09-27  
**升级人员**: AI Assistant  
**测试状态**: 编译通过 ✅  
**部署状态**: 待部署  
