- 注册（ok）
- 登录（ok）
- 单聊
  - 连接
  - 收发数据
- ip电话
- 群聊
  - 建群
  - 群发消息
  - 群接收消息
  - 退出群聊
- 加好友
  - 显示所有好友
  - 添加好友
  - 好友在不在线
- 对离线用户的处理
  - 发送消息
  - 建立群
  - 添加好友

- 空闲检测+心跳机制







# 基于Netty聊天系统开发文档

用户进入软件后，客户端会打开一个新线程去负责用户在控制台的输入，也负责给服务端发送消息，这样的好处是

1. 使用新线程处理用户的IO事件，不会影响NIO线程
2. 当等待服务端消息时不会发生阻塞（例如登录时给客户端发送LoginRequestMessage后需要阻塞住线程，此时需要等待服务端返回的结果信息，如果不新开一个线程而使用原来的nio线程就会阻塞住接收不到消息）

用户首先需要选择登录或者注册



### 登录

用户会向服务端发送一个`LoginRequestMessage`，其中包括用户名和密码，服务端使用`LoginHandler`处理登录请求消息，这个handler需要进行用户名和密码的匹配，之后返回一个`LoginResponseMessage`，其中包括是否成功登录以及失败原因。成功登录后服务端会使用一个`ConcurrentHashMap`记录哪些用户在线以及他们的channel。

需要注意的是，在客户端发送了`LoginRequestMessage`后需要进行阻塞等待服务端的结果，因此可以使用`CountDownLatch waitLogin = new CountDownLatch(1)`阻塞住，此外还需要设置一个`AtomicBoolean LOGIN = new AtomicBoolean(false)`表示是否成功登录，这两个等NIO线程接收到`LoginResponseMessage`后进行修改。

（遗留：与数据库结合）



### 注册

用户会向服务端发送一个`RegisterRequestMessage`，其中包括用户名和密码，服务端使用`RegisterHandler`处理登录请求消息，这个handler需要首先判断用户名是否重复，之后返回一个`RegisterResponseMessage`，其中包括是否成功注册以及失败原因。阻塞等处理方式与登陆类似。

（遗留：与数据库结合）



### 单聊

单聊使用命令`send [username] [content]`，客户端会向服务端发送一个`ChatRequestMessage`请求，其中包括接收人姓名、消息内容、日期，服务端接收到后会在`ChatHandler`对消息进行处理，并向接收方发送一个`ChatMessage`，如果对方不在线，则发送一个`ChatResponseMessage`（后期改进）

（遗留：对离线用户发送消息的处理）



### 建群

建立群聊使用命令`gcreate [group name] [m1,m2,m3...]`，其中m1，m2,，m3指用户名。客户端向服务端发送一个`CreateGroupRequest`请求，其中包括建立人姓名、群名、邀请成员名、时间，服务端是收到后会在`CreateGroupHandler`对消息进行处理，他会向群内所有人发送一个`RcvGroupMessage`消息，告诉每个人建群的消息。

每个用户都有一个`GroupOperator`对象，他维护一个Group的list以及操作这些list的方法，需要注意的是，为了防止被多线程破环，使用的是`CopyOnWriteArrayList`

（遗留：对离线用户发送建群的处理、群主修改群名并通知所有人）

