package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.ProviderInfoDTO;
import doanh.io.account_service.entity.info.ProviderInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderInfoMapper {
    ProviderInfoDTO toDTO(ProviderInfo providerInfo);
    ProviderInfo toEntity(ProviderInfoDTO providerInfoDTO);
}
