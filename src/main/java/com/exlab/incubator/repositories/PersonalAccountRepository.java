package com.exlab.incubator.repositories;

import com.exlab.incubator.entities.PersonalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAccountRepository extends JpaRepository<PersonalAccount, Integer> {

}
