package com.pgy.ups.pay.commom.utils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import com.pgy.ups.common.utils.RedisUtils;
import com.pgy.ups.pay.interfaces.cache.Cacheable;

/**
 * UPS缓存模块
 * 
 * @author acer
 *
 */
@Component
public class CacheUtils {

	@Resource
	private RedisUtils redisUtils;

	/**
	 * 所有Cacheable接口
	 */
	@Resource
	private List<Cacheable<? extends Serializable>> list;

	/**
	 * 预加载所有Cacheable内容
	 */
	public void initCacheUtils() {
		for (Cacheable<? extends Serializable> c : list) {
			String keyname = c.getCacheKeyname();
			Map<String, ? extends Serializable> cache = c.getCacheableData();
			redisUtils.hmset(keyname, cache);
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param keyName
	 * @return
	 */
	public <T extends Serializable> Map<String, T> getCacheByRediskeyname(String rediskeyName, Class<T> clazz) {
		return redisUtils.getMap(rediskeyName, clazz);
	}

	/**
	 * 获取缓存
	 * 
	 * @param keyName
	 * @return
	 */
	public <T extends Serializable> T getCacheByRediskeynameAndKey(String rediskeyName, String key, Class<T> clazz) {
		Map<String, T> map = redisUtils.getMap(rediskeyName, clazz);
		if (MapUtils.isEmpty(map)) {
			return null;
		}
		return map.get(key);
	}

	/**
	 * 更新缓存
	 * 
	 * @param keyName
	 * @param cache
	 * @return
	 */
	public boolean setCacheByRediskeyname(String rediskeyName, Map<String, ? extends Serializable> cache) {
		if(Objects.isNull(cache)) {
			return false;
		}
		return redisUtils.hmset(rediskeyName, cache);
	}

	/**
	 * 更新缓存
	 * 
	 * @param keyName
	 * @param cache
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> boolean setCacheByRediskeynameAndKey(String rediskeyName, String key, T value) {
        if(Objects.isNull(value)) {
        	return false;
        }
		Map<String, ? extends Serializable> cache = redisUtils.getMap(rediskeyName, value.getClass());
		if (MapUtils.isEmpty(cache)) {
			Map<String, T> cacheMap = new LinkedHashMap<String, T>();
			cacheMap.put(key, value);
			return setCacheByRediskeyname(rediskeyName, cache);
		}
		((Map<String, T>) cache).put(key, value);
		return setCacheByRediskeyname(rediskeyName, cache);
	}
	

	/**
	 * 统一生成map的key
	 * 
	 * @param param
	 * @return
	 */
	public static String generateKey(String... param) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < param.length; i++) {
			sb.append(param[i]);
			if (i != param.length - 1) {
				sb.append("-");
			}
		}
		return sb.toString();
	}

}
