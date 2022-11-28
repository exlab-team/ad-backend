package com.exlab.incubator.repository;

import com.exlab.incubator.entity.PrivateOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateOfficeRepository extends JpaRepository<PrivateOffice, Integer> {

}
