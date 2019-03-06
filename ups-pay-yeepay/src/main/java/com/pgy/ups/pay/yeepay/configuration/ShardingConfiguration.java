package com.pgy.ups.pay.yeepay.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.druid.pool.DruidDataSource;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

@Configuration
public class ShardingConfiguration {

	private Logger logger = LoggerFactory.getLogger(ShardingConfiguration.class);
	
	@Value("${druid.config.path}")
	private String druidConfig;
    
	//每
	private String[] fromSystems = { "meiqi", "shurong" };
    
	//不分库不要动这个字段
	private String databaseName = "ups-pay";

	@Bean("dataSource")
	public DataSource getShardingDataSource() {

		Properties pro = new Properties();
		try {
			pro.load(new ClassPathResource(druidConfig).getInputStream());
		} catch (Exception e) {
			logger.error("druid.properties读取失败：{}", e);
			throw new RuntimeException("druid.properties读取失败");
		}
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.configFromPropety(pro);

		// 配置分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_order","from_system"));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_order_push","from_system"));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_user_sign_log","from_system"));
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration("ups_t_user_sign","from_system"));

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
   
	private TableRuleConfiguration getTableRuleConfiguration(String logicTable,String shardingColumn) {
		TableRuleConfiguration orderPushTableRuleConfig = new TableRuleConfiguration();
		orderPushTableRuleConfig.setLogicTable(logicTable);
		orderPushTableRuleConfig.setActualDataNodes(getTables(logicTable));
		// 分表策略
		StandardShardingStrategyConfiguration csc = new StandardShardingStrategyConfiguration(shardingColumn,
				new MyTableShardingAlgorithm());
		orderPushTableRuleConfig.setTableShardingStrategyConfig(csc);
		return orderPushTableRuleConfig;
	}

    /**
     * 拼装所有table节点字符串
     * @param logicTableName
     * @return
     */
	private String getTables(String logicTableName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fromSystems.length; i++) {
			sb.append(databaseName + "." + logicTableName + "_" + fromSystems[i].toLowerCase());
			if (i != fromSystems.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

}

/**
 * 通过来源商户名分表
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
