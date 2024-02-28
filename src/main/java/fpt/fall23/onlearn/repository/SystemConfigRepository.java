package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {


}
