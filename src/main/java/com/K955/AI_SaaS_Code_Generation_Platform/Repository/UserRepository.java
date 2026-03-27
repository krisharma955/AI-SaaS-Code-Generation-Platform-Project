package com.K955.AI_SaaS_Code_Generation_Platform.Repository;

import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
