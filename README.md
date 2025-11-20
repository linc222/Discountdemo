# 电商优惠券系统 

## 📖 项目简介



本项目是一个简化的电商优惠券系统，旨在解决电商大促场景下的高并发领券和复杂优惠计算问题。系统采用了 **DDD（领域驱动设计）** 的分层思想，并重点实现了以下核心业务：

- **商家端**：创建多种类型的优惠券（满减券、折扣券）。
- **用户端**：查看优惠券、高并发抢券、我的卡包。
- **交易端**：模拟下单核销，自动计算优惠金额。

## 前端展示

![image-20251120205357500](C:\Users\MR\AppData\Roaming\Typora\typora-user-images\image-20251120205357500.png)

## 🛠️ 技术栈

- **核心框架**: Java 17, Spring Boot 3.2.0
- **数据存储**: MySQL 8.0 (JPA / Hibernate)
- **缓存/中间件**: Redis (用于防刷和分布式锁)
- **容器化/部署**: **Docker, Docker Compose** 🆕
- **API 文档**: Swagger / SpringDoc OpenAPI 3
- **前端演示**: Vue 3 + Bootstrap 5 (单页面 HTML，无构建工具依赖)



## ✨ 核心进阶功能



本项目不仅完成了基础需求，还针对高并发和代码设计进行了深入优化：

- **🔒 高并发防超卖 (Optimistic Locking)** 在数据库层面使用 `UPDATE ... SET stock = stock - 1 WHERE stock > 0` 的 CAS 乐观锁机制，确保多线程并发扣减库存时数据的一致性，杜绝超卖现象。
- **🛡️ 接口防刷机制 (Rate Limiting)** 引入 Redis 原子操作，对“领券接口”实施频率限制（如：单用户 5 秒内只能请求一次），有效防止脚本恶意刷券。
- **🧩 策略模式 (Strategy Pattern)** 针对“满减”和“折扣”的不同计算逻辑，定义了 `CalculationStrategy` 接口。通过 `StrategyFactory` 自动分发策略，消除了复杂的 if-else 逻辑，符合开闭原则 (OCP)。
- **🎨 前端零依赖演示** 后端已配置全局跨域 (CORS)，前端仅需一个 HTML 文件即可直接调用 Docker 中的后端接口，无需 Nginx 转发。

------



## 🚀 快速开始 (Quick Start)

### 🐳 方式一：Docker 一键部署 (推荐)



无需本地安装 MySQL 和 Redis，环境完全隔离。

1. **打包应用**:

   ```
   mvn clean package -DskipTests
   ```

2. **启动服务**: 在项目根目录下执行：

   ```
   docker-compose up -d --build
   ```

3. **访问前端**:

   - 进入项目根目录，找到 `index.html`
   - **直接双击打开**即可。
   - 页面会自动连接本地 Docker 容器 (`localhost:8081`)

4. **停止服务**:

   Bash

   ```
   docker-compose down
   ```

------



### 💻 方式二：本地开发环境部署 (调试用)



如果您不使用 Docker，想在 IDE 中运行：

1. **环境准备**: 需本地安装 JDK 17, MySQL 8.0, Redis。
2. **配置**: 修改 `application.yml` 中的数据库和 Redis 连接地址为 `localhost`。
3. **运行**: 在 IDE 中运行 `DiscountDemoApplication.java`。

------



## 📝 API 接口文档

**核心接口逻辑：**

1. **管理员 - 创建优惠券** (`POST /api/v1/admin/coupons`)
2. **用户 - 领取优惠券** (`POST /api/v1/app/coupons/{id}/acquire`) - *含防刷+乐观锁*
3. **用户 - 下单核销** (`POST /api/v1/app/checkout`) - *含策略模式计算*

------



## 📂 项目结构



Plaintext

```
org.example.discountdemo
├── common               // 通用类 (API响应, 异常处理)
├── controller           // 控制层 (Admin/App 接口)
├── entity               // 数据库实体
├── repository           // DAO层
├── service
│   ├── impl             // 业务逻辑
│   └── strategy         // 【核心】策略模式 (满减/折扣)
├── Dockerfile           // 镜像构建文件
├── docker-compose.yml   // 容器编排文件
└── pom.xml
```