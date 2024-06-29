package com.deepu.usermanagementservice.repository;

import com.deepu.usermanagementservice.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail,String> {

}
