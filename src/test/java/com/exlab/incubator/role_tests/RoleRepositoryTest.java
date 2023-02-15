package com.exlab.incubator.role_tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;
import com.exlab.incubator.repository.RoleRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateRoles(){

        Role user = Role.builder().roleName(RoleName.USER).build();
        Role admin = Role.builder().roleName(RoleName.ADMIN).build();

        roleRepository.save(user);
        roleRepository.save(admin);
        long numberOfRoles = roleRepository.count();

        assertEquals(2, numberOfRoles);
    }

}
