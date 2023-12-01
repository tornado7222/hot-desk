package com.example.hotdesk.address;

import com.example.hotdesk.address.entity.Address;
import com.example.hotdesk.common.repository.GenericSpecificationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends GenericSpecificationRepository<Address, Integer> {
}
