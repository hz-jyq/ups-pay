package com.pgy.ups.pay.service.dao;



import com.pgy.ups.pay.interfaces.entity.UpsOrderEntity;
import com.pgy.ups.pay.interfaces.form.UpsOrderForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UpsOrderDubboDao extends JpaRepository<UpsOrderEntity, Long> {

    @Query(value = "  FROM UpsOrderEntity c  WHERE c.fromSystem = :#{#form.fromSystem} AND       c.orderType = (CASE WHEN :#{#form.orderType} is null THEN true ELSE :#{#form.orderType} END)  AND c.bankMd5 = (CASE WHEN :#{#form.bankMd5} is null THEN true ELSE :#{#form.bankMd5} END)  ORDER BY c.id DESC")
    Page<UpsOrderEntity> getPage(@Param("form") UpsOrderForm form, Pageable pageable);

}
