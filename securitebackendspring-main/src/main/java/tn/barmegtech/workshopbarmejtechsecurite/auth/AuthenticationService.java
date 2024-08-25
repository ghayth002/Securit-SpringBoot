package tn.barmegtech.workshopbarmejtechsecurite.auth;



import static tn.barmegtech.workshopbarmejtechsecurite.auth.UserService.applicationUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.AuthenticationRequest;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.AuthenticationResponse;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.EleveDto;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.RegisterRequeste;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.Response;
import tn.barmegtech.workshopbarmejtechsecurite.configsecurite.JwtService;
import tn.barmegtech.workshopbarmejtechsecurite.configsecurite.securitemodel.Token;
import tn.barmegtech.workshopbarmejtechsecurite.configsecurite.securitemodel.TokenRepository;
import tn.barmegtech.workshopbarmejtechsecurite.configsecurite.securitemodel.TokenType;
import tn.barmegtech.workshopbarmejtechsecurite.entites.Eleve;
import tn.barmegtech.workshopbarmejtechsecurite.entites.Erole;
import tn.barmegtech.workshopbarmejtechsecurite.entites.User;
import tn.barmegtech.workshopbarmejtechsecurite.listner.RegistrationCompleteEvent;
import tn.barmegtech.workshopbarmejtechsecurite.repository.UserRepository;



@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final ApplicationEventPublisher publisher;
//methode register
  public ResponseEntity<Response>  register(RegisterRequeste userRequest,final HttpServletRequest request) {
   // System.err.println(userRequest.getRole());
  
    boolean userExists = repository.findAll()
            .stream()
            .anyMatch(user -> userRequest.getEmail().equalsIgnoreCase(user.getEmail()));

    if (userExists) {
      return ResponseEntity.badRequest().body(Response.builder()
              .responseMessage("User with provided email  already exists!")
              .build());
    }
  /*  if (userRequest.getRole().equalsIgnoreCase("1")) {
    	
    var user = User.builder()
       .firstName(userRequest.getFirstName())
       .lastName(userRequest.getLastName())
        .email(userRequest.getEmail())
        .password(passwordEncoder.encode(userRequest.getPassword()))
        .role(Role.VENDEUR)
        .phone(userRequest.getPhone())
        .adress(userRequest.getAdress())
        //.role(request.getRole())
        .build();
    var savedUser = repository.save(user);
 
    //publisher.publishEvent(new RegistrationCompleteEvent(savedUser, applicationUrl(request)));

    return new ResponseEntity<>(
            Response.builder()

                    .responseMessage("Success! Please, check your email to complete your registration")
                    .email(savedUser.getEmail())
                    
                    .build(),
            HttpStatus.CREATED
    );}*/
    if (userRequest instanceof EleveDto) {
    	Eleve user = new Eleve();
    	   user = EleveDto.Toentite((EleveDto) userRequest);
    	
    	   user.setRole(Erole.ELEVE);
    	   var savedUser = repository.save(user);
    	   publisher.publishEvent(new RegistrationCompleteEvent(savedUser, applicationUrl(request)));

    	   return new ResponseEntity<>(
    	           Response.builder()

    	                   .responseMessage("Success! Please, check your email to complete your registration")
    	                   .email(savedUser.getEmail())
    	                   .build(),
    	           HttpStatus.CREATED
    	   );
    	}
    return null;
  }
//seed
  
  @PostConstruct
  public void createDefaultAdmin() {
      User userADM =new User();
      
      String email50 = "adm@mail.com";
      if (!repository.existsByEmail(email50)) {
    	  userADM.setEmail("adm@mail.com");
    	  userADM.setFullname("ahmed naili");
    	  userADM.setPassword(passwordEncoder.encode("adm"));
    	  userADM.setRole(Erole.PROFFSSEUR);
          repository.save(userADM);
      }
//
     
      
      
   
      
    
      
      
  }
  //
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    
   var claims = new HashMap<String, Object>();
   claims.put("fullname", user.getFullname());
   //claims.put("role", user.getRole().name());
   claims.put("userId", user.getId());
    var jwtToken = jwtService.generateToken(claims,user);
    
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}

