package com.exlab.incubator.repository;

import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(RoleName name);

}
