<h1>ups pay system</h1>
# 项目简介
 支付系统，用于用户的代付代扣，签约绑卡操作
## 框架
 spring boot + dubbo + redis + sharding-jdbc(以productId分表) + zk +  rateLimit
## 项目启动
启动zk，然后先启动 baofoo,yeapay Application的 dubbo服务器，在启动其他的 Application
## 业务介绍
<hr/>
   1. 有代付代扣，签约，绑卡，协议绑卡，协议签约绑卡<br>
   2. 协议代扣用户必须完成签约绑卡和签约<br>
   3. 如果用户没有完成相应的绑卡，会弹出code 2001，2002让用户去签约绑卡<br>
   4. 默认的是协议签约和普通代付代扣<br>
<br>
<b>数据库介绍</b><br>
<hr/>
  1.   代付代扣的订单表ups_t_order_{产品ID}和ups_t_order_push_{产品id}<br>
  2.   签约绑卡ups_t_user_sign_{产品id}<br>
  3.   配置默认签约的表ups_t_sign_default_config<br>
  4.   配置默认渠道开启路由的表ups_t_merchant_order_type<br>
  5.   配置宝付易宝各个渠道商的配置文件的ups_t_thirdparty_config<br>
 

<br>
<b>流程介绍</b><br>
<hr>

<br>
<b>线上查看和查看jvm工具</b><br>
<hr/>
Arthas<br>
<br>
<b>开发者</b><br>
<hr>
天国的小金，墨凉

    
    


