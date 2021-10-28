package com.grin.lexicom.repository;

import com.grin.lexicom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    User findByEmail(String email);

    User findByPhone(String phone);
}
