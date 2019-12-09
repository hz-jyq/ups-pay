package com.pgy.ups.pay.commom.dao;

import com.pgy.ups.pay.interfaces.entity.UpsMerchantEntity;
import com.pgy.ups.pay.interfaces.entity.UpsProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UpsMerchantDao extends JpaRepository<UpsMerchantEntity, Long> {



}
