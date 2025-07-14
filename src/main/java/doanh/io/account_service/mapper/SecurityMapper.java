package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.SecurityInfoDTO;
import doanh.io.account_service.entity.info.SecurityInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecurityMapper {
    SecurityInfo toEntity(SecurityInfoDTO securityInfoDTO);
    SecurityInfoDTO toDto(SecurityInfo securityInfo);
}
