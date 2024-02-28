package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Device;

import java.util.List;

public interface DeviceService {
    Device save(Device device);
    List<Device> getDeviceByAccountId(Long accountId);

    Device getDeviceByTokenAndAccountId(String token, Long accountId);
}
