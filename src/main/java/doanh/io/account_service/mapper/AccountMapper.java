package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.entity.Account;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring",
        uses = {
                UserProfileMapper.class,
                ProviderInfoMapper.class,
                SettingsMapper.class
        }
)
public interface AccountMapper {
    AccountDTO toAccountDTO(Account account);
    Account toAccount(AccountDTO accountDTO);
    Account toAccountByOptional(Optional<Account> account);

}
