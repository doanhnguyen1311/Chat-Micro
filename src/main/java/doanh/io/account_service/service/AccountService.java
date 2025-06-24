package doanh.io.account_service.service;

import doanh.io.account_service.dto.AccountDTO;
import doanh.io.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;



//    void add(AccountDTO accountDTO);
//
//    void update(AccountDTO accountDTO);
//
//    void updatePassword(AccountDTO accountDTO);
//
//    void delete(Long id);
//
//    List<AccountDTO> getAll();
//
//    AccountDTO getOne(Long id);
}
