package com.K955.AI_SaaS_Code_Generation_Platform.Mapper;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Member.MemberResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.ProjectMember;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import com.K955.AI_SaaS_Code_Generation_Platform.Enum.ProjectRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toMemberResponse(User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    MemberResponse toMemberResponseFromProjectMember(ProjectMember projectMember);

}
