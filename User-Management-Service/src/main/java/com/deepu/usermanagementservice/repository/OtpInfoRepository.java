package com.deepu.usermanagementservice.repository;

import com.deepu.usermanagementservice.entity.OtpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpInfoRepository extends JpaRepository<OtpInfo,String> {
//    boolean existsByEmail()
}
