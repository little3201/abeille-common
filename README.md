# Leafage Common

### 介绍：

leafage 开源项目的公共模块，提供通用工具和抽象接口；

leafage-basic下 assets 和 hypervisor 两个服务必须的依赖项目，同leafage-basic 模块一样，也分为webmvc和webflux两个版本，现提供：

- BasicService: 基础业务接口；
- AbstractBasicService: 抽象接口，提供随机代码生成逻辑方法；
- FileBasicUtils: 文件操作工具类；

webmvc 版本：

- AbstractControllerMock: controller 相关接口配置;
- AbstractServiceMock: service 相关接口配置;