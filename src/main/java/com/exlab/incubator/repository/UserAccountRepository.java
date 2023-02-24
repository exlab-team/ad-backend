package com.exlab.incubator.repository;

import com.exlab.incubator.entity.UserAccount;
import com.fasterxml.jackson.annotation.OptBoolean;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUserId(Long userId);

    @Modifying(flushAutomatically = true)
    @Query("""
        update UserAccount acc
        set acc.tariff = :#{#account.tariff}, acc.personalAccount = :#{#account.personalAccount}
        where acc.id = :#{#account.id}
        """)
    int updateWithTariff(@Param("account") UserAccount userAccount);

}
