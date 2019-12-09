package com.pgy.ups.pay.commom.dao;

import com.pgy.ups.pay.interfaces.entity.UpsProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UpsProductDao extends JpaRepository<UpsProductEntity, Long> {

    @Query(value = "SELECT t1.* from ups_t_merchant t,ups_t_product t1 where t1.merchant_id = t.id and t.state = 1 and t1.state = 1 and t1.id=?1",  nativeQuery = true)
    UpsProductEntity getEnableProduct(Long productId);



}
