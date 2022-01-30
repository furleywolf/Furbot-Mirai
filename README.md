# 绒狸 - 开源版

[![License](https://img.shields.io/github/license/furleywolf/Furbot-Mirai)](https://github.com/furleywolf/Furbot-Mirai/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/furleywolf/Furbot-Mirai)](https://github.com/furleywolf/Furbot-Mirai/stargazers)
[![Jmeow](https://img.shields.io/badge/author-Jmeow-blue)](http://www.jmeow.org)

### 这是什么

`Furbot-mirai`是一个封装了绒狸基础功能的mirai插件，提供如`来只毛`、`来只 <Name>`、`找毛图 <FID>`等受好评的命令，支持群聊与私聊响应，享受快乐吸毛生活。

### 如何使用

1. 请先安装并配置好[Mirai Console](https://github.com/mamoe/mirai-console) 。
2. 将本插件放置在`/plugins`目录下，并启动一次`mirai-console`，插件会自动生成模板配置文件供填写。
3. 配置文件的位置`config/cn.transfur.furbot/config.yml`，请根据实际需要，填写对应配置项。
4. 配置文件的具体内容见下方`配置相关`。

### 配置相关
```yaml
furbot:
  #此处填写申请开源版地址的QQ号码
  qq: 0
  #此处填写申请开源版地址的授权码
  authKey: 0
  #是否响应私聊消息 true 响应  false 不响应
  responseFriend: true
  #是否响应群聊消息
  responseGroup: true
```