package com.github.mikesafonov.operatorbot.service.Impl;

import com.github.mikesafonov.operatorbot.exceptions.ConfigTableNotFoundException;
import com.github.mikesafonov.operatorbot.model.ConfigTable;
import com.github.mikesafonov.operatorbot.repository.ConfigTableRepository;
import com.github.mikesafonov.operatorbot.service.ConfigTableService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigTableServiceImpl implements ConfigTableService {
    private final ConfigTableRepository repository;

    @Override
    public ConfigTable findByConfig(String config) {
        return repository.findByConfig(config).orElseThrow(() -> new ConfigTableNotFoundException("Config " + config + " doesn't exists!"));
    }

    @Override
    public void updateConfig(String config, String value) {
        repository.updateConfig(config, value);
    }
}
