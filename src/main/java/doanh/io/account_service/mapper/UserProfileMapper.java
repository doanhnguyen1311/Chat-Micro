package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.UserProfileDTO;
import doanh.io.account_service.entity.info.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toDTO(UserProfile userProfile);
    UserProfile toEntity(UserProfileDTO userProfileDTO);
}
