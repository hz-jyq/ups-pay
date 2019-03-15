package com.pgy.ups.pay.service.dao;

import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import com.pgy.ups.pay.interfaces.entity.UpsUserSignLogEntity;
import com.pgy.ups.pay.interfaces.form.UpsUserSignForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpsUserSignDubboDao extends JpaRepository<UpsAuthSignEntity, Long> {

    List<UpsAuthSignEntity> queryByForm(UpsUserSignForm form);

}
