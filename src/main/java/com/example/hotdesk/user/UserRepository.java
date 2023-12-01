package com.example.hotdesk.user;

import com.example.hotdesk.common.repository.GenericSpecificationRepository;
import com.example.hotdesk.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericSpecificationRepository<User, Integer> {
}
