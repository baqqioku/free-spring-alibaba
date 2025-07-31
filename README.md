# Free Spring Alibaba

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Hoxton.SR9-blue.svg)](https://spring.io/projects/spring-cloud)
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.5-red.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 📖 项目介绍

Free Spring Alibaba 是一个基于 Spring Cloud Alibaba 的微服务框架，集成了完整的微服务生态组件，并实现了灰度发布功能。该项目提供了开箱即用的微服务解决方案，帮助企业快速构建高可用、可扩展的分布式系统。

### ✨ 主要特性

- 🚀 **完整的微服务生态**：集成 Spring Cloud Alibaba 全家桶
- 🎯 **灰度发布**：支持基于版本、用户、流量比例的灰度发布策略
- 🔧 **开箱即用**：提供丰富的 Starter 组件，简化微服务开发
- 📊 **监控追踪**：集成 SkyWalking 分布式链路追踪
- 🔒 **分布式事务**：集成 Seata 分布式事务管理
- 📝 **API 文档**：集成 Knife4j 自动生成 API 文档
- 🗄️ **数据存储**：支持 MySQL、Redis 等多种数据源
- 📨 **消息队列**：集成 RocketMQ 消息中间件
- 🔄 **服务治理**：集成 Dubbo RPC 框架

## 🏗️ 技术架构

### 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.2.5.RELEASE | 应用框架 |
| Spring Cloud | Hoxton.SR9 | 微服务框架 |
| Spring Cloud Alibaba | 2.2.5.RELEASE | 阿里微服务生态 |
| Nacos | - | 注册中心和配置中心 |
| Seata | 1.4.2 | 分布式事务 |
| Dubbo | 2.7.7 | RPC 框架 |
| RocketMQ | 2.2.0 | 消息队列 |
| Redis | - | 缓存数据库 |
| MySQL | 8.0.12 | 关系型数据库 |
| MyBatis Plus | 3.4.3.1 | ORM 框架 |
| Knife4j | 3.0.3 | API 文档 |
| SkyWalking | 8.6.0 | 链路追踪 |

### 项目结构

```
free-spring-alibaba/
├── free-framework/                 # 基础框架层
│   ├── free-dependencies/          # 依赖管理
│   ├── free-framework-common/      # 公共组件
│   ├── free-framework-gray/        # 灰度发布组件
│   └── free-framework-starters/    # 框架启动器
│       ├── free-framework-starter-dubbo/      # Dubbo 启动器
│       ├── free-framework-starter-mysql/      # MySQL 启动器
│       ├── free-framework-starter-nacos/      # Nacos 启动器
│       ├── free-framework-starter-redis/      # Redis 启动器
│       ├── free-framework-starter-rocketmq/   # RocketMQ 启动器
│       ├── free-framework-starter-seata/      # Seata 启动器
│       ├── free-framework-starter-statemachine/ # 状态机启动器
│       └── free-framework-starter-web/        # Web 启动器
├── free-business/                  # 业务服务层
│   ├── free-service/              # 主服务
│   ├── free-service-second/       # 服务二
│   ├── free-service-third/        # 服务三
│   ├── free-service-five/         # 服务五
│   ├── free-service-six/          # 服务六
│   ├── free-seven/                # 服务七
│   ├── free-service-eight/        # 服务八
│   └── free-service-ten/          # 服务十
└── free-support/                  # 基础服务层
    ├── free-framework-id/         # ID 生成服务
    └── free-framework-starter-gateway/ # 网关服务
```

## 🚀 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- Nacos 2.0+
- RocketMQ 4.0+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://gitee.com/your-repo/free-spring-alibaba.git
   cd free-spring-alibaba
   ```

2. **配置环境**
   - 启动 Nacos 服务
   - 启动 MySQL 数据库
   - 启动 Redis 服务
   - 启动 RocketMQ 服务

3. **修改配置**
   - 修改 `application.yml` 中的数据库连接信息
   - 修改 Nacos 配置中心的地址
   - 修改 Redis 连接信息

4. **编译项目**
   ```bash
   mvn clean compile
   ```

5. **启动服务**
   ```bash
   # 启动主服务
   mvn spring-boot:run -pl free-business/free-service
   
   # 启动其他服务
   mvn spring-boot:run -pl free-business/free-service-second
   ```

## 📖 使用说明

### 灰度发布

项目集成了灰度发布功能，支持以下灰度策略：

- **版本灰度**：基于服务版本进行流量分发
- **用户灰度**：基于用户标识进行灰度发布
- **流量灰度**：基于流量比例进行灰度发布

使用示例：
```java
@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        // 灰度发布会自动根据配置的路由规则进行流量分发
        return "Hello from service";
    }
}
```

### 服务调用

使用 Feign 进行服务间调用：
```java
@FeignClient(name = "free-service-second")
public interface SecondServiceClient {
    
    @GetMapping("/api/test")
    String test();
}
```

### 分布式事务

使用 Seata 进行分布式事务管理：
```java
@GlobalTransactional
public void businessMethod() {
    // 业务逻辑
    // 自动进行分布式事务管理
}
```

## 🔧 配置说明

### Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yaml
```

### 灰度发布配置

```yaml
gray:
  enabled: true
  rules:
    - service: free-service
      version: v2
      weight: 20
```

## 📊 监控和日志

### SkyWalking 链路追踪

项目集成了 SkyWalking 进行分布式链路追踪，可以监控：
- 服务调用链路
- 接口响应时间
- 异常信息
- 数据库操作

### 日志配置

使用 Logback 进行日志管理，支持：
- 结构化日志输出
- 日志级别动态调整
- 日志文件轮转

## 🤝 参与贡献

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目地址：[https://gitee.com/your-repo/free-spring-alibaba](https://gitee.com/your-repo/free-spring-alibaba)
- 问题反馈：[Issues](https://gitee.com/your-repo/free-spring-alibaba/issues)

## 🙏 致谢

感谢以下开源项目的支持：
- [Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba)
- [Nacos](https://github.com/alibaba/nacos)
- [Seata](https://github.com/seata/seata)
- [Dubbo](https://github.com/apache/dubbo)
- [RocketMQ](https://github.com/apache/rocketmq)

---

⭐ 如果这个项目对你有帮助，请给它一个星标！
