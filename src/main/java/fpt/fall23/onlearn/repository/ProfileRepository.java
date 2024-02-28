package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile getProfileByEmailIgnoreCase(String email);

}
