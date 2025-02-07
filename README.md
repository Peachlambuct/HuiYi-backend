# 慧医健康系统项目文档

## 目录
1. [项目概述](#项目概述)
2. [系统架构](#系统架构)
3. [数据库设计](#数据库设计)
4. [核心功能](#核心功能)
5. [数据流说明](#数据流说明)
6. [接口文档](#接口文档)
7. [部署说明](#部署说明)

## 项目概述

### 项目背景
慧医健康系统是一个现代化的医疗预约管理平台，旨在解决传统医疗预约流程繁琐、效率低下的问题。系统提供在线预约、病例管理、检查管理等功能，实现医疗服务的信息化管理。

### 项目特点
- 全流程在线预约
- 自动化病例管理
- 多角色权限控制
- 实时状态追踪
- 数据安全保护

## 系统架构

### 技术栈
- 后端框架：Spring Boot
- ORM框架：MyBatis-Plus
- 安全框架：Spring Security
- 认证方案：JWT
- 工具库：Hutool
- 数据库：MySQL/PostgreSQL

### 项目结构
```
src/main/java/com/beibei/
├── controller/    # 控制器层
├── service/       # 服务接口层
├── service/impl/  # 服务实现层
├── entity/        # 实体类
├── mapper/        # 数据访问层
├── config/        # 配置类
├── handler/       # 处理器
├── utils/         # 工具类
└── filter/        # 过滤器
```

## 数据库设计

### 核心表结构

#### 1. users（用户表）
| 字段名     | 类型     | 说明                       |
| ---------- | -------- | -------------------------- |
| id         | bigint   | 主键                       |
| username   | varchar  | 用户名                     |
| password   | varchar  | 密码                       |
| role       | varchar  | 角色(admin/doctor/patient) |
| status     | boolean  | 状态                       |
| email      | varchar  | 邮箱                       |
| avatar     | varchar  | 头像                       |
| created_at | datetime | 创建时间                   |
| updated_at | datetime | 更新时间                   |
| deleted_at | datetime | 删除时间                   |

#### 2. appointments（预约表）
| 字段名     | 类型     | 说明     |
| ---------- | -------- | -------- |
| id         | bigint   | 主键     |
| patient_id | bigint   | 患者ID   |
| doctor_id  | bigint   | 医生ID   |
| year       | varchar  | 年份     |
| month      | varchar  | 月份     |
| day        | varchar  | 日期     |
| time_id    | int      | 时间段ID |
| status     | boolean  | 预约状态 |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |
| deleted_at | datetime | 删除时间 |

#### 3. doctors（医生表）
| 字段名     | 类型     | 说明     |
| ---------- | -------- | -------- |
| id         | bigint   | 主键     |
| user_id    | bigint   | 用户ID   |
| name       | varchar  | 姓名     |
| job_type   | varchar  | 科室类型 |
| job_title  | varchar  | 职称     |
| status     | boolean  | 状态     |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |
| deleted_at | datetime | 删除时间 |

#### 4. patients（患者表）
| 字段名     | 类型     | 说明     |
| ---------- | -------- | -------- |
| id         | bigint   | 主键     |
| user_id    | bigint   | 用户ID   |
| name       | varchar  | 姓名     |
| birthday   | datetime | 出生日期 |
| sex        | boolean  | 性别     |
| created_at | datetime | 创建时间 |
| updated_at | datetime | 更新时间 |
| deleted_at | datetime | 删除时间 |

#### 5. cases（病例表）
| 字段名     | 类型     | 说明       |
| ---------- | -------- | ---------- |
| id         | bigint   | 主键       |
| patient_id | bigint   | 患者ID     |
| doctor_id  | bigint   | 医生ID     |
| title      | varchar  | 病例标题   |
| content    | text     | 病例内容   |
| status     | boolean  | 状态       |
| check_id   | varchar  | 检查项目ID |
| created_at | datetime | 创建时间   |
| updated_at | datetime | 更新时间   |
| deleted_at | datetime | 删除时间   |

## 核心功能

### 1. 预约管理模块
- 在线预约功能
  - 选择科室和医生
  - 选择预约时间段
  - 自动创建病例记录
- 预约查询功能
  - 按时间查询
  - 按状态查询
  - 查看历史预约
- 预约状态管理
  - 预约确认
  - 预约取消
  - 预约完成

### 2. 病例管理模块
- 病例创建
  - 预约关联创建
  - 基本信息记录
- 病例更新
  - 诊断信息记录
  - 检查项目关联
- 病例查询
  - 按时间查询
  - 按状态查询
  - 按医生查询

## 数据流说明

### 1. 预约流程
```
用户发起预约请求
↓
AppointmentsController.increase()
↓
AppointmentsService.increase()
  ├── 解析时间信息
  ├── 获取患者信息
  ├── 创建预约记录
  └── 创建关联病例
↓
返回预约结果
```

### 2. 查询预约流程
```
用户请求预约列表
↓
AppointmentsController.queryAppointments()
↓
AppointmentsService.queryAppointments()
  ├── 构建查询条件
  ├── 执行数据库查询
  └── 格式化返回数据
↓
返回预约列表
```

### 3. 病例管理流程
```
医生更新病例
↓
CasesController.update()
↓
CasesService.updateCase()
  ├── 更新基本信息
  ├── 关联检查项目
  └── 更新状态信息
↓
返回更新结果
```

## 接口文档

### 1. 用户相关接口
```
POST /api/user/register     # 用户注册
POST /api/user/login       # 用户登录
GET  /api/user/info        # 获取用户信息
POST /api/user/update      # 更新用户信息
```

### 2. 预约相关接口
```
POST /api/appointment/increase    # 创建预约
GET  /api/appointment/list       # 预约列表
POST /api/appointment/update     # 更新预约
GET  /api/appointment/delete     # 删除预约
GET  /api/appointment/latest     # 最近预约
GET  /api/appointment/choose     # 可选时间段
```

### 3. 病例相关接口
```
POST /api/cases/increase    # 创建病例
GET  /api/cases/list       # 病例列表
POST /api/cases/update     # 更新病例
GET  /api/cases/delete     # 删除病例
GET  /api/cases/latest     # 最新病例
```

## 部署说明

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Nginx 1.18+

### 配置说明
1. 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/huiyi_health
    username: your_username
    password: your_password
```

2. JWT配置
```yaml
jwt:
  secret: your_secret_key
  expire: 7200000  # 2小时
```

### 部署步骤
1. 编译打包
```bash
mvn clean package
```

2. 运行服务
```bash
java -jar huiyi-health.jar
```

## 注意事项
1. 预约时间格式必须为"yyyy-MM-dd"
2. 每个预约都会自动创建一个初始状态的病例记录
3. 预约前需确保患者信息已在系统中注册
4. 敏感数据传输需要使用HTTPS
5. 定期备份数据库数据
