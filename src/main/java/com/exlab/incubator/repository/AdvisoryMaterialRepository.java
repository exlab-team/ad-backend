package com.exlab.incubator.repository;

import com.exlab.incubator.entity.AdvisoryMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisoryMaterialRepository extends JpaRepository<AdvisoryMaterial, Integer> {

}
