package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.SystemConfig;
import fpt.fall23.onlearn.repository.SystemConfigRepository;
import fpt.fall23.onlearn.service.SystemConfigService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {


    @Autowired
    SystemConfigRepository systemConfigRepository;


    @Override
    public SystemConfig saveSystemConfig(SystemConfig systemConfig) {
        systemConfig.setDateCreate(LocalDate.now());
        return systemConfigRepository.save(systemConfig);
    }

    @Override
    public SystemConfig getSystemConfigById(Long id) {
        Optional<SystemConfig> systemConfig = systemConfigRepository.findById(id);
        if (systemConfig.isPresent()) {
            return systemConfig.get();
        }
        return null;
    }

    @Override
    public List<SystemConfig> findAllSystemConfigs() {
        return systemConfigRepository.findAll();
    }

    @Override
    public Boolean removeSystemConfig(Long id) {
        Optional<SystemConfig> systemConfig = systemConfigRepository.findById(id);
        if (systemConfig.isPresent()) {
            systemConfigRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SystemConfig getLastSystemConfig() {
        Query query = entityManager.createNativeQuery("SELECT * FROM system_config ORDER BY id DESC", SystemConfig.class);
        query.setMaxResults(1); // Fetch only the first result
        List<SystemConfig> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
