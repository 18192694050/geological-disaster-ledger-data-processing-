# geological-disaster-ledger-data-processing
鄂西北地质灾害隐患台账数据治理与管理系统

## 项目简介
面向区域地质灾害隐患排查业务，基于PostgreSQL + PostGIS搭建空间数据库，完成隐患点位GPS坐标规整清洗、重复点位去重、矢量空间关联行政区划、灾害风险等级多维度聚合统计；
后端采用SpringBoot开发台账管理接口，结合EasyExcel实现隐患台账批量导入导出；业务成果对接QGIS开展空间图层可视化渲染，完整覆盖**空间数据ETL治理、后端业务开发、GIS空间可视化**全流程。

## 技术栈
- 后端框架：Spring Boot
- 数据库：PostgreSQL + PostGIS（空间扩展）
- Excel处理：Alibaba EasyExcel
- GIS工具：QGIS
- 构建工具：Maven

## 主要模块
- geo-slope-fix：地质灾害隐患台账后台服务，提供台账CRUD、Excel导入导出、空间查询接口
- docs：项目需求文档、数据库设计、接口说明等资料

## 文档说明
项目设计文档存放于 `/docs` 目录。

> 提示：若包含Word文档，GitHub无法在线预览，请下载至本地打开查阅。

## 数据安全声明
本仓库仅开放业务源代码与项目设计文档；涉密原始台账、真实隐患点位数据、数据库备份文件**不纳入版本控制**，严格遵守业务数据保密要求。
