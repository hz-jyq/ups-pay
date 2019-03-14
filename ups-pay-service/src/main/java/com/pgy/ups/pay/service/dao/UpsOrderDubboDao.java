package com.pgy.ups.pay.service.dao;



import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.form.UpsOrderForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("upsOrderDubboDao")
public interface UpsOrderDubboDao extends JpaRepository<UpsOrderEntity, Long> {

    @Query(value = "SELECT * FROM UpsOrderEntity c  WHERE IF (#{#form.fromSystem} != '',c.fromSystem = :#{#form.fromSystem} ,1=1) AND IF (#{#form.orderType} !='',c.orderType = :#{#form.orderType} ,1=1)  AND IF (#{#form.bankMd5} !='',c.bankMd5 = :#{#form.bankMd5},1=1) ORDER BY c.id DESC", nativeQuery = true)
    Page<UpsOrderEntity> getPage(@Param("form") UpsOrderForm form, Pageable pageable);

}
