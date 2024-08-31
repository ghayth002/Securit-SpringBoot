package tn.barmegtech.workshopbarmejtechsecurite.auth;

import org.springframework.http.ResponseEntity;

import tn.barmegtech.workshopbarmejtechsecurite.Dto.Response;
import tn.barmegtech.workshopbarmejtechsecurite.configsecurite.securitemodel.VerificationToken;
import tn.barmegtech.workshopbarmejtechsecurite.entites.User;




public interface VerificationTokenService {
	
   void saveUserVerificationToken(User user, String token);
   String validateToken(String token);
   ResponseEntity<Response> verifyEmail(String token);
   VerificationToken generateNewVerificationToken(String oldToken);
}
