package tn.barmegtech.workshopbarmejtechsecurite.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.AuthenticationRequest;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.AuthenticationResponse;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.EleveDto;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.RegisterRequeste;
import tn.barmegtech.workshopbarmejtechsecurite.Dto.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Response> register(
          @RequestBody @Valid EleveDto userRequest,
          HttpServletRequest request
  )  {
    return service.register(userRequest,request);
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  
  

}
