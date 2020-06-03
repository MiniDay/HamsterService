# HamsterService
基于Netty的Bukkit服务端间广播消息服务插件

# 现已支持构建工具
[![](https://jitpack.io/v/cn.hamster3/HamsterService.svg)](https://jitpack.io/#cn.hamster3/HamsterService)
## Maven
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```xml
	<dependency>
	    <groupId>cn.hamster3</groupId>
	    <artifactId>HamsterService</artifactId>
	    <version>1.3.2-SNAPSHOT</version>
	    <scope>provided</scope>
	</dependency>
```
## Gradle
```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```
```gradle
	dependencies {
	        compileOnly 'cn.hamster3:HamsterService:1.3.2-SNAPSHOT'
	}
```

# 工作方式
在BungeeCord端上运行的插件会在启动时绑定某一个端口以便开启服务，而在Bukkit端上运行的插件则会在启动时尝试连接上这些服务。

链接成功后，Bukkit端会尝试向服务发送注册信息（服务器的IP和地址以及config中设置的密码

若注册不成功，则服务会发送注册失败的消息并断开这次链接。

否则你的服务就成功运行了，这样其他插件就可以以此为前置向其他的服务端广播一些字符串消息。

HamsterService独立安装时没有什么作用，一般只作为其他插件的前置运行。

# BungeeCord端
启动插件后在插件存档文件夹下
修改server.yml可以设置不同的服务组以及对应的端口
```yaml
# debug配置
debug:
  # 是否在后台输出插件信息
  enable: true
  # 是否在后台输出服务组启动的消息
  start: true
  # 是否在后台输出服务组关闭的消息
  close: true
  # 是否在后台输出注册消息
  register: true
  # 是否在后台输出服务器发送的消息
  receive: true
  # 是否在后台输出发送给服务器的消息
  send: false
  # 是否在后台输出广播的消息
  broadcast: true
  # 是否在后台输出连接的消息
  connect: true

# 消息组
# 不同组之间的消息无法同步
groups:
  # 组名
  default:
    # 绑定本地哪个网卡
    host: "localhost"
    # 端口
    port: 8520
    # 密码
    password: "password"
#  survival:
#    host: "localhost"
#    port: 8521
#    password: "password"
#  skyBlock:
#    host: "localhost"
#    port: 8522
#    password: "password"

```

# Bukkit端
启动插件后在插件存档文件夹下，修改config.yml可以设置绑定的服务服务器地址以及端口
```yaml
debug:
  # 是否在后台输出插件信息
  enable: true
  # 是否在后台输出收到的数据
  receive: true
  # 是否在后台输出发送的数据
  send: true
  # 是否在后台输出连接状态
  connect: true
  # 是否在后台输出注册信息
  register: true

# 服务地址
host: "localhost"
# 服务端口
port: 8520
# 密码
password: "password"
```

先启动BungeeCord服务端，再启动Bukkit服务端。
至此，你的插件已经完全配置完成。

# 命令
以下所有命令均需要`service.admin`权限才运行执行。

|                命令                 |                       描述                       |
| :---------------------------------: | :----------------------------------------------: |
|            /service list            |                查看所有服务组列表                |
|       /service info <服务组>        |               查看某个服务组的信息               |
| /service start <组名> <密码> <端口> |                  打开一个服务组                  |
|       /service close <服务组>       |                  关闭一个服务组                  |
| /service message <子服名称> <消息>  |            单独给某个服务器发一条消息            |
| /service broadcast <服务组> <消息>  |            在一个服务组内广播一条消息            |
|  /service command <服务组> <命令>   | 让该服务组内所有的服务器以控制台身份执行一条命令 |

# 开发者
在BungeeCord端，我们可以使用`cn.hamster3.service.bungee.service.ServiceManager`来获得`ServiceConnection`对象或者`ServiceGroup`对象，它们分别代表子服连接对象和服务组对象。  
在Bukkit端，我们可以直接使用`cn.hamster3.service.spigot.HamsterService`类的静态方法`sendMessage()`来向服务组广播消息。  
当Bukkit服务器启动时，插件会尝试连接至config中配置的地址，并发送相应的注册信息。  
插件因意外而断开连接时，我们会在3秒后尝试重连，在这段时间内你所申请发送的消息都将被缓存，重连成功后我们会自动发送之前未能成功发送的消息。  
默认情况下，Bukkit向BungeeCord发送的所有消息将会被BungeeCord广播（发送方自己也会收到自己发出去的消息）。若想改变这个策略，请编写BungeeCord插件并监听相应的事件作出处理。

## BungeeCord端事件
|             事件名             |           事件描述           |
| :----------------------------: | :--------------------------: |
|     ServiceGroupStartEvent     |        服务组启动事件        |
|     ServiceGroupCloseEvent     |        服务组关闭事件        |
|  ServiceClientConnectedEvent   |    某个服务器连接上服务组    |
| ServiceClientDisconnectedEvent |  某个服务器与服务组断开连接  |
|    ServiceGroupReceiveEvent    |      服务组收到一条消息      |
|     ServiceGroupSendEvent      |     服务组发送了一条消息     |
|   ServicePreSendClientEvent    |    服务组准备发送一条消息    |
|     ServiceSendClientEvent     | 服务组发送了一条有标签的消息 |
|   ServiceClientRegisterEvent   |        客户端注册事件        |
| ServiceGroupPreBroadcastEvent  |      服务组准备广播事件      |
|   ServiceGroupBroadcastEvent   |        服务组广播事件        |

## Bukkit端事件
|          事件名          |      事件描述      |
| :----------------------: | :----------------: |
|   ServiceConnectEvent    | 准备连接服务器事件 |
|  ServiceConnectedEvent   | 服务器连接完成事件 |
|  ServiceDisconnectEvent  |  准备断开连接事件  |
| ServiceDisconnectedEvent |  断开连接完成事件  |
|     ServiceSendEvent     |    发送消息事件    |
|   ServicePreSendEvent    |  准备发送消息事件  |
|   ServiceReceiveEvent    | 收到服务器消息事件 |
|  ServiceReconnectEvent   |    尝试重连事件    |
|   ServiceRegisterEvent   |    准备注册事件    |
|  ServiceRegisteredEvent  |    注册完成事件    |

## 插件自带广播消息
**tag: HamsterService**
  
|           消息格式            |                        消息内容                        |
| :---------------------------: | :----------------------------------------------------: |
|  serverRegistered 服务器名称  |              某服务器成功在该服务组上注册              |
| serverDisconnected 服务器名称 |             某服务器断开了与该服务组的链接             |
|       command 命令内容        |                  让服务器执行这条命令                  |
|      register 服务器密码      | 子服请求注册至服务组, 这条消息不会被广播. 只有BC能收到 |
|     registered 服务器名称     |  注册服务组成功, 这条消息不会被广播. 只有注册方会收到  |
|      registerFailed 原因      |  注册服务组失败, 这条消息不会被广播. 只有注册方会收到  |
