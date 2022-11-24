package com.exlab.incubator.repository;

import com.exlab.incubator.entity.CardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDataRepository extends JpaRepository<CardData, Integer> {
}
