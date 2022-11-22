package com.exlab.incubator.repositories;

import com.exlab.incubator.entities.CardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDataRepository extends JpaRepository<CardData, Integer> {
}
