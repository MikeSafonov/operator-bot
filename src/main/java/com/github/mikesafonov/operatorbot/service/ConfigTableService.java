package com.github.mikesafonov.operatorbot.service;

import com.github.mikesafonov.operatorbot.model.ConfigTable;

public interface ConfigTableService {
    ConfigTable findByConfig(String config);
}
