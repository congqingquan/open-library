## 名词解释

### 微信
1. appid：公众号、小程序、移动应用唯一标识
2. mchid：商户申请微信支付后，由微信支付分配的商户收款账号。
3. openid：微信用户在公众号 appid 下的唯一用户标识，可用于永久标记一个用户
4. unionid：微信用户在商家下多个 app 中的唯一标识
5. sub_appid：子 appid，服务商模式/银行服务商模式起作用
6. sub_mch_id：子商户号， 服务商模式/银行服务商模式起作用
7. sub_openid：子 appid 下的唯一标识
8. channel_id：服务商模式下，微信支付分配给收单服务商的ID

### 支付宝
1. app_id：支付宝分配给开发者的应用 ID
2. unionid：微信用户在商家下多个 app 中的唯一标识
3. 商户 uid/pid：支付宝商户号
4. buyer_id：买家的支付宝唯一用户号（2088开头的16位纯数字）
5. org_pid：银行服务商模式下，收单机构(例如银行）的标识，填写该机构在支付宝的 pid
6. merchant_id，银行服务商模式下，特约商户入驻成功之后，分配商户号

## 商户id 与 appid 的关系
1. 微信：商户id 与 appid 为多对多的关系。1个appid可以绑定N个商户号，无上限；一个商户号可以关联50个appid。所以微信支付需要同时传递 appid 与 商户id。对比支付宝，如果只传递一个 appid 是无法确定对应的商户id的。
2. 支付宝：merchantId 与 appid 为一对多的关系：同一个商户号可以绑定多个 appid，但是同一个 appid 只能绑定唯一个商户号。所以支付宝的 SDK 不需要传递商户id，根据 appid 即可在后台查询出商户 id。