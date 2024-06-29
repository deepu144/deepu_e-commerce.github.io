package com.deepu.usermanagementservice.repository;

import com.deepu.usermanagementservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
