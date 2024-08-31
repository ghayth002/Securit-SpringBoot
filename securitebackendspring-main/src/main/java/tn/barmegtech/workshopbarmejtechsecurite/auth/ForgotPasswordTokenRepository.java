package tn.barmegtech.workshopbarmejtechsecurite.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tn.barmegtech.workshopbarmejtechsecurite.entites.User;



public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    @Query("select fpt from ForgotPasswordToken fpt where fpt.token = ?1 and fpt.user = ?2 ")
    Optional<ForgotPasswordToken> findByTokenAndUser(Integer token, User user);
}

