package com.pgy.ups.pay.route.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.RouteMerchantChannelEntity;

@Repository
public interface RouteMerchantChannelDao extends JpaRepository<RouteMerchantChannelEntity, Long>{

}
