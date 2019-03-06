package com.pgy.ups.pay.baofoo.pay.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.pgy.ups.common.annotation.PrintExecuteTime;
import com.pgy.ups.common.exception.BussinessException;
import com.pgy.ups.common.utils.DateUtils;
import com.pgy.ups.common.utils.OkHttpUtil;
import com.pgy.ups.pay.baofoo.model.BaoFooCollectPrivateModel;
import com.pgy.ups.pay.baofoo.utils.BaofooRsaCodingUtil;
import com.pgy.ups.pay.baofoo.utils.BaofooSecurityUtil;
import com.pgy.ups.pay.commom.constants.OrderStatus;
import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.entity.UpsThirdpartyLogEntity;
import com.pgy.ups.pay.interfaces.enums.UpsResultEnum;
import com.pgy.ups.pay.interfaces.model.UpsResultModel;
import com.pgy.ups.pay.interfaces.pay.BussinessHandler;
import com.pgy.ups.pay.interfaces.service.log.UpsThirdpartyLogService;
import com.pgy.ups.pay.interfaces.service.order.UpsOrderService;

/**
 * 代扣 还款
 * 
 * @author 墨凉
 *
 */
@Service(group = "baofooCollect",timeout=60000,retries=0)
public class BaofooCollectHandler implements BussinessHandler<UpsOrderEntity, UpsResultModel> {

	/**
	 * 公钥私钥相对classpath的路径
	 */
	@Value("${ups.runtime.environment.rsa.path}")
	private String RSA_KEY_PATH;
	
	@Resource
	private BaofooCollectHttpRemoteInvoke baofooCollectHttpRemoteInvoke;

	private Logger logger = LoggerFactory.getLogger(BaofooCollectHandler.class);


	@Override
	public UpsResultModel handler(UpsOrderEntity orderEntity) throws BussinessException{

		// 获取公共配置信息
		Map<String, Object> baofooConfig = orderEntity.getUpsConfigDate();
		String member_id = MapUtils.getString(baofooConfig, "member_id", "");
		String terminal_id = MapUtils.getString(baofooConfig, "terminal_id", "");
		String biz_type = MapUtils.getString(baofooConfig, "biz_type", "");
		String txn_type = MapUtils.getString(baofooConfig, "txn_type", "");
		String data_type = MapUtils.getString(baofooConfig, "data_type", "");
		String private_key = MapUtils.getString(baofooConfig, "private_key", "");
		String public_key = MapUtils.getString(baofooConfig, "public_key", "");
		String version = MapUtils.getString(baofooConfig, "version", "");
		String key_store_password = MapUtils.getString(baofooConfig, "key_store_password", "");
		String request_url = MapUtils.getString(baofooConfig, "request_url", "");

		if (StringUtils.isAnyBlank(member_id, terminal_id, biz_type, txn_type, data_type, private_key, public_key,
				version, key_store_password, request_url)) {
			logger.error("读取宝付代扣配置信息错误:{}", baofooConfig);
			throw new BussinessException("读取宝付代扣配置信息错误");
		}
		private_key = RSA_KEY_PATH + private_key;
		public_key = RSA_KEY_PATH + public_key;
		// 拼装个人信息
		BaoFooCollectPrivateModel bpm = new BaoFooCollectPrivateModel();
		bpm.setBiz_type(biz_type);
		bpm.setMember_id(member_id);
		bpm.setTerminal_id(terminal_id);
		// 四要素必须从getUpsParamModel中获取，因为orderEntity中的四要素是加密过的
		bpm.setAcc_no(orderEntity.getUpsParamModel().getBankCard());
		bpm.setId_card(orderEntity.getUpsParamModel().getIdentity());
		bpm.setId_holder(orderEntity.getUpsParamModel().getRealName());
		bpm.setMobile(orderEntity.getUpsParamModel().getPhoneNo());

		bpm.setPay_code(orderEntity.getBankCode());
		bpm.setTrade_date(DateUtils.dateToString(orderEntity.getCreateTime(), "yyyyMMddHHmmss"));
		bpm.setTrans_id(orderEntity.getUpsOrderCode());
		bpm.setTrans_serial_no(orderEntity.getId() + "");
		bpm.setTxn_amt(orderEntity.getAmount().multiply(new BigDecimal("100")).toPlainString());// 元转为分
		bpm.setTxn_sub_type("13"); // 代付为13 查询为31

		// 个人信息转json
		String contentJson = JSONObject.toJSONString(bpm);
		logger.info("宝付代扣加密前报文：{}", contentJson);
		String contentJsonCiphertext = null;
		try {
			contentJsonCiphertext = BaofooSecurityUtil.Base64Encode(contentJson);
			// 再私钥加密
			contentJsonCiphertext = BaofooRsaCodingUtil.encryptByPriPfxFile(contentJsonCiphertext, private_key,
					key_store_password);
		} catch (UnsupportedEncodingException e) {
			logger.error("宝付代扣参数base64加密异常:{}", ExceptionUtils.getStackTrace(e));
			throw new BussinessException("宝付代扣参数base64加密异常！");
		}
		logger.info("宝付代扣加密后用户信息：{}", contentJsonCiphertext);

		// 拼装发送报文
		Map<String, String> postData = new LinkedHashMap<>();
		// 版本号
		postData.put("version", version);
		// 终端号
		postData.put("terminal_id", terminal_id);
		// 交易类型
		postData.put("txn_type", txn_type);
		// 交易子类
		postData.put("txn_sub_type", "13"); // 代付为13 查询为31
		// 商户号
		postData.put("member_id", member_id);
		// 数据类型
		postData.put("data_type", data_type);
		// 加密数据
		postData.put("data_content", contentJsonCiphertext);

		baofooCollectHttpRemoteInvoke.httpRemoteInvoke(request_url, postData, public_key,
				orderEntity, contentJson);
	

		return new UpsResultModel(UpsResultEnum.SUCCESS, orderEntity.getId());

	}

	

}

@Component
class BaofooCollectHttpRemoteInvoke{
	
	private Logger logger = LoggerFactory.getLogger(BaofooCollectHandler.class);

	@Resource
	private UpsOrderService upsOrderService;

	@Resource
	private UpsThirdpartyLogService upsThirdpartyLogService;
	
	
	/* 宝付代扣 交易受理编码 */
	private static final String[] COLLECT_ACCEPT_CODES = { "0000", "BF00114" };

	/* 宝付代扣 未知编码 */
	/*private static final String[] COLLECT_UNKNOWN_CODES = { "BF00100", "BF00112", "BF00113", "BF00115", "BF00144",
			"BF00202" };*/
	
	@Async
	@PrintExecuteTime
	public void httpRemoteInvoke(String request_url, Map<String, String> postData, String public_key,
			UpsOrderEntity orderEntity, String contentJson) {
		
		// HttpClient调用
		String resutlStr = "";
		try {
			resutlStr = OkHttpUtil.postForm(request_url, postData);
		} catch (IOException e) {
			logger.error("调用宝付代扣接口异常:{}", ExceptionUtils.getStackTrace(e));
			orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_ERROR);
			orderEntity.setRemark("调用宝付代扣接口异常！");
			upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
			return;
		}
		logger.info("宝付代扣返回信息：{}", resutlStr);
		// 创建与第三方交互日志
		UpsThirdpartyLogEntity utl = new UpsThirdpartyLogEntity();
		utl.setFormSystem(orderEntity.getFromSystem());
		utl.setOrderType(orderEntity.getOrderType());
		utl.setPayChannel(orderEntity.getPayChannel());
		utl.setUpsOrderCode(orderEntity.getUpsOrderCode());
		utl.setRequestUrl(request_url);
		utl.setRequestText(contentJson);
		postData.put("data_content", contentJson);
		utl.setRequestText(JSONObject.toJSONString(postData));
		// 处理返回结果
		disposeResult(resutlStr, public_key, orderEntity, utl);
	}

	/**
	 * 处理返回结果
	 * 
	 * @param resutlStr
	 * @param public_key
	 * @param orderEntity
	 * @param utl
	 * @return
	 */
	private void disposeResult(String resutlStr, String public_key, UpsOrderEntity orderEntity,
			UpsThirdpartyLogEntity utl) {
		// 第一步报文AES解密
		String deciphertext = BaofooRsaCodingUtil.decryptByPubCerFile(resutlStr, public_key);
		if (StringUtils.isEmpty(deciphertext)) {
			// 修改订单状态
			orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_ERROR);
			orderEntity.setRemark("RSA解密宝付代付返回报文发生异常！");
			upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
			// 保存第三方支付渠道交互日志
			utl.setReturnText(deciphertext);
			upsThirdpartyLogService.createThirdpartyLog(utl);
			return;
		}
		// 第二步base64解密
		try {
			deciphertext = BaofooSecurityUtil.Base64Decode(deciphertext);
		} catch (IOException e) {
			logger.error("base64解码宝付代付加密报文出错：{}", ExceptionUtils.getStackTrace(e));
			// 修改订单状态
			orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_ERROR);
			orderEntity.setRemark("base64解码宝付代付加密报文出错！");
			upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
			// 保存第三方支付渠道交互日志
			utl.setReturnText(resutlStr);
			upsThirdpartyLogService.createThirdpartyLog(utl);
			return;
		}
		// 保存第三方支付渠道交互日志
		logger.info("宝付代扣返回明文：{}", deciphertext);
		utl.setReturnText(deciphertext);
		upsThirdpartyLogService.createThirdpartyLog(utl);
		// 解析报文字符串为map
		Map<String, String> result = parseReturnMessage(deciphertext);
		String resp_code = MapUtils.getString(result, "resp_code");
		String resp_msg = MapUtils.getString(result, "resp_msg");
		orderEntity.setResultCode(resp_code);
		orderEntity.setResultMessage(resp_msg);
		// 返回受理成功
		if (ArrayUtils.contains(COLLECT_ACCEPT_CODES, resp_code)) {
			orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_DISPOSE);
			upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
			return;
		}
		/*// 返回未知状态
		if (ArrayUtils.contains(COLLECT_UNKNOWN_CODES, resp_code)) {
			orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_DISPOSE);
			upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
			return;
		}*/
		//其他情况均视为异常
		orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_ERROR);
		upsOrderService.updateOrderAndCreateOrderPush(orderEntity);
	}

	/**
	 * 解析返回的明文
	 * 
	 * @param returnMessage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> parseReturnMessage(String returnMessage) {
		return JSONObject.parseObject(returnMessage, Map.class);
	}
	
}
