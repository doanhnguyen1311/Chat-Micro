package doanh.io.authentication_service.repository;

import doanh.io.authentication_service.entity.RefreshToken;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.userId = :userId AND rt.deviceId = :deviceId")
    void deleteByUserIdAndDeviceId(@Param("userId") String userId, @Param("deviceId") String deviceId);

    void deleteByUserId(String userId);

    @Transactional
    void deleteByUserIdAndIsDefaultDeviceFalse(String userId);

    Optional<RefreshToken> findByUserIdAndIsDefaultDeviceTrue(String userId);
}
