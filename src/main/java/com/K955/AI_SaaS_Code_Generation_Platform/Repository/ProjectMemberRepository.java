package com.K955.AI_SaaS_Code_Generation_Platform.Repository;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.MemberResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMember;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMemberId;
import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);

    @Query("""
            SELECT pm.projectRole FROM ProjectMember pm
            WHERE pm.id.projectId = :projectId
            AND pm.id.userId = :userId
            """)
    Optional<ProjectRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId,
                                                       @Param("userId") Long userId);


    @Query("""
            SELECT COUNT(pm) FROM ProjectMember pm
            WHERE pm.id.userId = :userId
            AND pm.role = 'OWNER'
            """)
    int countProjectOwnedByUser(@Param("userId") Long userId);
}
