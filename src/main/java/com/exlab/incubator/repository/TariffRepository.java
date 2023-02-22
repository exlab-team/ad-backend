package com.exlab.incubator.repository;

import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.entity.TariffName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    Optional<Tariff> findByTariffName(TariffName name);

}
