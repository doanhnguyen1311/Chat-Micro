package doanh.io.account_service.repository;


import aj.org.objectweb.asm.commons.Remapper;
import doanh.io.account_service.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.username = :input OR a.email = :input OR a.phoneNumber = :input")
    Account findByUsernameOrEmailOrPhoneNumber(@Param("input") String input);
}
