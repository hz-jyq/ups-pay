package com.pgy.ups.pay.commom.dao;

import com.pgy.ups.pay.interfaces.entity.UpsAuthSignEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UpsAuthSignDao extends JpaRepository<UpsAuthSignEntity, Long> {

    /**
     * 解绑
     * @param fromSystem
     * @param userNo
     * @param
     */
   @Modifying
   @Transactional
   @Query(value="update UpsAuthSignEntity e  SET  e.status=20 WHERE e.fromSystem = :fromSystem AND  e.userNo = :userNo AND e.bankMd5 = :bankMd5  AND e.status=10 ")
   void  unbindCard(@Param("fromSystem") String fromSystem,@Param("userNo") String userNo,@Param("bankMd5") String bankMd5);
}
