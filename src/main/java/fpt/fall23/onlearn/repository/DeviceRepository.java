package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findDeviceByAccountId(Long accountId);

    Device findDeviceByTokenAndAccountId(String token, Long accountId);
}
