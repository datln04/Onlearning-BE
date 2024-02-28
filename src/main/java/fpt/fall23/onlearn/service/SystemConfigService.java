package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.SystemConfig;

import java.util.List;

public interface SystemConfigService {
    SystemConfig saveSystemConfig(SystemConfig systemConfig);

    SystemConfig getSystemConfigById(Long id);

    SystemConfig getLastSystemConfig();


    List<SystemConfig> findAllSystemConfigs();


    Boolean removeSystemConfig(Long id);


}
