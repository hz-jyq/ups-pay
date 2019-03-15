package com.pgy.ups.pay.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.PayCompanyEntity;

@Repository
public interface PayCompanyDubboDao extends JpaRepository<PayCompanyEntity, Long>{

}
