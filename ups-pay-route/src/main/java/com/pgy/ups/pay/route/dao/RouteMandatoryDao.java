package com.pgy.ups.pay.route.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.RouteMandatoryEntity;

@Repository
public interface RouteMandatoryDao extends JpaRepository<RouteMandatoryEntity, Long>{

}
