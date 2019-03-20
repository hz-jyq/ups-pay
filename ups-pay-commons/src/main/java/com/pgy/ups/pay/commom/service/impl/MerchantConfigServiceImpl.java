package com.pgy.ups.pay.commom.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pgy.ups.common.exception.BussinessException;
import com.pgy.ups.pay.commom.dao.MerchantConfigDao;
import com.pgy.ups.pay.commom.utils.CacheUtils;
import com.pgy.ups.pay.interfaces.cache.Cacheable;
import com.pgy.ups.pay.interfaces.entity.MerchantConfigEntity;
import com.pgy.ups.pay.interfaces.service.config.MerchantConfigService;

/**
 * 来源商户业务层
 * 
 * @author 墨凉
 *
 */
@Service
public class MerchantConfigServiceImpl implements MerchantConfigService, Cacheable<MerchantConfigEntity> {

	/**
	 * redis 缓存key
	 */
	public static final String UPS_MERCHANT_CACHE = "ups-merchant-cache";

	private Logger logger = LoggerFactory.getLogger(MerchantConfigServiceImpl.class);

	@Value("${ups.runtime.environment.rsa.path}")
	private String AES_KEY_PATH;

	@Resource
	private MerchantConfigDao merchantConfigDao;

	@Resource
	private CacheUtils cacheUtils;
	
	/**
	 * 查询商户私钥
	 */
	@Override
	public String queryMerchantPrivateKey(String fromSystem) {
		MerchantConfigEntity merchantConfigEntity = findMerchant(fromSystem);
		if (Objects.isNull(merchantConfigEntity) || StringUtils.isEmpty(merchantConfigEntity.getUpsPrivateKey())) {
			logger.error("查询来源商私钥信息异常,fromSystem:{} ,merchantConfig:{}", fromSystem, merchantConfigEntity);
			throw new BussinessException("查询来源商私钥信息异常！");
		}
		String privateKey = merchantConfigEntity.getUpsPrivateKey();
		if(StringUtils.isBlank(privateKey)) {
			throw new BussinessException("读取商户私钥信息异常！");
		}
		return privateKey;
		
	}

	/**
	 * 查询商户公钥
	 */
	@Override
	public String queryMerchantPublicKey(String fromSystem) {
		MerchantConfigEntity merchantConfigEntity = findMerchant(fromSystem);
		if (Objects.isNull(merchantConfigEntity) || StringUtils.isEmpty(merchantConfigEntity.getMerchantPublicKey())) {
			logger.error("查询来源商公钥信息异常,fromSystem:{} ,merchantConfig:{}", fromSystem, merchantConfigEntity);
			throw new BussinessException("查询来源商公钥信息异常！");
		}
		String publicKey = merchantConfigEntity.getMerchantPublicKey();
		if(StringUtils.isBlank(publicKey)) {
			throw new BussinessException("读取商户公钥信息异常！");
		}
		return publicKey;
		
	}

	@Override
	public List<MerchantConfigEntity> queryAvaliableMerchantList() {
		return merchantConfigDao.querByAvaliableMerchantList(true);
	}

	/**
	 * 查询来源商户信息
	 */
	@Override
	public MerchantConfigEntity findMerchant(String fromSystem) {
		//先查询缓存
		MerchantConfigEntity mce = cacheUtils.getCacheByRediskeynameAndKey(UPS_MERCHANT_CACHE,
				fromSystem,MerchantConfigEntity.class);
		if (Objects.isNull(mce)) {
			//若缓存不存在，则从数据库读取，并更新缓存
			mce = merchantConfigDao.queryByMerchant(fromSystem, true);
			cacheUtils.setCacheByRediskeynameAndKey(UPS_MERCHANT_CACHE, fromSystem,mce);
		}
		return mce;
	}

	/**
	 * redis缓存key
	 */
	@Override
	public String getCacheKeyname() {
		return UPS_MERCHANT_CACHE;
	}

	/**
	 * 预加载缓存数据
	 */
	@Override
	public Map<String, MerchantConfigEntity> getCacheableData() {
		List<MerchantConfigEntity> list = queryAvaliableMerchantList();
		Map<String, MerchantConfigEntity> cache = new LinkedHashMap<>();
		for (MerchantConfigEntity mce : list) {
			cache.put(mce.getMerchantName(), mce);
		}
		return cache;
	}

	

}
