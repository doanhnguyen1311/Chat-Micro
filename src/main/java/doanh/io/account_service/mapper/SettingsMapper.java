package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.SettingsDTO;
import doanh.io.account_service.entity.info.Settings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingsMapper {
    SettingsDTO toDto(Settings settings);
    Settings toEntity(SettingsDTO settingsDTO);
}
