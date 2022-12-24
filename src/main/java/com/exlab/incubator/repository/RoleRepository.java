package com.exlab.incubator.repository;

import com.exlab.incubator.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findById(long id);
}
