package com.pgy.ups.pay.gateway.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pgy.ups.common.annotation.PrintExecuteTime;
import com.pgy.ups.common.exception.ParamValidException;
import com.pgy.ups.common.utils.SpringUtils;
import com.pgy.ups.pay.commom.constants.OrderType;
import com.pgy.ups.pay.commom.constants.RequestAttributeName;
import com.pgy.ups.pay.commom.utils.SecurityUtils;
import com.pgy.ups.pay.commom.utils.UpsResultModelUtil;
import com.pgy.ups.pay.commom.utils.ValidateUtils;
import com.pgy.ups.pay.gateway.factory.BussinessHandlerFactory;
import com.pgy.ups.pay.interfaces.entity.CollectChooseEntity;
import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import com.pgy.ups.pay.interfaces.entity.UpsLogEntity;
import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.entity.UpsSignDefaultConfigEntity;
import com.pgy.ups.pay.interfaces.entity.UpsThirdpartyConfigEntity;
import com.pgy.ups.pay.interfaces.enums.SignTypeEnum;
import com.pgy.ups.pay.interfaces.enums.UpsResultEnum;
import com.pgy.ups.pay.interfaces.model.UpsBindCardParamModel;
import com.pgy.ups.pay.interfaces.model.UpsCollectParamModel;
import com.pgy.ups.pay.interfaces.model.UpsParamModel;
import com.pgy.ups.pay.interfaces.model.UpsPayParamModel;
import com.pgy.ups.pay.interfaces.model.UpsResultModel;
import com.pgy.ups.pay.interfaces.model.UpsSignatureParamModel;
import com.pgy.ups.pay.interfaces.model.UpsUnBindCardModel;
import com.pgy.ups.pay.interfaces.service.authSign.UpsAuthSignService;
import com.pgy.ups.pay.interfaces.service.authSign.UpsSignDefaultConfigervice;
import com.pgy.ups.pay.interfaces.service.config.CollectChooseService;
import com.pgy.ups.pay.interfaces.service.config.MerchantConfigService;
import com.pgy.ups.pay.interfaces.service.config.UpsThirdpartyConfigService;
import com.pgy.ups.pay.interfaces.service.log.UpsLogService;
import com.pgy.ups.pay.interfaces.service.order.UpsOrderService;
import com.pgy.ups.pay.interfaces.service.route.RouteService;

/**
 * 支付网关
 *
 * @author 墨凉
 *
 */

@Controller
@RequestMapping("/index")
public class IndexController {

	private Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Resource
	private HttpServletRequest request;

	@Resource
	private UpsLogService upsLogService;

	@Resource
	private RouteService routeService;

	@Resource
	private UpsThirdpartyConfigService upsThirdpartyConfigService;

	@Resource
	private UpsOrderService upsOrderService;

	@Resource
	private MerchantConfigService merchantConfigService;

	@Resource
	private UpsSignDefaultConfigervice upsSignDefaultConfigervice;

	@Resource
	private CollectChooseService collectChooseService;

	@Resource
	private UpsAuthSignService upsAuthSignBaofooService;

	@Resource
	private BussinessHandlerFactory<Object> bussinessHandlerFactory;

	@Resource
	private UpsAuthSignService upsAuthSignService;

	/**
	 * 代付接口
	 *
	 * @return
	 * @throws ParamValidException
	 */

	@ResponseBody
	@RequestMapping("/pay.do")
	@PrintExecuteTime
	public UpsResultModel upsPay(UpsPayParamModel upsPayParamModel) throws ParamValidException {
		logger.info("接收借款参数：{}", upsPayParamModel);
		// 验证并保存参数，记录日志
		recordAndVerifyParam(upsPayParamModel);

		upsPayParamModel.setOrderType(OrderType.PAY);
		return disposePayOrCollect(upsPayParamModel);

	}

	/**
	 * 代扣接口
	 *
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@RequestMapping("/collect.do")
	@PrintExecuteTime
	public UpsResultModel upsCollect(UpsCollectParamModel upsCollectParamModel) throws ParamValidException {
		logger.info("接收还款参数：{}", upsCollectParamModel);
		recordAndVerifyParam(upsCollectParamModel);
		CollectChooseEntity cce = collectChooseService.queryCollectType(upsCollectParamModel.getFromSystem());
		upsCollectParamModel.setOrderType(Objects.isNull(cce) ? OrderType.COLLECT
				: StringUtils.isBlank(cce.getCollectType()) ? OrderType.COLLECT : cce.getCollectType());

		return disposePayOrCollect(upsCollectParamModel);
	}

	/**
	 * 短信验证码的接口
	 *
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/auth/signature.do")
	public UpsResultModel upsSignature(UpsSignatureParamModel upsSignatureParamModel) throws ParamValidException {
		logger.info("认证签约参数：{}", upsSignatureParamModel);
		recordAndVerifyParam(upsSignatureParamModel);
		upsSignatureParamModel.setOrderType(OrderType.SIGNATRUE);
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsSignatureParamModel);
		upsSignatureParamModel.setPayChannel(payChannel);
		// 处理并返回结果
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsSignatureParamModel)
				.handler(upsSignatureParamModel);
		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 绑卡接口
	 *
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/auth/bindCard.do")
	public UpsResultModel upsBindCard(UpsBindCardParamModel upsBindCardParamModel) throws ParamValidException {
		logger.info("认证绑卡参数：{}", upsBindCardParamModel);
		recordAndVerifyParam(upsBindCardParamModel);
		upsBindCardParamModel.setOrderType(OrderType.BINDCARD);
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsBindCardParamModel);
		upsBindCardParamModel.setPayChannel(payChannel);
		// 处理并返回结果
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsBindCardParamModel)
				.handler(upsBindCardParamModel);
		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 协议签约
	 * 
	 * @param upsBindCardParamModel
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/protocol/signature.do")
	public UpsResultModel protocolSignature(UpsSignatureParamModel upsBindCardParamModel) throws ParamValidException {
		logger.info("协议签约参数：{}", upsBindCardParamModel);
		recordAndVerifyParam(upsBindCardParamModel);
		upsBindCardParamModel.setOrderType(OrderType.PROTOCOL_SIGNATRUE);
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsBindCardParamModel);
		upsBindCardParamModel.setPayChannel(payChannel);
		// 处理并返回结果
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsBindCardParamModel)
				.handler(upsBindCardParamModel);
		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 协议绑卡
	 * 
	 * @param upsBindCardParamModel
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/protocol/bindCard.do")
	public UpsResultModel protocolBindCard(UpsBindCardParamModel upsBindCardParamModel) throws ParamValidException {
		logger.info("协议绑卡参数：{}", upsBindCardParamModel);
		recordAndVerifyParam(upsBindCardParamModel);
		upsBindCardParamModel.setOrderType(OrderType.PROTOCOL_BINDCARD);

		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsBindCardParamModel);
		upsBindCardParamModel.setPayChannel(payChannel);
		// 处理并返回结果
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsBindCardParamModel)
				.handler(upsBindCardParamModel);

		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 统一签约
	 * 
	 * @param upsBindCardParamModel
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/signature.do")
	public UpsResultModel signature(UpsSignatureParamModel upsBindCardParamModel) throws ParamValidException {
		logger.info("统一签约入参：{}", upsBindCardParamModel);
		recordAndVerifyParam(upsBindCardParamModel);
		UpsSignDefaultConfigEntity entity = upsSignDefaultConfigervice
				.queryUpsSignDefaultConfig(upsBindCardParamModel.getFromSystem());
		String signType = entity == null ? SignTypeEnum.PROTOCOL.getCode() : entity.getSignType();
		upsBindCardParamModel.setOrderType(OrderType.SignatureMap.get(signType));
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsBindCardParamModel);
		upsBindCardParamModel.setPayChannel(payChannel);
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsBindCardParamModel)
				.handler(upsBindCardParamModel);
		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 统一绑卡
	 * 
	 * @param upsBindCardParamModel
	 * @return
	 * @throws ParamValidException
	 */
	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/bindCard.do")
	public UpsResultModel bindCard(UpsBindCardParamModel upsBindCardParamModel) throws ParamValidException {
		logger.info("统一绑卡参数：{}", upsBindCardParamModel);
		recordAndVerifyParam(upsBindCardParamModel);
		UpsSignDefaultConfigEntity entity = upsSignDefaultConfigervice
				.queryUpsSignDefaultConfig(upsBindCardParamModel.getFromSystem());
		String signType = entity == null ? SignTypeEnum.PROTOCOL.getCode() : entity.getSignType();
		upsBindCardParamModel.setOrderType(OrderType.BindCardMap.get(signType));
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upsBindCardParamModel);
		upsBindCardParamModel.setPayChannel(payChannel);
		// 处理并返回结果

		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upsBindCardParamModel)
				.handler(upsBindCardParamModel);

		return recordAndReturnResult(upsResultModel);
	}

	@ResponseBody
	@PrintExecuteTime
	@RequestMapping("/unbindCard.do")
	public UpsResultModel unbindCard(UpsUnBindCardModel upsUnBindCardModel) {
		logger.info("统一解绑参数：{}", upsUnBindCardModel);
		upsAuthSignService.unbindCard(upsUnBindCardModel);
		return UpsResultModelUtil.upsResultModelSuccess();
	}

	/**
	 * 处理代付代扣
	 * 
	 * @param upm
	 * @return
	 */

	private UpsResultModel disposePayOrCollect(UpsParamModel upm) {
		// 返回并设置路由
		String payChannel = routeService.obtainAvalibaleRoute(upm);
		upm.setPayChannel(payChannel);

		// 协议代扣验证是否有先签约
		if (Objects.equals(upm.getOrderType(), OrderType.PROTOCOL_COLLECT)) {
			// 查询用户协议信息
			UpsAuthSignEntity uasbe = upsAuthSignBaofooService.queryProtocolSignBaofoo(upm.getFromSystem(),
					upm.getPayChannel(), upm.getUserNo(), upm.getBankCard());
			if (Objects.isNull(uasbe) || StringUtils.isBlank(uasbe.getTppSignNo())) {
				logger.error("宝付协议代扣未查到用户签约信息！查询信息：{}", uasbe);
				return new UpsResultModel(UpsResultEnum.NO_PROPTOCAL);
			}
		}

		// 通过订单查询支付配置信息
		UpsThirdpartyConfigEntity upsThirdpartyConfigEntity = upsThirdpartyConfigService
				.queryThirdpartyConfig(upm.getPayChannel(), upm.getOrderType(), upm.getFromSystem());
		Map<String, Object> configDateMap = JSONObject.parseObject(upsThirdpartyConfigEntity.getConfigDate(),
				new TypeReference<Map<String, Object>>() {
				});
		// 创建订单
		UpsOrderEntity upsOrderEntity = upsOrderService.createUpsOrder(upm);
		// 将第三方渠道调用配置放入订单实体中
		upsOrderEntity.setUpsConfigDate(configDateMap);
		upsOrderEntity.setUpsParamModel(upm);
		// 处理并返回结果
		UpsResultModel upsResultModel = bussinessHandlerFactory.getInstance(upm).handler(upsOrderEntity);
		// UpsResultModel upsResultModel = new UpsResultModel(UpsResultEnum.SUCCESS,
		// "123");
		return recordAndReturnResult(upsResultModel);
	}

	/**
	 * 验证和记录参数
	 *
	 * @param upsParamModel
	 * @throws ParamValidException
	 */

	private void recordAndVerifyParam(UpsParamModel upsParamModel) throws ParamValidException {
		UpsLogEntity upsLogEntity = upsLogService.createUpsLog(upsParamModel, request.getRequestURI());
		// 保存当前请求日志对象
		request.setAttribute(RequestAttributeName.UPS_LOG_ENTITY, upsLogEntity);
		// 参数合法性验证
		ValidateUtils.validate(upsParamModel);
		// 查询商户配置公钥
		String publicKey = merchantConfigService.queryMerchantPublicKey(upsParamModel.getFromSystem());
		// RSA验证签名
		SecurityUtils.signVerification(upsParamModel, publicKey);
		// 获取当前环境
		String profile = SpringUtils.getApplicationContext().getEnvironment().getActiveProfiles()[0];

		logger.info("spring当前运行环境：{}", profile);
		// 非生产环境限制金钱为0.01
		if (!StringUtils.equals(profile.trim(), "prod")) {
			if (upsParamModel instanceof UpsPayParamModel) {
				((UpsPayParamModel) upsParamModel).setAmount(new BigDecimal("0.01"));
			}
			if (upsParamModel instanceof UpsCollectParamModel) {
				((UpsCollectParamModel) upsParamModel).setAmount(new BigDecimal("0.01"));
			}
		}

	}

	/**
	 * 记录返回给业务的结果
	 *
	 * @param upsResultModel
	 */
	private UpsResultModel recordAndReturnResult(UpsResultModel upsResultModel) {
		UpsLogEntity upsLogEntity = (UpsLogEntity) request.getAttribute(RequestAttributeName.UPS_LOG_ENTITY);
		upsLogEntity.setReturnParam(JSONObject.toJSONString(upsResultModel));
		upsLogEntity.setCode(upsResultModel.getCode());
		upsLogEntity.setMessage(upsResultModel.getMessage());
		upsLogService.updateUpsLog(upsLogEntity);
		return upsResultModel;
	}
}
