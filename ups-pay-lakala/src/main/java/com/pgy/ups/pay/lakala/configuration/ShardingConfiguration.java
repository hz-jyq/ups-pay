package com.pgy.ups.pay.lakala.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.ShardingStrategyConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

@Configuration
public class ShardingConfiguration {

	private Logger logger = LoggerFactory.getLogger(ShardingConfiguration.class);

	// 每
	private String[] productIds = { "10001", "10002" };
	
	private String fieldName="product_id";

	// 不分库不要动这个字段
	private String databaseName = "ups-pay";
    
	@Bean(name = "druidDataSource")
	@ConfigurationProperties(prefix = "druid")
	public DataSource getDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean("dataSource")
	public DataSource getShardingDataSource(@Qualifier("druidDataSource") DataSource dataSource) {

		// 配置分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_order", fieldName));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_order_push", fieldName));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_user_sign_log", fieldName));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_user_sign", fieldName));

		Map<String, DataSource> map = new HashMap<>();
		map.put(databaseName, dataSource);

		// 获取数据源对象
		try {
			DataSource shardingDataSource = ShardingDataSourceFactory.createDataSource(map, shardingRuleConfig,
					new ConcurrentHashMap<>(), new Properties());
			return shardingDataSource;
		} catch (Exception e) {
			logger.error("获取shardingDataSource失败：{}", e);
			throw new RuntimeException("获取shardingDataSource失败");
		}

	}

	private TableRuleConfiguration getTableRuleConfiguration(String logicTable, String shardingColumn) {
		TableRuleConfiguration orderPushTableRuleConfig = new TableRuleConfiguration();
		orderPushTableRuleConfig.setLogicTable(logicTable);

		// 分表策略

		orderPushTableRuleConfig.setActualDataNodes(databaseName + "." + logicTable +  "_${1..4}");
		// 分表策略
		ShardingStrategyConfiguration csc = new InlineShardingStrategyConfiguration(shardingColumn,
				logicTable +  "_${product_id}");
	//	orderPushTableRuleConfig.setActualDataNodes(getTables(logicTable));
/*		StandardShardingStrategyConfiguration csc = new StandardShardingStrategyConfiguration(shardingColumn,
				new MyTableShardingAlgorithm());*/
		orderPushTableRuleConfig.setTableShardingStrategyConfig(csc);
		return orderPushTableRuleConfig;
	}

	/**
	 * 拼装所有table节点字符串
	 * 
	 * @param logicTableName
	 * @return
	 */
	private String getTables(String logicTableName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < productIds.length; i++) {
			sb.append(databaseName + "." + logicTableName + "_" + productIds[i].toLowerCase());
			if (i != productIds.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 通过来源商户名分表
	 * 
	 * @author acer
	 *
	 */
	class MyTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

		@Override
		public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
			for (String name : availableTargetNames) {
				if (StringUtils.endsWithIgnoreCase(name, shardingValue.getValue())) {
					return name;
				}
			}
			return null;
		}
	}

}
