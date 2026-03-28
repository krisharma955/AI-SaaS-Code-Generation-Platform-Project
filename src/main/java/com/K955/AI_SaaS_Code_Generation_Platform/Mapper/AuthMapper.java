package com.K955.AI_SaaS_Code_Generation_Platform.Mapper;

import com.K955.AI_SaaS_Code_Generation_Platform.DTOs.Auth.UserProfileResponse;
import com.K955.AI_SaaS_Code_Generation_Platform.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    UserProfileResponse toUserProfileResponse(User user);

}
