package com.pgy.ups.pay.service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import com.pgy.ups.pay.interfaces.form.UpsUserSignForm;

@Repository
public interface UpsUserSignDubboDao extends JpaRepository<UpsAuthSignEntity, Long> {


    @Query(value = " select  * FROM ups_t_user_sign c  WHERE c.from_system = :#{#form.fromSystem} AND IF (:#{#form.orderType} !='',c.order_type = :#{#form.orderType}  ,1=1) AND IF (:#{#form.bankMd5} !='',c.bank_md5 = :#{#form.bankMd5} ,1=1)",nativeQuery = true)
    List<UpsAuthSignEntity> queryByForm(UpsUserSignForm form);

}
