package doanh.io.account_service.mapper;

import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toAccountDTO(Account account);
    Account toAccount(AccountDTO accountDTO);
}
