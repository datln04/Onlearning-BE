package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Device;
import fpt.fall23.onlearn.repository.DeviceRepository;
import fpt.fall23.onlearn.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceRepository deviceRepository;
    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public List<Device> getDeviceByAccountId(Long accountId) {
        return deviceRepository.findDeviceByAccountId(accountId);
    }

    @Override
    public Device getDeviceByTokenAndAccountId(String token, Long accountId) {
        return deviceRepository.findDeviceByTokenAndAccountId(token, accountId);
    }
}
