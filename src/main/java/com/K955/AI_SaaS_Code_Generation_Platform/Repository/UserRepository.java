package com.K955.AI_SaaS_Code_Generation_Platform.Repository;

import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

}
