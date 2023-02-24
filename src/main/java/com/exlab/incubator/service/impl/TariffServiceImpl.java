package com.exlab.incubator.service.impl;

import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.entity.TariffName;
import com.exlab.incubator.exception.TariffNameException;
import com.exlab.incubator.repository.TariffRepository;
import com.exlab.incubator.service.TariffService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffServiceImpl implements TariffService {

    private final TariffRepository repository;

    @Autowired
    public TariffServiceImpl(TariffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tariff createTariffIfNotExist(TariffDto tariff) {
        TariffName tariffName = TariffName.of(tariff.getName());

        if (Objects.isNull(tariffName)){
            throw new TariffNameException("No such tariff name");
        }
        return repository.findByTariffName(tariffName)
            .orElseGet(() -> repository.save(
                Tariff.builder()
                    .tariffName(tariffName)
                    .price(tariff.getPrice()).build()));
    }
}
